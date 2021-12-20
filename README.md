![Generic badge](https://img.shields.io/badge/version-1.0.0-blue.svg)

# Sticky State Android
A library that helps you save and restore UI states as using `@StickyState` annotation provided by `core` module.    
You can apply the annotation to any of the properties which is supported from StickyState library in a class.    
(You can find the supported types in the [Supported types](#supported-types) section)


## Configurations
Apply the `com.google.devtools.ksp` plugin with the specified version to your module.
(You can find the most recent version [here](https://mvnrepository.com/artifact/com.google.devtools.ksp/symbol-processing-api))

```kotlin
plugins {
  id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}
```

Add `core` module and `processor` symbol processor module of the StickyState to the list of the module dependencies.

```kotlin
dependencies {
  implementation("io.github.myungpyo:stickystate-core:1.0.0")
  ksp("io.github.myungpyo:stickystate-processor:1.0.0")
}
```

## Usage
In your activity, you can use `@StickyState` like below.

```kotlin
class MainActivity : AppCompatActivity() {

  @StickyState
  var stringProp: String = "aaa"

  @StickyState
  var stringArrayProp: Array<String> = arrayOf("a", "b", "c")

  @StickyState
  var stringArrayListProp: ArrayList<String> = arrayListOf("a", "b", "c")

  @StickyState
  var intProp: Int = 1


  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.main_activity)

      MainActivityStateBinding.restore(this, savedInstanceState)
  }

  override fun onSaveInstanceState(outState: Bundle) {
      MainActivityStateBinding.save(this, outState)
      super.onSaveInstanceState(outState)
  }
  
}
```

In your fragment, you can use `@StickyState` like below.

```kotlin
class MainFragment : Fragment() {

    @StickyState
    var inputName: String = ""

    @StickyState
    var inputAge: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        MainFragmentStateBinding.restore(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        MainFragmentStateBinding.save(this, outState)
        super.onSaveInstanceState(outState)
    }
}
```

Additionally, You can also use this library in any class, including activities and fragments.

## Supported types
The following is a list of types supported by the StickyState library.

|     Supported types     |
|:-----------------------:|
| String                  |
| Array<String>           |
| ArrayList<String>       |
| Byte                    |
| ByteArray               |
| Char                    |
| CharArray               |
| Short                   |
| ShortArray              |
| Int                     |
| IntArray                |
| ArrayList<Int>          |
| Long                    |
| LongArray               |
| Float                   |
| FloatArray              |
| Double                  |
| DoubleArray             |
| Boolean                 |
| BooleanArray            |
| Bundle                  |
| CharSequence            |
| Parcelable              |
| Size                    |
| SizeF                   |
| SparseArray<Parcelable> |