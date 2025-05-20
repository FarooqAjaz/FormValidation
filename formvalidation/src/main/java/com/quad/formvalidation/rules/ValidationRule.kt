package com.quad.formvalidation.rules

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

sealed class ValidationRule(
    open val errorMessage: String,
    open val visualTransformation: VisualTransformation = VisualTransformation.None
) {
    data object Optional : ValidationRule("")
    data class Required(override val errorMessage: String = "This field is required") : ValidationRule(errorMessage)
    data class ExactLength(val length: Int, override val errorMessage: String) : ValidationRule(errorMessage)
    data class MinMaxLength(val minLength: Int, val maxLength: Int = 100, override val errorMessage: String) : ValidationRule(errorMessage)
    data class DigitsOnly(override val errorMessage: String = "Only digits allowed") : ValidationRule(errorMessage)

    data class RegexMatch(
        val pattern: Regex,
        override val errorMessage: String,
        override val visualTransformation: VisualTransformation = VisualTransformation.None
    ) : ValidationRule(errorMessage, visualTransformation)

    data class Custom(
        val validator: (String) -> Boolean,
        override val errorMessage: String,
        override val visualTransformation: VisualTransformation = VisualTransformation.None
    ) : ValidationRule(errorMessage, visualTransformation)
}

fun emailRule(
    required: Boolean = true,
    errorMessage: String = "Invalid email format"
): List<ValidationRule> {
    val rules = mutableListOf<ValidationRule>()
    if (required) rules += ValidationRule.Required()
    rules += ValidationRule.RegexMatch(
        pattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"),
        errorMessage = errorMessage
    )
    return rules
}

fun mobileNumberRule(
    required: Boolean = true,
    inputLength: Int = 11,
    startWith: String = "03",
    errorMessage: String = "Mobile number should be start with 03",
    enableFormatting: Boolean = true
): List<ValidationRule> {
    val rules = mutableListOf<ValidationRule>()
    if (required) rules += ValidationRule.Required()
    rules += ValidationRule.ExactLength(inputLength, "Mobile must be $inputLength digits")
    rules += ValidationRule.RegexMatch(
        pattern = Regex("^$startWith\\d{${inputLength - startWith.length}}$"),
        errorMessage = errorMessage,
        visualTransformation = if (enableFormatting) VisualTransformation { text ->
            val formatted = text.chunked(2).joinToString(" ")
            TransformedText(AnnotatedString(formatted), OffsetMapping.Identity)
        } else VisualTransformation.None
    )
    return rules
}

fun cnicRule(
    required: Boolean = true,
    errorMessage: String = "Invalid CNIC format",
    enableFormatting: Boolean = true
): List<ValidationRule> {
    val rules = mutableListOf<ValidationRule>()
    if (required) rules += ValidationRule.Required()
    rules += ValidationRule.ExactLength(13, "CNIC must be 13 digits")
    rules += ValidationRule.RegexMatch(
        pattern = Regex("^\\d{13}$"),
        errorMessage = errorMessage,
        visualTransformation = if (enableFormatting) VisualTransformation { text ->
            val trimmed = text.take(13).padEnd(13)
            val formatted = "${trimmed.take(5)}-${trimmed.drop(5).take(7)}-${trimmed.takeLast(1)}"
            TransformedText(AnnotatedString(formatted.trimEnd('-')), OffsetMapping.Identity)
        } else VisualTransformation.None
    )
    return rules
}

fun passwordRule(
    required: Boolean = true,
    minLength: Int = 8,
    maxLength: Int = 8,
    mustContainUppercase: Boolean = true,
    mustContainSpecial: Boolean = true,
    errorMessage: String = "Password too weak"
): List<ValidationRule> {
    val rules = mutableListOf<ValidationRule>()
    if (required) rules += ValidationRule.Required()
    rules += ValidationRule.MinMaxLength(minLength, maxLength = maxLength, "Minimum $minLength characters")

    if (mustContainUppercase) {
        rules += ValidationRule.Custom(
            validator = { it.any { ch -> ch.isUpperCase() } },
            errorMessage = "Must contain at least one uppercase letter"
        )
    }

    if (mustContainSpecial) {
        rules += ValidationRule.Custom(
            validator = { it.any { ch -> "!@#\$%^&*()-_=+[{]}|;:',<.>/?".contains(ch) } },
            errorMessage = "Must contain at least one special character"
        )
    }

    return rules
}

fun matchFieldRule(
    targetValueProvider: () -> String?,
    errorMessage: String = "Fields do not match"
): List<ValidationRule> = listOf(
    ValidationRule.Custom(
        validator = { input -> input == targetValueProvider().orEmpty() },
        errorMessage = errorMessage
    )
)
