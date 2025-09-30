# CS501_IA3-5
# Themed Login Form (Jetpack Compose • Material 3)

A simple login screen with **username** and **password** fields, styled using **Material 3** colors & typography. Includes inline **validation** that shows error text if fields are empty on submit. No icon imports required (password toggle uses a **Show/Hide** TextButton).

## Features
- `OutlinedTextField` for **Username** and **Password**
- Material 3 theming via `MaterialTheme(colorScheme, typography)`
- Inline errors using `isError` + `supportingText`
- Password **Show/Hide** TextButton (no icons)
- Submit by **button** or keyboard **Done**
- Snackbar feedback on success
- State kept with `rememberSaveable`
