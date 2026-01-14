package net.msalt.onboarding

import androidx.annotation.DrawableRes

/**
 * 온보딩 페이지 데이터 모델
 *
 * @param imageRes 페이지에 표시할 이미지 리소스 ID (app 모듈에서 주입)
 * @param title 페이지 제목
 * @param description 페이지 설명
 */
data class OnboardingPage(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String
)
