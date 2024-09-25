package com.example.authapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.authapp.ui.theme.AuthAppTheme
import kotlinx.coroutines.selects.RegistrationFunction

class MainActivity : ComponentActivity() {

    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            RegistrationViewModel(registrationViewModel)
        }
    }
}


@Composable
fun RegistrationViewModel(viewModel: RegistrationViewModel){
    //state variables
    var email by remember { mutableStateOf("") }
    //var number by remember {mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var confirmPassword by remember { mutableStateOf("")}
    var message by remember { mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            //if password = password, nothing will show up
            onValueChange = {password = it},
            label = { Text("Password")},
            // password -> "·······"
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),

        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = {confirmPassword = it},
            label = { Text("Confirm Password")},
            // password -> "·······"
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            if(password == confirmPassword){
                viewModel.register(email, password){ result ->
                    message = result
                }
            }else{
                message = "Passwords do not match"
            }
        }){
            Text("Register")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = message)

    }
}

class RegistrationViewModel : ViewModel(){
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(email: String, password: String, onResult: (String) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    onResult("Registration successful")
                } else {
                    onResult("Registration failed: ${task.exception?.message}")
                }
            }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AuthAppTheme {
    }
}