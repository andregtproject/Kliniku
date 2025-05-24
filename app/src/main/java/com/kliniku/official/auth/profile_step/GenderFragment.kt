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
import com.kliniku.official.databinding.FragmentGenderBinding
import com.kliniku.official.databinding.ItemCardVerticalBinding

class GenderFragment : Fragment() {
    private var _binding: FragmentGenderBinding? = null
    private val binding get() = _binding!!

    private var selectedGender: String? = null

    private lateinit var maleBinding: ItemCardVerticalBinding
    private lateinit var femaleBinding: ItemCardVerticalBinding

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
        _binding = FragmentGenderBinding.inflate(inflater, container, false)

        maleBinding = ItemCardVerticalBinding.bind(binding.itemMale.root)
        femaleBinding = ItemCardVerticalBinding.bind(binding.itemFemale.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGenderCards()
        setupGenderSelection()
        restoreSelection()
    }

    private fun setupGenderCards() {
        maleBinding.apply {
            titleGender.text = getString(R.string.male)
            iconGender.setImageResource(R.drawable.ic_male)
        }
        femaleBinding.apply {
            titleGender.text = getString(R.string.female)
            iconGender.setImageResource(R.drawable.ic_female)
        }
    }

    private fun setupGenderSelection() {
        maleBinding.root.setOnClickListener {
            selectGender("laki-laki", maleBinding)
        }
        femaleBinding.root.setOnClickListener {
            selectGender("perempuan", femaleBinding)
        }
    }

    private fun selectGender(gender: String, selectedBinding: ItemCardVerticalBinding) {
        updateCard(maleBinding, false)
        updateCard(femaleBinding, false)

        updateCard(selectedBinding, true)
        selectedGender = gender
        viewModel.selectedGender = gender
    }

    private fun updateCard(binding: ItemCardVerticalBinding, isSelected: Boolean) {
        val backgroundColor = if (isSelected) {
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        } else {
            ContextCompat.getColor(requireContext(), R.color.white)
        }
        binding.root.setCardBackgroundColor(backgroundColor)

        val textColor = if (isSelected) {
            ContextCompat.getColor(requireContext(), R.color.white)
        } else {
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        }
        binding.titleGender.setTextColor(textColor)
    }

    fun isGenderSelected(): Boolean {
        return selectedGender != null
    }

    private fun restoreSelection() {
        viewModel.selectedGender?.let { gender ->
            when (gender) {
                "laki-laki" -> selectGender(gender, maleBinding)
                "perempuan" -> selectGender(gender, femaleBinding)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
