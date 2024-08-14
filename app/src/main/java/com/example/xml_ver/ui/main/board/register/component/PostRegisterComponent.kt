///*
//package com.example.xml_ver.ui.main.board.register.component
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.LinearLayout
//import android.widget.PopupWindow
//import android.widget.Toast
//import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
//import androidx.core.content.ContextCompat
//import androidx.core.content.FileProvider
//import com.example.xml_ver.R
//import com.example.xml_ver.databinding.PopupItemBackgroundBinding
//import com.example.xml_ver.databinding.PopupLayoutBinding
//import java.io.File
//
//private fun openGallery(context: Context) {
//    galleryLauncher.launch("image/*")
//}
//
//private fun openCamera(context: Context) {
//    val photoFile = File.createTempFile("IMG_", ".jpg", requireContext().cacheDir).apply {
//        createNewFile()
//        deleteOnExit()
//    }
//    photoUri = FileProvider.getUriForFile(
//        requireContext(),
//        "${context.packageName}.fileprovider",
//        photoFile
//    )
//    cameraLauncher.launch(photoUri)
//}
//
//private fun openFilePicker() {
//    filePickerLauncher.launch(arrayOf("image/*"))
//}
//
//private fun checkCameraPermissionAndOpenCamera(context: Context) {
//    when {
//        ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.CAMERA
//        ) == PackageManager.PERMISSION_GRANTED -> {
//            openCamera()
//        }
//
//        shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
//            Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
//        }
//
//        else -> {
//            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
//        }
//    }
//}
//
//private fun showImagePickerPopup(anchorView: View, context: Context) {
//    val popupBinding = PopupLayoutBinding.inflate(LayoutInflater.from(anchorView.context))
//
//    val popupWindow = PopupWindow(
//        popupBinding.root,
//        (anchorView.width * 2.3).toInt(),
//        (anchorView.height * 1.6).toInt(),
//        true
//    )
//
//    val popupItems = listOf(
//        Pair("사진 보관함", R.drawable.gallery_icon),
//        Pair("사진 촬영", R.drawable.camera_icon),
//        Pair("파일 선택", R.drawable.file_icon)
//    )
//
//    for ((index, item) in popupItems.withIndex()) {
//        val itemViewBinding = PopupItemBackgroundBinding.inflate(
//            LayoutInflater.from(anchorView.context),
//            popupBinding.popupContainer,
//            false
//        )
//        itemViewBinding.popupItemText.text = item.first
//        itemViewBinding.popupItemIcon.setImageResource(item.second)
//
//        itemViewBinding.root.setOnClickListener {
//            popupWindow.dismiss()
//            when (index) {
//                0 -> openGallery(context)
//                1 -> checkCameraPermissionAndOpenCamera(context)
//                2 -> openFilePicker(context)
//            }
//        }
//
//        popupBinding.popupContainer.addView(itemViewBinding.root)
//
//        if (index < popupItems.size - 1) {
//            val divider = View(anchorView.context).apply {
//                layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    1
//                ).apply {
//                    setMargins(0, 8, 0, 8)
//                }
//                setBackgroundColor(
//                    ContextCompat.getColor(
//                        anchorView.context,
//                        android.R.color.darker_gray
//                    )
//                )
//            }
//            popupBinding.popupContainer.addView(divider)
//        }
//    }
//
//    popupWindow.showAsDropDown(anchorView, 0, 10)
//}*/
