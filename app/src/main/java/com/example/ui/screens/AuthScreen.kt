package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.RoyalPurple
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StockVisionViewModel

@Composable
fun AuthScreen(
  viewModel: StockVisionViewModel,
  initialIsRegister: Boolean = false,
  modifier: Modifier = Modifier
) {
  var isRegister by remember { mutableStateOf(initialIsRegister) }
  var fullName by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("investor@stockvision.ai") }
  var password by remember { mutableStateOf("StockVision2026!") }
  var confirmPassword by remember { mutableStateOf("StockVision2026!") }
  var showPassword by remember { mutableStateOf(false) }
  var rememberMe by remember { mutableStateOf(true) }
  var formError by remember { mutableStateOf<String?>(null) }
  var infoMessage by remember { mutableStateOf<String?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(rememberScrollState())
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    // Brand header
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.AutoAwesome,
        contentDescription = null,
        tint = NeonCyan,
        modifier = Modifier.size(32.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "StockVision AI",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onBackground
      )
    }

    Text(
      text = if (isRegister) "Create your investor account" else "Welcome back to market intelligence",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(28.dp))

    // Login / Register Tab Switcher
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        .padding(4.dp)
    ) {
      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(10.dp))
          .background(if (!isRegister) RoyalPurple else Color.Transparent)
          .clickable { isRegister = false; formError = null }
          .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "Sign In",
          fontWeight = FontWeight.Bold,
          color = if (!isRegister) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(10.dp))
          .background(if (isRegister) RoyalPurple else Color.Transparent)
          .clickable { isRegister = true; formError = null }
          .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "Register",
          fontWeight = FontWeight.Bold,
          color = if (isRegister) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Form Container
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        if (isRegister) {
          OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            placeholder = { Text("e.g. Alex Trader") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NeonCyan) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )
          Spacer(modifier = Modifier.height(14.dp))
        }

        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          label = { Text("Email Address") },
          placeholder = { Text("investor@domain.com") },
          leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NeonCyan) },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = password,
          onValueChange = { password = it },
          label = { Text("Password") },
          leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NeonCyan) },
          trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
              Icon(
                imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = "Show/Hide Password"
              )
            }
          },
          visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        )

        if (isRegister) {
          Spacer(modifier = Modifier.height(14.dp))
          OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NeonCyan) },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )
        } else {
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it },
                colors = CheckboxDefaults.colors(checkedColor = NeonCyan)
              )
              Text(
                text = "Remember me",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Text(
              text = "Forgot Password?",
              style = MaterialTheme.typography.bodySmall,
              color = NeonCyan,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.clickable {
                infoMessage = "Password reset link simulated to $email"
              }
            )
          }
        }

        formError?.let { err ->
          Spacer(modifier = Modifier.height(8.dp))
          Text(text = err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        infoMessage?.let { info ->
          Spacer(modifier = Modifier.height(8.dp))
          Text(text = info, color = NeonCyan, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Primary Submit Button
        Button(
          onClick = {
            if (isRegister) {
              if (fullName.isBlank()) {
                formError = "Please enter your full name"
                return@Button
              }
              if (password != confirmPassword) {
                formError = "Passwords do not match"
                return@Button
              }
              viewModel.login(fullName, email)
            } else {
              if (email.isBlank() || password.isBlank()) {
                formError = "Please enter email and password"
                return@Button
              }
              viewModel.login("Aarav Sharma", email)
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
        ) {
          Text(
            text = if (isRegister) "Create Account" else "Sign In to Dashboard",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00363F),
            fontSize = 15.sp
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Guest Demo Button
        OutlinedButton(
          onClick = {
            viewModel.login("Student Investor", "guest.trader@stockvision.ai")
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
          shape = RoundedCornerShape(14.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
        ) {
          Icon(Icons.Default.Bolt, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Instant Guest Access (Demo)",
            color = NeonCyan,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    TextButton(onClick = { viewModel.navigateTo(AppScreen.LANDING) }) {
      Text(
        text = "← Back to Home Page",
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
