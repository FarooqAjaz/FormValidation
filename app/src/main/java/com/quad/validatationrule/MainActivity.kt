package com.quad.validatationrule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quad.formvalidation.core.FormController
import com.quad.formvalidation.rules.ValidationRule
import com.quad.formvalidation.rules.cnicRule
import com.quad.formvalidation.rules.emailRule
import com.quad.formvalidation.rules.matchFieldRule
import com.quad.formvalidation.rules.mobileNumberRule
import com.quad.formvalidation.rules.passwordRule
import com.quad.validatationrule.ui.theme.ValidatationRuleDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ValidatationRuleDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ValidateFormScreen2(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ValidateFormScreen2(
    modifier: Modifier = Modifier
) {
    val formState = remember { FormController() }
    Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
        Spacer(modifier = modifier.height(30.dp))
        // Name (Required + MinLength)
        ValidateTextField(
            name = "Full Name",
            label = "Full Name",
            form = formState,
            rules = listOf(
                ValidationRule.Optional,
                ValidationRule.MinMaxLength(3,4, "Name must be at least 3 characters"),
            )
        )

        Spacer(Modifier.height(12.dp))

        // Age (DigitsOnly + MinValue + MaxValue)
        ValidateTextField(
            name = "age",
            label = "Age",
            form = formState,
            rules = listOf(
                ValidationRule.Required(errorMessage = "This field is mandatory"),
                ValidationRule.DigitsOnly(),
                ValidationRule.MinMaxLength(4, maxLength = 5, errorMessage = "Must be at least 4"),
            )
        )

        Spacer(Modifier.height(12.dp))

        // Email (Regex Match + Required)
        ValidateTextField(
            name = "email",
            label = "Email Address",
            form = formState,
            rules = emailRule()
        )

        Spacer(Modifier.height(12.dp))

        // CNIC (ExactLength + Digits + Formatting)
        ValidateTextField(
            name = "cnic",
            label = "CNIC",
            form = formState,
            rules = cnicRule()
        )

        Spacer(Modifier.height(12.dp))

        // Mobile (ExactLength + Regex + Formatting)
        ValidateTextField(
            name = "mobile",
            label = "Mobile Number",
            form = formState,
            rules = mobileNumberRule()
        )

        Spacer(Modifier.height(12.dp))

        // Password (min length + uppercase + special)
        ValidateTextField(
            name = "password",
            label = "Password",
            form = formState,
            rules = passwordRule()
        )

        Spacer(Modifier.height(12.dp))

        // Confirm Password (matches password)
        ValidateTextField(
            name = "confirmPassword",
            label = "Confirm Password",
            form = formState,
            rules = matchFieldRule(targetValueProvider = { formState.getValue("password") })
        )
        Spacer(Modifier.height(24.dp))


        Button(onClick = {
            val isValid = formState.validate()
            if (isValid) {
                val summary = """
                    Name: ${formState.getValue("name")}
                    Age: ${formState.getValue("age")}
                    Email: ${formState.getValue("email")}
                    CNIC: ${formState.getValue("cnic")}
                    Mobile: ${formState.getValue("mobile")}
                    Password: ${formState.getValue("password")}
                """.trimIndent()
                println(summary)
                // Handle successful submission
            }
        }) {
            Text("Submit")
        }

    }
}

@Composable
fun ValidateTextField(
    name: String,
    label: String,
    form: FormController,
    rules: List<ValidationRule>,
    modifier: Modifier = Modifier
) {
    val field = remember { form.registerField(name, rules) }

    Column(modifier) {
        OutlinedTextField(
            value = field.text,
            onValueChange = {
                field.updateText(it)
                field.validate()
            },
            label = { Text(label) },
            isError = field.error != null,
            modifier = Modifier.fillMaxWidth()
        )
        field.error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
    }
}