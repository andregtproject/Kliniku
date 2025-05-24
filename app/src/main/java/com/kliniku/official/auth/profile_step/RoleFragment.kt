package com.kliniku.official.auth.profile_step

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kliniku.official.R
import com.kliniku.official.databinding.FragmentRoleBinding
import com.kliniku.official.databinding.ItemCardHorizontalBinding

class RoleFragment : Fragment() {
    private var _binding: FragmentRoleBinding? = null
    private val binding get() = _binding!!

    private var selectedRole: String? = null

    private lateinit var adminBinding: ItemCardHorizontalBinding
    private lateinit var patientBinding: ItemCardHorizontalBinding
    private lateinit var doctorBinding: ItemCardHorizontalBinding

    private lateinit var viewModel: CompleteProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity())[CompleteProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoleBinding.inflate(inflater, container, false)

        adminBinding = ItemCardHorizontalBinding.bind(binding.itemAdmin.root)
        patientBinding = ItemCardHorizontalBinding.bind(binding.itemPasien.root)
        doctorBinding = ItemCardHorizontalBinding.bind(binding.itemDokter.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRoleCards()
        setupRoleSelection()
        restoreSelection()
    }

    private fun setupRoleCards() {
        adminBinding.apply {
            titlePasien.text = getString(R.string.role_admin_title)
            descPasien.text = getString(R.string.role_admin_desc)
            iconPasien.setImageResource(R.drawable.ic_clinic)
        }

        patientBinding.apply {
            titlePasien.text = getString(R.string.role_patient_title)
            descPasien.text = getString(R.string.role_patient_desc)
            iconPasien.setImageResource(R.drawable.ic_patient)
        }

        doctorBinding.apply {
            titlePasien.text = getString(R.string.role_doctor_title)
            descPasien.text = getString(R.string.role_doctor_desc)
            iconPasien.setImageResource(R.drawable.ic_doctor)
        }
    }

    private fun setupRoleSelection() {
        adminBinding.root.setOnClickListener {
            selectRole("admin", adminBinding)
        }
        patientBinding.root.setOnClickListener {
            selectRole("pasien", patientBinding)
        }
        doctorBinding.root.setOnClickListener {
            selectRole("dokter", doctorBinding)
        }
    }

    private fun selectRole(role: String, selectedBinding: ItemCardHorizontalBinding) {
        updateCard(adminBinding, false)
        updateCard(patientBinding, false)
        updateCard(doctorBinding, false)

        updateCard(selectedBinding, true)
        selectedRole = role
        viewModel.selectedRole = role  // Simpan ke ViewModel
    }

    private fun updateCard(binding: ItemCardHorizontalBinding, isSelected: Boolean) {
        val backgroundColor = if (isSelected) {
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        } else {
            ContextCompat.getColor(requireContext(), R.color.white)
        }
        binding.root.setCardBackgroundColor(backgroundColor)

        val textTitleColor = if (isSelected) {
            ContextCompat.getColor(requireContext(), R.color.white)
        } else {
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        }
        binding.titlePasien.setTextColor(textTitleColor)
        binding.iconPasien.setColorFilter(textTitleColor)
    }

    fun isRoleSelected(): Boolean {
        return selectedRole != null
    }

    private fun restoreSelection() {
        viewModel.selectedRole?.let { role ->
            when (role) {
                "admin" -> selectRole(role, adminBinding)
                "pasien" -> selectRole(role, patientBinding)
                "dokter" -> selectRole(role, doctorBinding)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}