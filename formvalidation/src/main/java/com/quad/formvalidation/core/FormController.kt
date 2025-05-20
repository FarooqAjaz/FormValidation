package com.quad.formvalidation.core

import androidx.compose.runtime.mutableStateListOf
import com.quad.formvalidation.rules.ValidationRule

class FormController {
    // List preserves insertion order and Compose observes changes
    private val fields = mutableStateListOf<Pair<String, ValidatedField>>()

    fun registerField(name: String, rules: List<ValidationRule>): ValidatedField {
        val existing = fields.find { it.first == name }?.second
        if (existing != null) return existing

        val newField = ValidatedField(rules = rules)
        fields.add(name to newField)
        return newField
    }

    fun validate(): Boolean = fields.all { it.second.validate() }

    fun getValue(name: String): String = fields.find { it.first == name }?.second?.text.orEmpty()

    fun getError(name: String): String? = fields.find { it.first == name }?.second?.error

    fun getAllErrors(): Map<String, String?> = fields.associate { it.first to it.second.error }
}
