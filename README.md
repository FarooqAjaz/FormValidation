
# 🎯 Form Validation Library for Jetpack Compose

## Overview 🚀
FormValidation library is designed to handle input field validation in a simple, intuitive, and declarative way for Jetpack Compose-based applications. With this library, you can efficiently handle validation for a wide range of user inputs, making your forms user-friendly and robust.

## Features 🌟
- ✅ **Easy-to-Use Validation**: Seamlessly integrate validations for user input fields.
- 📏 **Customizable Rules**: You can define your own validation rules or use predefined ones like `Required`, `ExactLength`, `MinLength`, `MaxLength`, etc.
- 📱 **Visual Feedback**: Automatic error message display and form validation status.
- 🔒 **Secure**: Ensures proper validation for sensitive data like passwords and emails.
- ⚙️ **Support for Various Input Types**: Mobile numbers, email addresses, CNIC, passwords, and more.
- 💪 **Optional Field Support**: Fields can be marked as optional while still supporting validation when required.

## Installation 📦
To install and use this library in your project, follow these steps:

### Step 1: Add Maven Central to your project
```gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```

### Step 2: Add the dependency in your app-level build.gradle
```gradle
dependencies {
    implementation 'com.quadlogixs.compose:validation:0.0.1'
}
```

## Usage 📘

### 1. Initialize the Form Controller
```kotlin
val formState = remember { FormController() }
```

### 2. Define Validation Rules
```kotlin
val rules = listOf(
    ValidationRule.Required,
    ValidationRule.MinMaxLength(3, 50, "Name must be between 3 and 50 characters")
)
```

### 3. Add Validated TextField
```kotlin
ValidatedTextField(
    name = "name",
    label = "Full Name",
    form = formState,
    rules = rules
)
```

### 4. Handle Form Submission
```kotlin
Button(onClick = {
    val isValid = formState.validate()
    if (isValid) {
        // Proceed with the form submission
        val summary = """
            Name: ${formState.getValue("name")}
            Age: ${formState.getValue("age")}
            Email: ${formState.getValue("email")}
            CNIC: ${formState.getValue("cnic")}
            Mobile: ${formState.getValue("mobile")}
            """.trimIndent()
    }
}) {
    Text("Submit")
}
```

## Validation Rules 📝
Here are the available validation rules that you can use in your form fields:

- **Required**: Ensures the field is not empty.
- **ExactLength**: Validates that the input length matches exactly the given number.
- **MinMaxLength**: Validates that the input length is between a defined range.
- **DigitsOnly**: Ensures that the field contains only digits.
- **RegexMatch**: Allows for custom regex-based validation.
- **Custom**: Allows you to define a custom validation function.

## Example 📚
Here's an example with multiple fields:

```kotlin

ValidateTextField(
 name = "Full Name",
 label = "Full Name",
 form = formState,
 rules = listOf(
       ValidationRule.Optional,
       ValidationRule.MinMaxLength(3,4, "Name must be at least 3 characters"),
   )
)

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
ValidatedTextField(
    name = "email",
    label = "Email Address",
    form = formState,
    rules = emailRule() // Predefined email validation rule
)

ValidatedTextField(
    name = "mobile",
    label = "Mobile Number",
    form = formState,
    rules = mobileNumberRule() // Predefined mobile number validation rule
)


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
```

## Contributing 🤝
Feel free to fork this repository, submit pull requests, and contribute to the development of this library. We welcome any feedback or suggestions that improve the usability and functionality of the library.

## License 📄
This project is licensed under the MIT License - see the LICENSE file for details.

---
**Enjoy making user-friendly and validated forms with Jetpack Compose! 🚀**
