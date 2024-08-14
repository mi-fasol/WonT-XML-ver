package com.example.xml_ver.ui.main.board.register

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haemo_kotlin.network.Resource
import com.example.xml_ver.MainActivity
import com.example.xml_ver.R
import com.example.xml_ver.adapter.ImageAdapter
import com.example.xml_ver.data.retrofit.post.HotPlaceResponsePostModel
import com.example.xml_ver.databinding.FragmentHotPlacePostRegisterBinding
import com.example.xml_ver.databinding.PopupItemBackgroundBinding
import com.example.xml_ver.databinding.PopupLayoutBinding
import com.example.xml_ver.util.setupButtonState
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.HotPlacePostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class HotPlacePostRegisterFragment : Fragment() {
    private var _binding: FragmentHotPlacePostRegisterBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: HotPlacePostViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var imageAdapter: ImageAdapter
    private val selectedImageUris = mutableListOf<Uri>()
    private var photoUri: Uri? = null

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri>? ->
            uris?.let {
                if (it.size <= 4) {
                    selectedImageUris.clear()
                    selectedImageUris.addAll(it)
                    postViewModel.uploadImageList(it)
                    setupRecyclerView()
                } else {
                    Toast.makeText(requireContext(), "최대 4개의 이미지를 선택할 수 있습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && photoUri != null) {
                selectedImageUris.clear()
                photoUri?.let {
                    selectedImageUris.add(it)
                    postViewModel.uploadImageList(listOf(it))
                    setupRecyclerView()
                }
            }
        }

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri>? ->
            uris?.let {
                if (it.size <= 4) {
                    selectedImageUris.clear()
                    selectedImageUris.addAll(it)
                    postViewModel.uploadImageList(it)
                    setupRecyclerView()
                } else {
                    Toast.makeText(requireContext(), "최대 4개의 이미지를 선택할 수 있습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHotPlacePostRegisterBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupEditText()
        setButton()
        setupImagePicker()
        binding.num = 0

        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideFloatingButton()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
            (activity as MainActivity).showBottomNavigation()
            (activity as MainActivity).showFloatingButton()
        }
    }

    private fun setupEditText() {
        binding.contentEnterField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                postViewModel.content.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.descriptionEnterField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                postViewModel.description.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.titleEnterField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                postViewModel.title.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.isValid.collect {
                setupButtonState(it, binding.registerButton, requireContext())
                Log.d("미란, isValid", it.toString())
            }
        }

        binding.registerButton.setOnClickListener {
            tryRegister()
        }
    }

    private fun setupImagePicker() {
        binding.imagePickerBox.setOnClickListener {
            showImagePickerPopup(binding.imagePickerBox)
        }
    }

    private fun setupRecyclerView() {
        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.image.collect {
                if (it.isNotEmpty()) {
                    imageAdapter = ImageAdapter(it.toMutableList()) { position ->

                    }
                    binding.imageRecyclerView.adapter = imageAdapter
                    binding.imageRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.num = it.size
                }
            }
        }

    }

    private fun showImagePickerPopup(anchorView: View) {
        val popupBinding = PopupLayoutBinding.inflate(LayoutInflater.from(anchorView.context))

        val popupWindow = PopupWindow(
            popupBinding.root,
            (anchorView.width * 2.3).toInt(),
            (anchorView.height * 1.6).toInt(),
            true
        )

        val popupItems = listOf(
            Pair("사진 보관함", R.drawable.gallery_icon),
            Pair("사진 촬영", R.drawable.camera_icon),
            Pair("파일 선택", R.drawable.file_icon)
        )

        for ((index, item) in popupItems.withIndex()) {
            val itemViewBinding = PopupItemBackgroundBinding.inflate(
                LayoutInflater.from(anchorView.context),
                popupBinding.popupContainer,
                false
            )
            itemViewBinding.popupItemText.text = item.first
            itemViewBinding.popupItemIcon.setImageResource(item.second)

            itemViewBinding.root.setOnClickListener {
                popupWindow.dismiss()
                when (index) {
                    0 -> openGallery()
                    1 -> checkCameraPermissionAndOpenCamera()
                    2 -> openFilePicker()
                }
            }

            popupBinding.popupContainer.addView(itemViewBinding.root)

            if (index < popupItems.size - 1) {
                val divider = View(anchorView.context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                    ).apply {
                        setMargins(0, 8, 0, 8)
                    }
                    setBackgroundColor(
                        ContextCompat.getColor(
                            anchorView.context,
                            android.R.color.darker_gray
                        )
                    )
                }
                popupBinding.popupContainer.addView(divider)
            }
        }

        popupWindow.showAsDropDown(anchorView, 0, 10)
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun openCamera() {
        val photoFile = File.createTempFile("IMG_", ".jpg", requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photoFile
        )
        cameraLauncher.launch(photoUri)
    }

    private fun openFilePicker() {
        filePickerLauncher.launch(arrayOf("image/*"))
    }

    private fun checkCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(requireContext(), "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun tryRegister() {
        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.registerPost()
            postViewModel.hotPlacePostRegisterState.collect { state ->
                when (state) {
                    is Resource.Success<HotPlaceResponsePostModel> -> {
                        findNavController().popBackStack()
                        (activity as MainActivity).showFloatingButton()
                        (activity as MainActivity).showBottomNavigation()
                    }

                    is Resource.Error<HotPlaceResponsePostModel> -> {
                    }

                    else -> {

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}