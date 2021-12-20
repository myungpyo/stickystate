package io.github.myungpyo.stickystate.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.github.myungpyo.ei.databinding.PrimaryFragmentBinding
import io.github.myungpyo.stickystate.StickyState

class PrimaryFragment : Fragment() {

    private val viewModel: NavViewModel by activityViewModels()

    private var _binding: PrimaryFragmentBinding? = null
    private val binding get() = _binding!!

    @StickyState
    var userName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PrimaryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PrimaryFragmentStateBinding.restore(this, savedInstanceState)
        binding.doneButton.setOnClickListener {
            userName = binding.nameInputView.text.toString()
            viewModel.navTo(
                NavEvent.Secondary(userName = userName)
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        PrimaryFragmentStateBinding.save(this, outState)
        super.onSaveInstanceState(outState)
    }

    companion object {
        fun create(): PrimaryFragment {
            return PrimaryFragment()
        }
    }
}