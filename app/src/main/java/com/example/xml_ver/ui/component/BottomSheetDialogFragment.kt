package com.example.xml_ver.ui.component

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.example.xml_ver.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.xml_ver.databinding.FragmentRoundedBottomSheetBinding
import com.example.xml_ver.util.userProfileList
import com.tbuonomo.viewpagerdotsindicator.setBackgroundCompat

class RoundedBottomSheetDialogFragment(
    private val userImage: Int,
    private val nickname: String,
    private val major: String
) : BottomSheetDialogFragment() {

    private var _binding: FragmentRoundedBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            setOnShowListener { dialog ->
                val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.let {
                    val layoutParams = it.layoutParams
                    layoutParams.height = (resources.displayMetrics.heightPixels * 0.6).toInt()
                    it.layoutParams = layoutParams
                    it.background = resources.getDrawable(R.drawable.rounded_bottom_sheet_background, null)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoundedBottomSheetBinding.inflate(inflater, container, false).apply {
            Log.d("미란 바텀시트 이미지", this@RoundedBottomSheetDialogFragment.userImage.toString())
            this.userImageField.setImageResource(userProfileList[this@RoundedBottomSheetDialogFragment.userImage])
            this.nickname = this@RoundedBottomSheetDialogFragment.nickname
            this.major = this@RoundedBottomSheetDialogFragment.major
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}