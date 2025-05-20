package com.quad.formvalidation.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.quad.formvalidation.rules.ValidationRule

class ValidatedField(
    initial: String = "",
    val rules: List<ValidationRule> = emptyList()
) {
    private val maxLength =
        rules.filterIsInstance<ValidationRule.MinMaxLength>().firstOrNull()?.maxLength
            ?: Int.MAX_VALUE

    var text by mutableStateOf(initial)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun updateText(newText: String) {
        text = newText.take(maxLength)
        validate()
    }

    fun validate(): Boolean {
        // Check if field is optional
        val isOptional = rules.any { it is ValidationRule.Optional }
        val isEmpty = text.isBlank()

        if (isOptional && isEmpty) {
            error = null
            return true  // Skip other rules if optional and empty
        }

        // Otherwise, apply all rules normally
        error = rules.firstOrNull { !it.isValid(text) }?.errorMessage
        return error == null
    }
}

private fun ValidationRule.isValid(input: String): Boolean {
    return when (this) {
        is ValidationRule.Required -> input.isNotBlank()
        is ValidationRule.MinMaxLength -> input.length in minLength..maxLength
        is ValidationRule.ExactLength -> input.length == length
        is ValidationRule.RegexMatch -> pattern.matches(input)
        is ValidationRule.DigitsOnly -> input.all { it.isDigit() }
        is ValidationRule.Custom -> validator(input)
        is ValidationRule.Optional -> true
    }
}