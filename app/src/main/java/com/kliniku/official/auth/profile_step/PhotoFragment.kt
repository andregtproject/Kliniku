package com.kliniku.official.auth.profile_step

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kliniku.official.databinding.FragmentPhotoBinding

class PhotoFragment : Fragment() {

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompleteProfileViewModel by activityViewModels()

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data!!.data
                imageUri?.let {
                    binding.imgProfile.setImageURI(it)
                    binding.imgAddIcon.visibility = View.GONE
                    binding.tvUploadText.visibility = View.GONE
                    binding.btnEditPhoto.visibility = View.VISIBLE
                    viewModel.selectedPhotoUri = it
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoUri = viewModel.selectedPhotoUri
        if (photoUri != null) {
            binding.imgProfile.setImageURI(photoUri)
            binding.imgAddIcon.visibility = View.GONE
            binding.tvUploadText.visibility = View.GONE
            binding.btnEditPhoto.visibility = View.VISIBLE
        } else {
            binding.imgAddIcon.visibility = View.VISIBLE
            binding.tvUploadText.visibility = View.VISIBLE
            binding.btnEditPhoto.visibility = View.GONE
        }

        binding.imgAddIcon.setOnClickListener { openImagePicker() }
        binding.btnEditPhoto.setOnClickListener { openImagePicker() }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
