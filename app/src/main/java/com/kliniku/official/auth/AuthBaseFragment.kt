package com.kliniku.official.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kliniku.official.R
import com.kliniku.official.auth.profile_step.CompleteProfileActivity
import com.kliniku.official.auth.util.ValidatorUtil
import com.kliniku.official.databinding.FragmentAuthBaseBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AuthBaseFragment : Fragment() {

    private var _binding: FragmentAuthBaseBinding? = null
    private val binding get() = _binding!!

    private var authMode: AuthMode = AuthMode.REGISTER_EMAIL
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var selectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            authMode = AuthMode.valueOf(it.getString(ARG_AUTH_MODE, AuthMode.REGISTER_EMAIL.name))
        }

        setupViews()
        setupPasswordToggles()
        setupValidators()
        setupListeners()
    }

    private fun setupViews() {
        with(binding) {
            spaceBeforeButton.visibility = View.GONE
            spaceForgotPassword.visibility = View.GONE

            when (authMode) {
                AuthMode.REGISTER_EMAIL -> {
                    setupRegistrationViews(show = true)
                    setupEmailViews(show = true)
                    setupGoogleSection(
                        show = true,
                        text = getString(R.string.register_with_google)
                    )
                    setupPromptSection(
                        promptText = getString(R.string.already_have_account),
                        actionText = getString(R.string.login)
                    )
                    btnAction.text = getString(R.string.register)
                    spaceBeforeButton.visibility = View.VISIBLE
                    btnAction.setConstraintTopToBottomOf(spaceBeforeButton)
                }

                AuthMode.REGISTER_PHONE -> {
                    setupRegistrationViews(show = true)
                    setupPhoneViews(show = true)
                    setupGoogleSection(show = false)
                    setupPromptSection(
                        promptText = getString(R.string.already_have_account),
                        actionText = getString(R.string.login)
                    )
                    btnAction.text = getString(R.string.register)
                    spaceBeforeButton.visibility = View.VISIBLE
                    btnAction.setConstraintTopToBottomOf(spaceBeforeButton)
                }

                AuthMode.LOGIN_EMAIL -> {
                    setupRegistrationViews(show = false)
                    setupEmailViews(show = true)
                    setupGoogleSection(
                        show = true,
                        text = getString(R.string.login_with_google)
                    )
                    setupPromptSection(
                        promptText = getString(R.string.dont_have_account),
                        actionText = getString(R.string.register)
                    )
                    btnAction.text = getString(R.string.login)
                    spaceForgotPassword.visibility = View.VISIBLE
                    btnAction.setConstraintTopToBottomOf(spaceForgotPassword)
                }

                AuthMode.LOGIN_PHONE -> {
                    setupRegistrationViews(show = false)
                    setupPhoneViews(show = true)
                    setupGoogleSection(show = false)
                    setupPromptSection(
                        promptText = getString(R.string.dont_have_account),
                        actionText = getString(R.string.register)
                    )
                    btnAction.text = getString(R.string.login)
                    spaceForgotPassword.visibility = View.VISIBLE
                    btnAction.setConstraintTopToBottomOf(spaceForgotPassword)
                }
            }
        }
    }

    private fun showLoading() {
        (activity as? AuthActivity)?.showLoading()
    }

    private fun hideLoading() {
        (activity as? AuthActivity)?.hideLoading()
    }

    private fun setupRegistrationViews(show: Boolean) {
        with(binding) {
            tvFullname.visibility = if (show) View.VISIBLE else View.GONE
            etFullname.visibility = if (show) View.VISIBLE else View.GONE
            tvErrorFullname.visibility = View.GONE

            tvBirthdate.visibility = if (show) View.VISIBLE else View.GONE
            birthdateContainer.visibility = if (show) View.VISIBLE else View.GONE
            tvErrorBirthdate.visibility = View.GONE

            tvConfirmPassword.visibility = if (show) View.VISIBLE else View.GONE
            confirmPasswordContainer.visibility = if (show) View.VISIBLE else View.GONE
            tvErrorConfirmPassword.visibility = View.GONE

            tvForgotPassword.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    private fun setupEmailViews(show: Boolean) {
        with(binding) {
            tvEmail.visibility = if (show) View.VISIBLE else View.GONE
            etEmail.visibility = if (show) View.VISIBLE else View.GONE
            tvErrorEmail.visibility = View.GONE

            tvPhoneNumber.visibility = if (show) View.GONE else View.VISIBLE
            etPhoneNumber.visibility = if (show) View.GONE else View.VISIBLE
            tvErrorPhone.visibility = View.GONE

            if (show) {
                tvPassword.setConstraintTopToBottomOf(tvErrorEmail)
            } else {
                tvPassword.setConstraintTopToBottomOf(tvErrorPhone)
            }
        }
    }

    private fun setupPhoneViews(show: Boolean) {
        with(binding) {
            tvPhoneNumber.visibility = if (show) View.VISIBLE else View.GONE
            etPhoneNumber.visibility = if (show) View.VISIBLE else View.GONE
            tvErrorPhone.visibility = View.GONE

            tvEmail.visibility = if (show) View.GONE else View.VISIBLE
            etEmail.visibility = if (show) View.GONE else View.VISIBLE
            tvErrorEmail.visibility = View.GONE

            if (show) {
                tvPassword.setConstraintTopToBottomOf(tvErrorPhone)
            } else {
                tvPassword.setConstraintTopToBottomOf(tvErrorEmail)
            }
        }
    }

    private fun setupGoogleSection(show: Boolean, text: String? = null) {
        with(binding) {
            separatorLayout.visibility = if (show) View.VISIBLE else View.GONE
            btnGoogleContainer.visibility = if (show) View.VISIBLE else View.GONE
            text?.let {
                (btnGoogleContainer.getChildAt(1) as TextView).text = it
            }
        }
    }

    private fun setupPromptSection(promptText: String, actionText: String) {
        with(binding) {
            tvPromptText.text = promptText
            tvActionText.text = actionText
        }
    }

    private fun setupPasswordToggles() {
        binding.tglPassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(binding.etPassword, isPasswordVisible)
            binding.tglPassword.setImageResource(
                if (isPasswordVisible) R.drawable.ic_visibility_on
                else R.drawable.ic_visibility_off
            )
        }

        binding.tglConfirmPassword.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            togglePasswordVisibility(binding.etConfirmPassword, isConfirmPasswordVisible)
            binding.tglConfirmPassword.setImageResource(
                if (isConfirmPasswordVisible) R.drawable.ic_visibility_on
                else R.drawable.ic_visibility_off
            )
        }
    }

    private fun togglePasswordVisibility(editText: EditText, isVisible: Boolean) {
        editText.transformationMethod = if (isVisible) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
        editText.setSelection(editText.text.length)
    }

    private fun setupValidators() {
        with(binding) {
            etFullname.addTextChangedListener(createTextWatcher { validateFullName() })
            etEmail.addTextChangedListener(createTextWatcher { validateEmail() })
            etPhoneNumber.addTextChangedListener(createTextWatcher { validatePhone() })
            etPassword.addTextChangedListener(createTextWatcher {
                validatePassword()
                if (etConfirmPassword.text.isNotEmpty()) {
                    validateConfirmPassword()
                }
            })
            etConfirmPassword.addTextChangedListener(createTextWatcher { validateConfirmPassword() })
        }
    }

    private fun createTextWatcher(validationFn: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validationFn()
            }
        }
    }

    private fun validateFullName(): Boolean {
        if (binding.etFullname.visibility != View.VISIBLE) return true
        val fullName = binding.etFullname.text.toString()
        val validationResult = ValidatorUtil.validateName(requireContext(), fullName)
        return updateFieldStatus(
            editText = binding.etFullname,
            errorTextView = binding.tvErrorFullname,
            validationResult = validationResult
        )
    }

    private fun validateEmail(): Boolean {
        if (binding.etEmail.visibility != View.VISIBLE) return true
        val email = binding.etEmail.text.toString()
        val validationResult = ValidatorUtil.validateEmail(requireContext(), email)
        return updateFieldStatus(
            editText = binding.etEmail,
            errorTextView = binding.tvErrorEmail,
            validationResult = validationResult
        )
    }

    private fun validatePhone(): Boolean {
        if (binding.etPhoneNumber.visibility != View.VISIBLE) return true
        val phone = binding.etPhoneNumber.text.toString()
        val validationResult = ValidatorUtil.validatePhone(requireContext(), phone)
        return updateFieldStatus(
            editText = binding.etPhoneNumber,
            errorTextView = binding.tvErrorPhone,
            validationResult = validationResult
        )
    }

    private fun validateBirthDate(): Boolean {
        if (binding.birthdateContainer.visibility != View.VISIBLE) return true
        val birthdate = binding.tvBirthdateValue.text.toString()
        val validationResult =
            ValidatorUtil.validateBirthdate(requireContext(), birthdate, selectedDate)
        return updateFieldStatus(
            container = binding.birthdateContainer,
            errorTextView = binding.tvErrorBirthdate,
            validationResult = validationResult
        )
    }

    private fun validatePassword(): Boolean {
        val password = binding.etPassword.text.toString()
        val validationResult = ValidatorUtil.validatePassword(requireContext(), password)
        return updateFieldStatus(
            container = binding.passwordContainer,
            errorTextView = binding.tvErrorPassword,
            validationResult = validationResult
        )
    }

    private fun validateConfirmPassword(): Boolean {
        if (binding.confirmPasswordContainer.visibility != View.VISIBLE) return true
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        val validationResult =
            ValidatorUtil.validateConfirmPassword(requireContext(), password, confirmPassword)
        if (!validationResult.isValid) {
            binding.spaceBeforeButton.visibility = View.VISIBLE
        }
        return updateFieldStatus(
            container = binding.confirmPasswordContainer,
            errorTextView = binding.tvErrorConfirmPassword,
            validationResult = validationResult
        )
    }

    private fun updateFieldStatus(
        editText: EditText? = null,
        container: View? = null,
        errorTextView: TextView,
        validationResult: ValidatorUtil.ValidationResult
    ): Boolean {
        val view = editText ?: container
        val errorDrawable = R.drawable.edit_text_error
        val normalDrawable = R.drawable.edit_text_outline

        if (validationResult.isValid) {
            errorTextView.visibility = View.GONE
            view?.background = ContextCompat.getDrawable(requireContext(), normalDrawable)
            return true
        } else {
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = validationResult.errorMessage
            view?.background = ContextCompat.getDrawable(requireContext(), errorDrawable)
            return false
        }
    }

    private fun validateAll(): Boolean {
        var isValid = true

        if (authMode == AuthMode.REGISTER_EMAIL || authMode == AuthMode.REGISTER_PHONE) {
            isValid = validateFullName() && isValid
            isValid = validateBirthDate() && isValid
            isValid = validateConfirmPassword() && isValid
        }

        if (authMode == AuthMode.REGISTER_EMAIL || authMode == AuthMode.LOGIN_EMAIL) {
            isValid = validateEmail() && isValid
        }

        if (authMode == AuthMode.REGISTER_PHONE || authMode == AuthMode.LOGIN_PHONE) {
            isValid = validatePhone() && isValid
        }

        isValid = validatePassword() && isValid

        return isValid
    }

    private fun setupListeners() {
        with(binding) {
            btnAction.setOnClickListener {
                if (validateAll()) {
                    when (authMode) {
                        AuthMode.REGISTER_EMAIL -> registerWithEmail()
                        AuthMode.REGISTER_PHONE -> registerWithPhone()
                        AuthMode.LOGIN_EMAIL -> loginWithEmail()
                        AuthMode.LOGIN_PHONE -> loginWithPhone()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.failed_form),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            birthdateContainer.setOnClickListener {
                showDatePicker()
            }

            imgCalendar.setOnClickListener {
                showDatePicker()
            }

            btnGoogleContainer.setOnClickListener {
                showLoading()
                binding.root.postDelayed({
                    hideLoading()
                    Toast.makeText(
                        requireContext(),
                        "Google authentication belum",
                        Toast.LENGTH_SHORT
                    ).show()
                }, 1500)
            }

            tvActionText.setOnClickListener {
                when (authMode) {
                    AuthMode.REGISTER_EMAIL, AuthMode.REGISTER_PHONE ->
                        (activity as? AuthActivity)?.switchToLogin()

                    AuthMode.LOGIN_EMAIL, AuthMode.LOGIN_PHONE ->
                        (activity as? AuthActivity)?.switchToRegister()
                }
            }

            tvForgotPassword.setOnClickListener {
                val intent = Intent(requireContext(), ForgetPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                selectedDate = calendar.time
                updateBirthdateDisplay()
                validateBirthDate()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.maxDate = System.currentTimeMillis()
        datePicker.show()
    }

    private fun updateBirthdateDisplay() {
        binding.tvBirthdateValue.text = dateFormat.format(calendar.time)
    }

    private fun registerWithEmail() {
        val fullName = binding.etFullname.text.toString()
        val email = binding.etEmail.text.toString()
        val birthdate =
            if (binding.tvBirthdateValue.text.isNullOrEmpty()) "" else dateFormat.format(calendar.time)
        val password = binding.etPassword.text.toString()

        showLoading()

        // Simulate network call
        binding.root.postDelayed({
            hideLoading()
            Toast.makeText(
                requireContext(),
                "Registrasi berhasil untuk email: $email",
                Toast.LENGTH_SHORT
            ).show()

            // Navigate to CompleteProfileActivity
            val intent = Intent(requireContext(), CompleteProfileActivity::class.java).apply {
                putExtra("fullName", fullName)
                putExtra("email", email)
                putExtra("birthdate", birthdate)
            }
            startActivity(intent)
            activity?.finish()
        }, 1500)
    }


    private fun registerWithPhone() {
        val fullName = binding.etFullname.text.toString()
        val phoneNumber = binding.etPhoneNumber.text.toString()
        val birthdate =
            if (binding.tvBirthdateValue.text.isNullOrEmpty()) "" else dateFormat.format(calendar.time)
        val password = binding.etPassword.text.toString()

        showLoading()

        // Simulate network call
        binding.root.postDelayed({
            hideLoading()
            Toast.makeText(
                requireContext(),
                "Registrasi berhasil untuk nomor: $phoneNumber",
                Toast.LENGTH_SHORT
            ).show()

            // Navigate to CompleteProfileActivity
            val intent = Intent(requireContext(), CompleteProfileActivity::class.java).apply {
                putExtra("fullName", fullName)
                putExtra("phoneNumber", phoneNumber)
                putExtra("birthdate", birthdate)
            }
            startActivity(intent)
            activity?.finish()
        }, 1500)
    }

    private fun loginWithEmail() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        showLoading()

        // Simulate network call
        binding.root.postDelayed({
            hideLoading()
            Toast.makeText(
                requireContext(),
                "Login berhasil untuk email: $email",
                Toast.LENGTH_SHORT
            ).show()
            // Call ViewModel
        }, 1500)
    }

    private fun loginWithPhone() {
        val phoneNumber = binding.etPhoneNumber.text.toString()
        val password = binding.etPassword.text.toString()

        showLoading()

        // Simulate network call
        binding.root.postDelayed({
            hideLoading()
            Toast.makeText(
                requireContext(),
                "Login berhasil untuk nomor: $phoneNumber",
                Toast.LENGTH_SHORT
            ).show()
            // Call ViewModel
        }, 1500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun View.setConstraintTopToBottomOf(targetView: View) {
        val params = layoutParams as ConstraintLayout.LayoutParams
        params.topToBottom = targetView.id
        layoutParams = params
    }

    companion object {
        private const val ARG_AUTH_MODE = "auth_mode"

        fun newInstance(authMode: AuthMode): AuthBaseFragment {
            return AuthBaseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_AUTH_MODE, authMode.name)
                }
            }
        }
    }

    enum class AuthMode {
        REGISTER_EMAIL,
        REGISTER_PHONE,
        LOGIN_EMAIL,
        LOGIN_PHONE
    }
}