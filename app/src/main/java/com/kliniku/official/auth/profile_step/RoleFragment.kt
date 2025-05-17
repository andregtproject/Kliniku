package com.kliniku.official.auth.profile_step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
        binding.itemAdmin.root.setOnClickListener {
            selectRole("admin", binding.itemAdmin.root)
        }

        binding.itemPasien.root.setOnClickListener {
            selectRole("pasien", binding.itemPasien.root)
        }

        binding.itemDokter.root.setOnClickListener {
            selectRole("dokter", binding.itemDokter.root)
        }

        selectedRole?.let { role ->
            when (role) {
                "admin" -> binding.itemAdmin.root.isSelected = true
                "pasien" -> binding.itemPasien.root.isSelected = true
                "dokter" -> binding.itemDokter.root.isSelected = true
            }
        }
    }

    private fun selectRole(role: String, selectedView: View) {
        binding.itemAdmin.root.isSelected = false
        binding.itemPasien.root.isSelected = false
        binding.itemDokter.root.isSelected = false

        updateCard(binding.itemAdmin.root, false)
        updateCard(binding.itemPasien.root, false)
        updateCard(binding.itemDokter.root, false)

        selectedView.isSelected = true
        updateCard(selectedView, true)
        selectedRole = role
    }

    private fun updateCard(cardView: View, isSelected: Boolean) {
        val backgroundColor = if (isSelected) {
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        } else {
            ContextCompat.getColor(requireContext(), R.color.white)
        }
        cardView.setBackgroundColor(backgroundColor)

        val textTitleColor = if (isSelected) {
            ContextCompat.getColor(requireContext(), R.color.white)
        } else {
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        }
        (cardView as ItemCardHorizontalBinding).titlePasien.setTextColor(textTitleColor)
    }

    fun isRoleSelected(): Boolean {
        return selectedRole != null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}