package io.github.myungpyo.stickystate.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Nullability
import io.github.myungpyo.stickystate.processor.StickyStateProcessor.Companion.STATE_HOLDER
import io.github.myungpyo.stickystate.processor.StickyStateProcessor.Companion.STATE_STORE

class StickyStateVisitor(
    private val saveCodeList: MutableList<String>,
    private val restoreCodeList: MutableList<String>,
    private val logger: KSPLogger,
): KSVisitorVoid() {

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
        val propertyName = property.simpleName.asString()
        val resolvedType = property.type.resolve()
        val propertyType = getPropertyType(resolvedType)
        val genericType = getGenericType(resolvedType) // Non-Empty String if it has generic type
        val propertyAccessorName = getPropertyAccessorName(propertyType, genericType)

        // Generate put code
        var saveCode = "$STATE_STORE.put$propertyAccessorName(\"${makePropertyStoreKey(propertyName)}\", $STATE_HOLDER.$propertyName)"
        if (resolvedType.nullability == Nullability.NULLABLE) {
            saveCode = "$propertyName?.let{ $saveCode }"
        }
        saveCodeList.add(saveCode)

        // Generate restore code
        var restoreCode = "$STATE_HOLDER.$propertyName = $STATE_STORE.get$propertyAccessorName(\"${makePropertyStoreKey(propertyName)}\")"
        if (resolvedType.nullability != Nullability.NULLABLE && shouldUseDefaultValue(propertyType)) {
           restoreCode = "$restoreCode ?: $STATE_HOLDER.$propertyName"
        }
        restoreCodeList.add(restoreCode)
    }

    private fun shouldUseDefaultValue(propertyType: String): Boolean {
        if (propertyType.endsWith("Array")
            || propertyType.endsWith("ArrayList")
            || propertyType.endsWith("SparseArray")
        ) return true

        return propertyType in listOf("String", "Bundle", "CharSequence", "Parcelable", "Size", "SizeF")
    }

    private fun makePropertyStoreKey(propertyName: String): String {
        return "StickyState_$propertyName"
    }

    private fun getGenericType(resolvedType: KSType): String {
        if (resolvedType.arguments.isEmpty()) return ""

        if (resolvedType.arguments.size != 1) {
            logger.warn("Failed to resolve type argument of $resolvedType")
            return ""
        }

        val genericType = resolvedType.arguments[0].type ?: run {
            logger.warn("Failed to resolve generic type of of $resolvedType")
            return ""
        }

        return genericType.toString()
    }

    private fun getPropertyType(resolvedType: KSType): String {
        return resolvedType.declaration.simpleName.asString()
    }

    private fun getPropertyAccessorName(propertyType: String, genericType: String = ""): String {
        val accessorPrefix = when (propertyType) {
            "ArrayList" -> if (genericType == "Int") "Integer" else genericType
            "SparseArray" -> "Sparse$genericType"
            else -> genericType
        }
        val accessorSuffix = when (propertyType) {
            "SparseArray" -> "Array"
            else -> propertyType
        }
        return "$accessorPrefix$accessorSuffix"
    }
}