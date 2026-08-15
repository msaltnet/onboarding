package net.msalt.onboarding

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import net.msalt.onboarding.databinding.ActivityOnboardingBinding

/**
 * 온보딩 화면을 표시하는 Activity
 *
 * 사용법:
 * ```kotlin
 * val pages = listOf(
 *     OnboardingPage(R.drawable.onboarding_1, "제목 1", "설명 1"),
 *     OnboardingPage(R.drawable.onboarding_2, "제목 2", "설명 2")
 * )
 * OnboardingActivity.start(context, pages)
 * ```
 */
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var pages: List<OnboardingPage>
    private val indicators = mutableListOf<View>()
    private var detailUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyResponsiveOrientation()

        pages = getOnboardingPages()
        detailUrl = intent.getStringExtra(EXTRA_DETAIL_URL)

        setupViewPager()
        setupIndicators()
        setupButtons()
    }

    private fun applyResponsiveOrientation() {
        requestedOrientation =
            if (resources.configuration.screenWidthDp < 600) {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
    }

    private fun getOnboardingPages(): List<OnboardingPage> {
        val imageResList = intent.getIntArrayExtra(EXTRA_IMAGE_RES) ?: intArrayOf()
        val titleList = intent.getStringArrayExtra(EXTRA_TITLES) ?: arrayOf()
        val descriptionList = intent.getStringArrayExtra(EXTRA_DESCRIPTIONS) ?: arrayOf()

        return imageResList.indices.map { index ->
            OnboardingPage(
                imageRes = imageResList[index],
                title = titleList.getOrNull(index) ?: "",
                description = descriptionList.getOrNull(index) ?: ""
            )
        }
    }

    private fun setupViewPager() {
        val adapter = OnboardingPagerAdapter(this, pages)
        binding.viewPagerOnboarding.adapter = adapter

        binding.viewPagerOnboarding.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
                updateButtons(position)
            }
        })
    }

    private fun setupIndicators() {
        val indicatorSize = resources.getDimensionPixelSize(R.dimen.indicator_size)
        val indicatorMargin = resources.getDimensionPixelSize(R.dimen.indicator_margin)

        indicators.clear()
        binding.layoutIndicator.removeAllViews()

        for (i in pages.indices) {
            val indicator = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(indicatorSize, indicatorSize).apply {
                    setMargins(indicatorMargin, 0, indicatorMargin, 0)
                }
                setBackgroundResource(R.drawable.indicator_inactive)
            }
            indicators.add(indicator)
            binding.layoutIndicator.addView(indicator)
        }

        if (indicators.isNotEmpty()) {
            indicators[0].setBackgroundResource(R.drawable.indicator_active)
        }
    }

    private fun updateIndicators(position: Int) {
        indicators.forEachIndexed { index, view ->
            view.setBackgroundResource(
                if (index == position) R.drawable.indicator_active
                else R.drawable.indicator_inactive
            )
        }
    }

    private fun updateButtons(position: Int) {
        val isLastPage = position == pages.size - 1

        binding.layoutNavigation.visibility = if (isLastPage) View.GONE else View.VISIBLE
        binding.buttonGetStarted.visibility = if (isLastPage) View.VISIBLE else View.GONE
        binding.buttonWebsiteDetails.visibility =
            if (isLastPage && !detailUrl.isNullOrBlank()) View.VISIBLE else View.GONE
    }

    private fun setupButtons() {
        binding.buttonNext.setOnClickListener {
            val currentItem = binding.viewPagerOnboarding.currentItem
            if (currentItem < pages.size - 1) {
                binding.viewPagerOnboarding.currentItem = currentItem + 1
            }
        }

        binding.buttonSkip.setOnClickListener {
            finishOnboarding()
        }

        binding.buttonGetStarted.setOnClickListener {
            finishOnboarding()
        }

        binding.buttonWebsiteDetails.setOnClickListener {
            detailUrl?.takeIf(String::isNotBlank)?.let { url ->
                runCatching {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
        }
    }

    private fun finishOnboarding() {
        OnboardingManager.setOnboardingCompleted(this, true)
        finish()
    }

    companion object {
        private const val EXTRA_IMAGE_RES = "extra_image_res"
        private const val EXTRA_TITLES = "extra_titles"
        private const val EXTRA_DESCRIPTIONS = "extra_descriptions"
        private const val EXTRA_DETAIL_URL = "extra_detail_url"

        /**
         * OnboardingActivity를 시작합니다.
         *
         * @param context Context
         * @param pages 온보딩 페이지 리스트 (이미지와 텍스트를 app 모듈에서 주입)
         */
        fun start(context: Context, pages: List<OnboardingPage>, detailUrl: String? = null) {
            val intent = Intent(context, OnboardingActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_RES, pages.map { it.imageRes }.toIntArray())
                putExtra(EXTRA_TITLES, pages.map { it.title }.toTypedArray())
                putExtra(EXTRA_DESCRIPTIONS, pages.map { it.description }.toTypedArray())
                putExtra(EXTRA_DETAIL_URL, detailUrl)
            }
            context.startActivity(intent)
        }
    }
}
