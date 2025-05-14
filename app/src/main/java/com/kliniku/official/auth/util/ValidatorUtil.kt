package com.kliniku.official.auth.util

import android.content.Context
import android.util.Patterns
import com.kliniku.official.R
import java.util.Calendar
import java.util.Date

object ValidatorUtil {

    fun validateName(context: Context, name: String): ValidationResult {
        return when {
            name.isEmpty() -> ValidationResult(false, context.getString(R.string.error_name_empty))
            name.length < 3 -> ValidationResult(false, context.getString(R.string.error_name_too_short))
            !name.matches(Regex("^[a-zA-Z ]*$")) -> ValidationResult(false, context.getString(R.string.error_name_invalid_chars))
            else -> ValidationResult(true)
        }
    }

    fun validateEmail(context: Context, email: String): ValidationResult {
        return when {
            email.isEmpty() -> ValidationResult(false, context.getString(R.string.error_email_empty))
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult(false, context.getString(R.string.error_email_invalid_format))
            else -> ValidationResult(true)
        }
    }

    fun validatePhone(context: Context, phone: String): ValidationResult {
        return when {
            phone.isEmpty() -> ValidationResult(false, context.getString(R.string.error_phone_empty))
            !phone.matches(Regex("^[0-9]{10,13}$")) -> ValidationResult(false, context.getString(R.string.error_phone_length))
            !phone.startsWith("08") -> ValidationResult(false, context.getString(R.string.error_phone_prefix))
            else -> ValidationResult(true)
        }
    }

    fun validateBirthdate(context: Context, birthdate: String?, date: Date?): ValidationResult {
        if (birthdate.isNullOrEmpty() || date == null) {
            return ValidationResult(false, context.getString(R.string.error_birthdate_empty))
        }

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        calendar.time = date
        val birthYear = calendar.get(Calendar.YEAR)
        val age = currentYear - birthYear

        return when {
            age < 13 -> ValidationResult(false, context.getString(R.string.error_birthdate_too_young))
            age > 100 -> ValidationResult(false, context.getString(R.string.error_birthdate_invalid))
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(context: Context, password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult(false, context.getString(R.string.error_password_empty))
            password.length < 8 -> ValidationResult(false, context.getString(R.string.error_password_too_short))
            !password.matches(Regex(".*[A-Z].*")) -> ValidationResult(false, context.getString(R.string.error_password_uppercase))
            !password.matches(Regex(".*[a-z].*")) -> ValidationResult(false, context.getString(R.string.error_password_lowercase))
            !password.matches(Regex(".*[0-9].*")) -> ValidationResult(false, context.getString(R.string.error_password_number))
            !password.matches(Regex(".*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) ->
                ValidationResult(false, context.getString(R.string.error_password_special_char))
            else -> ValidationResult(true)
        }
    }

    fun validateConfirmPassword(context: Context, password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isEmpty() -> ValidationResult(false, context.getString(R.string.error_confirm_password_empty))
            password != confirmPassword -> ValidationResult(false, context.getString(R.string.error_password_mismatch))
            else -> ValidationResult(true)
        }
    }

    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )
}
