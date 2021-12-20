package io.github.myungpyo.stickystate.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.github.myungpyo.ei.R
import io.github.myungpyo.ei.databinding.SecondaryFragmentBinding
import io.github.myungpyo.stickystate.StickyState

class SecondaryFragment : Fragment() {

    private val viewModel: NavViewModel by activityViewModels()

    private var _binding: SecondaryFragmentBinding? = null
    private val binding get() = _binding!!

    @StickyState
    var testValue: Int = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SecondaryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SecondaryFragmentStateBinding.restore(this, savedInstanceState)

        binding.welcomeMessageView.text = getString(R.string.welcome_message, getUserName(arguments))

        binding.backButton.setOnClickListener {
            viewModel.navBack()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        SecondaryFragmentStateBinding.save(this, outState)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val ARG_USER_NAME = "argUserName"

        fun create(userName: String): SecondaryFragment {
            return SecondaryFragment().apply {
                arguments = bundleOf(
                    ARG_USER_NAME to userName
                )
            }
        }

        fun getUserName(args: Bundle?): String {
            return args?.getString(ARG_USER_NAME) ?: ""
        }
    }
}