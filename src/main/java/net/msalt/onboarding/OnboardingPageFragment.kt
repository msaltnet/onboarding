package net.msalt.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.msalt.onboarding.databinding.FragmentOnboardingPageBinding

/**
 * 개별 온보딩 페이지를 표시하는 Fragment
 */
class OnboardingPageFragment : Fragment() {

    private var _binding: FragmentOnboardingPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            val imageRes = bundle.getInt(ARG_IMAGE_RES)
            val title = bundle.getString(ARG_TITLE) ?: ""
            val description = bundle.getString(ARG_DESCRIPTION) ?: ""

            binding.imageOnboarding.setImageResource(imageRes)
            binding.textTitle.text = title
            binding.textDescription.text = description
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_IMAGE_RES = "image_res"
        private const val ARG_TITLE = "title"
        private const val ARG_DESCRIPTION = "description"

        fun newInstance(page: OnboardingPage): OnboardingPageFragment {
            return OnboardingPageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_IMAGE_RES, page.imageRes)
                    putString(ARG_TITLE, page.title)
                    putString(ARG_DESCRIPTION, page.description)
                }
            }
        }
    }
}
