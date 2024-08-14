package com.example.xml_ver.ui.main.board.register

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import com.bumptech.glide.Glide
import com.example.haemo_kotlin.network.Resource
import com.example.xml_ver.MainActivity
import com.example.xml_ver.R
import com.example.xml_ver.data.retrofit.post.ClubPostResponseModel
import com.example.xml_ver.databinding.FragmentClubPostRegisterBinding
import com.example.xml_ver.databinding.PopupItemBackgroundBinding
import com.example.xml_ver.databinding.PopupLayoutBinding
import com.example.xml_ver.util.personList
import com.example.xml_ver.util.setupButtonState
import com.example.xml_ver.util.setupSpinnerClickEvent
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.ClubPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ClubPostRegisterFragment : Fragment() {
    private var _binding: FragmentClubPostRegisterBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: ClubPostViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var personAdapter: ArrayAdapter<String>

    private var photoUri: Uri? = null

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                postViewModel.uploadImage(it)
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                photoUri?.let {
                    postViewModel.uploadImage(it)
                }
            }
        }

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                postViewModel.uploadImage(it)
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
        _binding = FragmentClubPostRegisterBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupSpinners()
        setupAllSpinner()
        setupEditText()
        setButton()
        setupImagePicker()

        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideFloatingButton()
    }

    private fun setupAllSpinner() {
        setupSpinnerClickEvent(binding.personEnterField) { person ->
            postViewModel.person.value = personList.indexOf(person)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
            (activity as MainActivity).showBottomNavigation()
            (activity as MainActivity).showFloatingButton()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupSpinners() {
        personAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, personList)
        personAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.personEnterField.adapter = personAdapter
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

        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.image.collect {
                if (it.isNotBlank()) {
                    binding.imagePickerImage.apply {
                        Glide.with(this@ClubPostRegisterFragment)
                            .load(it)
                            .placeholder(R.drawable.dummy_image)
                            .error(R.drawable.error_image)
                            .into(this)
                        visibility = View.VISIBLE
                    }

                    binding.imagePickerLogo.visibility = View.GONE
                    binding.imagePickerText.visibility = View.GONE
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
            postViewModel.clubPostRegisterState.collect { state ->
                when (state) {
                    is Resource.Success<ClubPostResponseModel> -> {
                        findNavController().popBackStack()
                        (activity as MainActivity).showFloatingButton()
                        (activity as MainActivity).showBottomNavigation()
                    }

                    is Resource.Error<ClubPostResponseModel> -> {
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