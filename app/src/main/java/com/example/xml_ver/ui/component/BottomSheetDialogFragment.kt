package com.example.xml_ver.ui.component

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.xml_ver.R
import com.example.xml_ver.data.retrofit.user.UserResponseModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.xml_ver.databinding.FragmentRoundedBottomSheetBinding
import com.example.xml_ver.ui.main.board.detail.MeetingDetailFragmentDirections
import com.example.xml_ver.ui.main.chat.ChatListFragmentDirections
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.util.userProfileList
import com.example.xml_ver.viewModel.chat.ChatViewModel
import com.tbuonomo.viewpagerdotsindicator.setBackgroundCompat
import kotlinx.coroutines.launch

class RoundedBottomSheetDialogFragment(
    private val user: UserResponseModel,
    private val chatViewModel: ChatViewModel,
    private val onChatButtonClick: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentRoundedBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            setOnShowListener {
                _binding?.root?.let { bottomSheet ->
                    val layoutParams = bottomSheet.layoutParams
                    layoutParams.height = (resources.displayMetrics.heightPixels * 0.6).toInt()
                    bottomSheet.layoutParams = layoutParams
                    bottomSheet.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.rounded_bottom_sheet_background
                    )
                }
            }
        }
    }

    private fun setupButtons() {
        binding.chatButton.setOnClickListener {
            if (user.uId != SharedPreferenceUtil(requireContext()).getUser().uId) {
                viewLifecycleOwner.lifecycleScope.launch {
                    chatViewModel.findChatRoom(user.uId)
                    chatViewModel.chatId.collect { chatId ->
                        if (chatId.isNotBlank()) {
                            onChatButtonClick()
                            dismiss()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoundedBottomSheetBinding.inflate(inflater, container, false).apply {
            this.userImageField.setImageResource(userProfileList[this@RoundedBottomSheetDialogFragment.user.userImage])
            this.nickname = this@RoundedBottomSheetDialogFragment.user.nickname
            this.major = this@RoundedBottomSheetDialogFragment.user.major
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}