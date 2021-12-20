![Generic badge](https://img.shields.io/badge/version-1.0.0-blue.svg)

# Sticky State Android 
StickyState 라이브러리에서 제공하는 `@StickyState` 어노테이션을 사용하여 UI 컴포넌트의 상태를 저장 / 복원할 수 있습니다.   
이 어노테이션은 클래스 내부에 정의된 속성 중 StickyState 라이브러리에서 지원하는 모든 속성에 적용 가능합니다.  
(지원하는 속성은 [Supported types](#supported-types) 섹션은 참고하세요)


## Configurations
애플리케이션 모듈 `build.gradle.kts`에 다음과 같이 KSP 플러그인(`com.google.devtools.ksp`)을 프로젝트에 적합한 버전을 명시하여 적용합니다.  
(최신 버전은 [이곳](https://mvnrepository.com/artifact/com.google.devtools.ksp/symbol-processing-api) 에서 확인 가능하며 {Kotlin Gradle Plugin Version-KSP Version} 형식입니다)
```kotlin
plugins {
  id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}
```

StickyState 라이브러리의 `core` 모듈과 `processor` 심볼 프로세서 모듈 의존성을 다음과 같이 프로젝트 의존성 리스트에 추가합니다.
```kotlin
dependencies {
  implementation("io.github.myungpyo:stickystate-core:1.0.0")
  ksp("io.github.myungpyo:stickystate-processor:1.0.0")
}
```

## Usage
프로젝트의 액티비티에서는 다음과 같이 `@StickyState`를 사용할 수 있습니다.

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

프로젝트의 프래그먼트에서는 다음과 같이 `@StickyState`를 사용할 수 있습니다.

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

그 외, 액티비티나 프래그먼트 같은 UI 컴포넌트 제약 없이도 클래스 속성의 저장 / 복원 시점을 알 수 있다면 동일한 방식으로 사용 가능합니다.

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