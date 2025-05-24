package com.kliniku.official.auth.profile_step

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var viewModel: CompleteProfileViewModel

    private val completedSteps = mutableSetOf<Int>()

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

        viewModel = ViewModelProvider(this)[CompleteProfileViewModel::class.java]

        setupToolbar()
        setupStepView()
        setupViewPager()
        setupButtons()
        updateButtonText(0)

        onBackPressedDispatcher.addCallback(this) {
            val currentItem = viewPager.currentItem
            if (currentItem == 0) {
                showExitConfirmationDialog()
            } else {
                viewPager.currentItem = currentItem - 1
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbarBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
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

        // Set offscreen page limit untuk mempertahankan fragment
        viewPager.offscreenPageLimit = 1

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.stepView.go(position, true)
                updateStepGuideText(position)
                updateButtonText(position)

                // Notify fragment tentang visibility change
                notifyFragmentVisibilityChanged(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                // Ketika scroll selesai, pastikan fragment address refresh
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    val currentPosition = viewPager.currentItem
                    if (currentPosition == 2) { // Address fragment position
                        binding.root.postDelayed({
                            try {
                                val fragment = adapter.getFragment(currentPosition)
                                if (fragment is AddressFragment) {
                                    fragment.refreshMap()
                                }
                            } catch (e: Exception) {
                                // Ignore error
                            }
                        }, 100)
                    }
                }
            }
        })
    }

    private fun notifyFragmentVisibilityChanged(position: Int) {
        // Delay untuk memastikan fragment dan view sudah ready
        binding.root.postDelayed({
            try {
                val fragment = adapter.getFragment(position)
                if (fragment is AddressFragment) {
                    // Trigger refresh untuk AddressFragment dengan multiple delays
                    fragment.onHiddenChanged(false)

                    // Refresh dengan beberapa attempt
                    binding.root.postDelayed({
                        fragment.refreshMap()
                    }, 150)

                    binding.root.postDelayed({
                        fragment.refreshMap()
                    }, 500)
                }
            } catch (e: Exception) {
                // Ignore error
            }
        }, 100)
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
                    viewPager.currentItem = currentPosition + 1
                } else {
                    completeRegistration()
                }
            }
        }
    }

    private fun validateCurrentStep(position: Int): Boolean {
        return when (position) {
            0 -> {
                val currentFragment = adapter.getFragment(position) as? RoleFragment
                currentFragment?.isRoleSelected()?.also {
                    if (!it) showToast(getString(R.string.please_select_role))
                } ?: false
            }
            1 -> {
                val currentFragment = adapter.getFragment(position) as? GenderFragment
                currentFragment?.isGenderSelected()?.also {
                    if (!it) showToast(getString(R.string.please_select_gender))
                } ?: false
            }
            2 -> {
                val currentFragment = adapter.getFragment(position) as? AddressFragment
                currentFragment?.isAddressSelected()?.also {
                    if (!it) showToast(getString(R.string.please_enter_address))
                } ?: false
            }
            3 -> {
                if (viewModel.selectedPhotoUri == null) {
                    showToast(getString(R.string.please_upload_photo))
                    false
                } else true
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
        if (completedSteps.size < adapter.itemCount) {
            showToast(getString(R.string.complete_all_steps))
            return
        }

        // planning for integrate to database
        val photoUri = viewModel.selectedPhotoUri
        val addressText = viewModel.selectedAddress
        val locationPoint = viewModel.selectedGeoPoint
        val role = viewModel.selectedRole
        val gender = viewModel.selectedGender

        // TODO: Submit ke database atau Firebase

        showToast(getString(R.string.registration_successful))

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
            .setPositiveButton(getString(R.string.yes)) { _, _ -> finish() }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
        dialog.show()
    }
}