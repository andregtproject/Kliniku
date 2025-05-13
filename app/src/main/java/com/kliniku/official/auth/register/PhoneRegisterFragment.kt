package com.kliniku.official.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kliniku.official.databinding.FragmentPhoneRegisterBinding

class PhoneRegisterFragment : Fragment() {

    private var _binding: FragmentPhoneRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        binding.btnRegister.setOnClickListener {
            // Handle phone registration
            val fullName = binding.etFullname.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val password = binding.etPassword.text.toString()

            // Implement registration
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PhoneRegisterFragment()
    }
}