package com.kliniku.official.auth.profile_step

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import com.kliniku.official.R
import com.kliniku.official.databinding.ActivityCompleteProfileBinding
import com.kliniku.official.databinding.CustomToolbarBinding
import com.shuhart.stepview.StepView

class CompleteProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompleteProfileBinding
    private lateinit var toolbarBinding: CustomToolbarBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ProfileStepAdapter

    // Track completed steps
    private val completedSteps = mutableSetOf<Int>()

    // Step titles
    private val stepTitles = listOf(
        R.string.step_choose_role,
        R.string.step_select_gender,
        R.string.step_enter_address,
        R.string.step_upload_photo
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteProfileBinding.inflate(layoutInflater)
        toolbarBinding = CustomToolbarBinding.bind(binding.toolbar.root)
        setContentView(binding.root)

        setupToolbar()
        setupStepView()
        setupViewPager()
        setupButtons()
        updateButtonText(0)

        onBackPressedDispatcher.addCallback(this) {
            val currentItem = viewPager.currentItem

            if (currentItem == 0) {
                // First step, so show exit confirmation dialog
                showExitConfirmationDialog()
            } else {
                // Other Step, back to previous step
                viewPager.currentItem = currentItem - 1
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbarBinding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
        toolbarBinding.toolbarTitle.text = getString(R.string.complete_profile_title)
        toolbarBinding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupStepView() {
        binding.stepView.setStepsNumber(4)
        binding.stepView.state
            .animationType(StepView.ANIMATION_ALL)
            .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
            .typeface(ResourcesCompat.getFont(this, R.font.league_spartan_regular))
            .commit()

        // Set labels for steps
        val stepLabels = listOf(
            getString(R.string.step_role),
            getString(R.string.step_gender),
            getString(R.string.step_address),
            getString(R.string.step_photo)
        )
        binding.stepView.setSteps(stepLabels)
    }

    private fun setupViewPager() {
        adapter = ProfileStepAdapter(this)
        viewPager = binding.viewPager
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.stepView.go(position, true)
                updateStepGuideText(position)
                updateButtonText(position)
            }
        })
    }

    private fun updateStepGuideText(position: Int) {
        binding.tvGuide.text = getString(stepTitles[position])
    }

    private fun setupButtons() {
        binding.layoutButtons.buttonSkip.visibility = View.GONE

        binding.layoutButtons.buttonNext.setOnClickListener {
            val currentPosition = viewPager.currentItem

            if (validateCurrentStep(currentPosition)) {
                completedSteps.add(currentPosition)

                if (currentPosition < adapter.itemCount - 1) {
                    // Move to next step
                    viewPager.currentItem = currentPosition + 1
                } else {
                    // All steps completed
                    completeRegistration()
                }
            }
        }
    }

    private fun validateCurrentStep(position: Int): Boolean {
        return when (position) {
            0 -> { // RoleFragment
                val currentFragment = adapter.getFragment(position) as? RoleFragment
                currentFragment?.isRoleSelected()?.also { isValid ->
                    if (!isValid) {
                        showToast(getString(R.string.please_select_role))
                    }
                } ?: false
            }
            1 -> { // GenderFragment
                val currentFragment = adapter.getFragment(position) as? GenderFragment
                currentFragment?.isGenderSelected()?.also { isValid ->
                    if (!isValid) {
                        showToast(getString(R.string.please_select_gender))
                    }
                } ?: false
            }
            2 -> { // AddressFragment
               true
            }
            3 -> { // PhotoFragment

                true
            }
            else -> false
        }
    }

    private fun updateButtonText(position: Int) {
        binding.layoutButtons.buttonNext.text = if (position == adapter.itemCount - 1) {
            getString(R.string.selesai)
        } else {
            getString(R.string.lanjut)
        }
    }

    private fun completeRegistration() {
        // Check if all steps are completed
        if (completedSteps.size < adapter.itemCount) {
            showToast(getString(R.string.complete_all_steps))
            return
        }

        showToast(getString(R.string.registration_successful))

        // Submit to database

        // Set result and finish activity
        setResult(RESULT_OK)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showExitConfirmationDialog() {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm))
            .setMessage(getString(R.string.exit_confirm_complete_profile))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                finish()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()

        dialog.show()
    }
}