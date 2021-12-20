package io.github.myungpyo.stickystate.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate
import io.github.myungpyo.stickystate.StickyState
import java.io.OutputStream

/**
 * Symbol processor to generate sticky state bindings.
 */
@KspExperimental
class StickyStateProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>,
) : SymbolProcessor {

    companion object {
        private val STICKY_ANNOTATION_NAME = StickyState::class.java.name
        const val STATE_HOLDER = "stateHolder"
        const val STATE_STORE = "stateStore"
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbolMap = resolver
            .getSymbolsWithAnnotation(STICKY_ANNOTATION_NAME)
            .filterIsInstance<KSPropertyDeclaration>()
            .groupBy {
                resolver.getOwnerJvmClassName(it) ?: run {
                    logger.warn("Failed to find class name of property $it")
                    return emptyList()
                }
            }
        if (symbolMap.isEmpty()) return emptyList()

        val invalidSymbols = mutableListOf<KSPropertyDeclaration>()
        symbolMap.keys.forEach { ownerClassName ->
            val containingFile = symbolMap[ownerClassName]?.getOrNull(0)?.containingFile
            invalidSymbols.addAll(
                generateBinding(resolver, ownerClassName, containingFile, symbolMap[ownerClassName] ?: emptyList())
            )
        }
        return invalidSymbols
    }

    private fun generateBinding(
        resolver: Resolver,
        ownerClassName: String,
        containingFile: KSFile?,
        propertyDeclarations: List<KSPropertyDeclaration>
    ): List<KSPropertyDeclaration> {
        val ownerFile = containingFile?.let { arrayOf(it) } ?: resolver.getAllFiles().toList().toTypedArray()
        val ownerPackageName = ownerClassName.substringBeforeLast(".")
        val ownerClassSimpleName = ownerClassName.substringAfterLast(".")
        val bindingClassName = "${ownerClassSimpleName}StateBinding"

        val saveCodeList = mutableListOf<String>()
        val restoreCodeList = mutableListOf<String>()
        propertyDeclarations.forEach { it.accept(StickyStateVisitor(saveCodeList, restoreCodeList, logger), Unit) }

        val outputStream = codeGenerator.createNewFile(
            dependencies = Dependencies(
                aggregating = false,
                sources = ownerFile
            ),
            packageName = ownerPackageName,
            fileName = bindingClassName,
        )

        outputStream.use {
            with(CodeLoom(it)) {
                write("package $ownerPackageName").lineWrap(repeat = 2)
                write("import android.os.Bundle").lineWrap(repeat = 2)
                writeWithOpenBracket("object $bindingClassName").lineWrap()

                lineWrap()

                // Save function
                writeWithOpenBracket("fun save($STATE_HOLDER: $ownerClassSimpleName, $STATE_STORE: Bundle)").lineWrap()
                writeWithOpenBracket("with($STATE_HOLDER)").lineWrap()
                saveCodeList.forEach { saveCode ->
                    write(saveCode).lineWrap()
                }
                closeBracket().lineWrap()
                closeBracket().lineWrap(repeat = 2)

                // Restore function
                writeWithOpenBracket("fun restore($STATE_HOLDER: $ownerClassSimpleName, $STATE_STORE: Bundle?)").lineWrap()
                write("$STATE_STORE ?: return").lineWrap()
                restoreCodeList.forEach { restoreCode ->
                    write(restoreCode).lineWrap()
                }
                closeBracket().lineWrap()

                lineWrap()
                closeBracket().lineWrap()
            }
        }

        return propertyDeclarations.filterNot { it.validate() }.toList()
    }
}

class CodeLoom(private val outputStream: OutputStream) {
    private var indentLevel: Int = 0

    fun write(code: String): CodeLoom {
        outputStream += "\t".repeat(indentLevel)
        outputStream += code
        return this
    }

    fun writeWithOpenBracket(code: String): CodeLoom {
        write(code)
        outputStream += " {"
        indentLevel++
        return this
    }

    fun lineWrap(repeat: Int = 1): CodeLoom {
        outputStream += "\n".repeat(repeat)
        return this
    }

    fun closeBracket(): CodeLoom {
        indentLevel--
        outputStream += "\t".repeat(indentLevel)
        outputStream += "}"
        return this
    }

    private operator fun OutputStream.plusAssign(str: String) {
        this.write(str.toByteArray())
    }
}