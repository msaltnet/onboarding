package net.msalt.onboarding

import android.content.Context
import android.content.SharedPreferences

/**
 * 온보딩 완료 상태를 관리하는 클래스
 */
object OnboardingManager {

    private const val PREF_NAME = "onboarding_prefs"
    private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 온보딩 완료 여부를 확인합니다.
     */
    fun isOnboardingCompleted(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    /**
     * 온보딩 완료 상태를 설정합니다.
     */
    fun setOnboardingCompleted(context: Context, completed: Boolean) {
        getPreferences(context).edit()
            .putBoolean(KEY_ONBOARDING_COMPLETED, completed)
            .apply()
    }

    /**
     * 온보딩 상태를 초기화합니다 (개발/테스트 용도).
     */
    fun resetOnboarding(context: Context) {
        setOnboardingCompleted(context, false)
    }
}
