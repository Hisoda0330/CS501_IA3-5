package com.example.cs501_ia3_5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Login", style = MaterialTheme.typography.titleLarge) }
                        )
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { inner ->
                    Box(Modifier.fillMaxSize().padding(inner)) {
                        LoginForm(
                            onSubmit = { user, _ ->
                                // Use coroutine scope
                                scope.launch {
                                    snackbarHostState.showSnackbar("Welcome, $user!")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/** Simple Material 3 theme:light */
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = Typography(),
        content = content
    )
}

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    onSubmit: (username: String, password: String) -> Unit
) {
    val focus = LocalFocusManager.current

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    var userError by rememberSaveable { mutableStateOf<String?>(null) }
    var passError by rememberSaveable { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        userError = if (username.isBlank()) "Username is required" else null
        passError = if (password.isBlank()) "Password is required" else null
        return userError == null && passError == null
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Welcome back",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                if (userError != null && it.isNotBlank()) userError = null
            },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = userError != null,
            supportingText = {
                if (userError != null) {
                    Text(userError!!, color = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                if (passError != null && it.isNotBlank()) passError = null
            },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { showPassword = !showPassword }) {
                    Text(if (showPassword) "Hide" else "Show")
                }
            },
            isError = passError != null,
            supportingText = {
                if (passError != null) {
                    Text(passError!!, color = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (validate()) {
                        focus.clearFocus()
                        onSubmit(username.trim(), password)
                    }
                }
            )
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                if (validate()) {
                    focus.clearFocus()
                    onSubmit(username.trim(), password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = username.isNotBlank() || password.isNotBlank()
        ) {
            Text("Sign In", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    AppTheme { LoginForm(onSubmit = { _, _ -> }) }
}