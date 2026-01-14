# Onboarding Module

재사용 가능한 안드로이드 온보딩 라이브러리 모듈입니다. 독립적인 Gradle 모듈로 구성되어 있어 다른 프로젝트에서도 쉽게 사용할 수 있습니다.

## 특징

- **독립된 Gradle 모듈**: app 모듈과 별도로 빌드되는 Android 라이브러리
- **이미지와 텍스트 주입 방식**: 모듈에는 UI 로직만 포함하고, 콘텐츠는 app 모듈에서 주입
- **ViewPager2 기반**: 스와이프 가능한 온보딩 페이지
- **완전 커스터마이징 가능**: 원하는 개수의 페이지, 이미지, 텍스트 사용 가능
- **자동 상태 관리**: SharedPreferences를 통한 온보딩 완료 여부 관리

## 모듈 구조

```
onboarding/
├── build.gradle.kts              # 모듈 빌드 설정
├── proguard-rules.pro
├── consumer-rules.pro
└── src/main/
    ├── AndroidManifest.xml       # Activity 선언
    ├── java/net/msalt/onboarding/
    │   ├── OnboardingPage.kt             # 데이터 모델
    │   ├── OnboardingPageFragment.kt     # 페이지 Fragment
    │   ├── OnboardingPagerAdapter.kt     # ViewPager2 어댑터
    │   ├── OnboardingActivity.kt         # 메인 Activity
    │   └── OnboardingManager.kt          # 상태 관리
    └── res/
        ├── layout/
        │   ├── activity_onboarding.xml
        │   └── fragment_onboarding_page.xml
        ├── drawable/
        │   ├── indicator_active.xml
        │   └── indicator_inactive.xml
        └── values/
            ├── strings.xml
            └── dimens.xml
```

## 프로젝트에 추가하기

### 1. settings.gradle.kts에 모듈 추가

```kotlin
include(":app")
include(":onboarding")
```

### 2. app 모듈의 build.gradle.kts에 의존성 추가

```kotlin
dependencies {
    implementation(project(":onboarding"))
    // ... 다른 의존성들
}
```

### 3. 온보딩 이미지를 app 모듈에 추가

`app/src/main/res/drawable/` 폴더에 온보딩 이미지를 추가합니다:
- `onboarding_1.xml` (또는 .png, .jpg 등)
- `onboarding_2.xml`
- `onboarding_3.xml`
- `onboarding_4.xml`

### 4. MainActivity에서 온보딩 호출

```kotlin
import net.msalt.onboarding.OnboardingActivity
import net.msalt.onboarding.OnboardingManager
import net.msalt.onboarding.OnboardingPage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 온보딩 체크
        if (!OnboardingManager.isOnboardingCompleted(this)) {
            val pages = createOnboardingPages()
            OnboardingActivity.start(this, pages)
        }

        // 나머지 초기화 코드
        setContentView(R.layout.activity_main)
    }

    private fun createOnboardingPages(): List<OnboardingPage> {
        return listOf(
            OnboardingPage(
                imageRes = R.drawable.onboarding_1,
                title = "첫 번째 페이지",
                description = "첫 번째 페이지 설명"
            ),
            OnboardingPage(
                imageRes = R.drawable.onboarding_2,
                title = "두 번째 페이지",
                description = "두 번째 페이지 설명"
            ),
            OnboardingPage(
                imageRes = R.drawable.onboarding_3,
                title = "세 번째 페이지",
                description = "세 번째 페이지 설명"
            ),
            OnboardingPage(
                imageRes = R.drawable.onboarding_4,
                title = "네 번째 페이지",
                description = "네 번째 페이지 설명"
            )
        )
    }
}
```

## 사용법

### 기본 사용

```kotlin
// 온보딩 완료 여부 확인
if (!OnboardingManager.isOnboardingCompleted(context)) {
    val pages = listOf(
        OnboardingPage(R.drawable.img1, "제목 1", "설명 1"),
        OnboardingPage(R.drawable.img2, "제목 2", "설명 2")
    )
    OnboardingActivity.start(context, pages)
}
```

### 온보딩 상태 관리

```kotlin
// 온보딩 완료 여부 확인
val isCompleted = OnboardingManager.isOnboardingCompleted(context)

// 온보딩 완료 상태 설정
OnboardingManager.setOnboardingCompleted(context, true)

// 온보딩 상태 초기화 (테스트용)
OnboardingManager.resetOnboarding(context)
```

### 페이지 수 커스터마이징

원하는 만큼 페이지를 추가할 수 있습니다:

```kotlin
private fun createOnboardingPages(): List<OnboardingPage> {
    return listOf(
        OnboardingPage(R.drawable.onboarding_1, "페이지 1", "설명 1"),
        OnboardingPage(R.drawable.onboarding_2, "페이지 2", "설명 2"),
        OnboardingPage(R.drawable.onboarding_3, "페이지 3", "설명 3"),
        // 페이지를 더 추가하거나 제거 가능
    )
}
```

## 다른 프로젝트에서 재사용하기

### 방법 1: 모듈 폴더 복사

1. `onboarding` 폴더 전체를 새 프로젝트의 root 디렉토리로 복사
2. `settings.gradle.kts`에 `include(":onboarding")` 추가
3. app 모듈의 `build.gradle.kts`에 의존성 추가
4. 온보딩 이미지를 app 모듈에 추가
5. MainActivity에서 온보딩 호출

### 방법 2: AAR로 배포

모듈을 빌드하여 AAR 파일로 만들고 다른 프로젝트에서 사용:

```bash
./gradlew :onboarding:assembleRelease
```

생성된 AAR 파일 위치:
```
onboarding/build/outputs/aar/onboarding-release.aar
```

## 커스터마이징

### 버튼 텍스트 변경

`onboarding/src/main/res/values/strings.xml` 파일을 수정:

```xml
<string name="onboarding_skip">건너뛰기</string>
<string name="onboarding_next">다음</string>
<string name="onboarding_get_started">시작하기</string>
```

### 인디케이터 스타일 변경

`onboarding/src/main/res/drawable/` 폴더의 파일들을 수정:
- `indicator_active.xml`: 현재 페이지 인디케이터
- `indicator_inactive.xml`: 다른 페이지 인디케이터

### 레이아웃 커스터마이징

`onboarding/src/main/res/layout/` 폴더의 레이아웃 파일들을 수정:
- `activity_onboarding.xml`: 메인 레이아웃
- `fragment_onboarding_page.xml`: 개별 페이지 레이아웃

## 의존성

모듈이 사용하는 주요 라이브러리:
- AndroidX Core KTX
- AndroidX AppCompat
- Material Components
- AndroidX Fragment KTX
- ViewPager2

## 라이선스

이 모듈은 자유롭게 사용 및 수정 가능합니다.

## 문의 및 기여

이슈나 개선 사항이 있으면 프로젝트 저장소에 제보해주세요.
