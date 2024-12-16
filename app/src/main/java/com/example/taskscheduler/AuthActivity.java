package com.example.taskscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthActivity extends AppCompatActivity {

    private LinearLayout layoutLogin, layoutRegister;
    private Button btnToggleLogin, btnToggleRegister, btnLogin, btnRegister, btnResetPassword;
    private EditText edtLoginEmail, edtLoginPassword, edtRegisterName, edtRegisterEmail, edtRegisterPassword;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize Views
        initializeViews();

        // Toggle Between Login and Register
        setupToggleButtons();

        // Login Button Action
        btnLogin.setOnClickListener(v -> performLogin());

        // Register Button Action
        btnRegister.setOnClickListener(v -> performRegistration());

        // Reset Password Button Action
        btnResetPassword.setOnClickListener(v -> sendPasswordResetEmail());
    }

    private void initializeViews() {
        layoutLogin = findViewById(R.id.layoutLogin);
        layoutRegister = findViewById(R.id.layoutRegister);
        btnToggleLogin = findViewById(R.id.btnToggleLogin);
        btnToggleRegister = findViewById(R.id.btnToggleRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        edtRegisterName = findViewById(R.id.edtRegisterName);
        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToggleButtons() {
        btnToggleLogin.setOnClickListener(v -> {
            layoutLogin.setVisibility(View.VISIBLE);
            layoutRegister.setVisibility(View.GONE);
            updateToggleButtonStyles(true);
        });

        btnToggleRegister.setOnClickListener(v -> {
            layoutLogin.setVisibility(View.GONE);
            layoutRegister.setVisibility(View.VISIBLE);
            updateToggleButtonStyles(false);
        });
    }

    private void updateToggleButtonStyles(boolean isLoginActive) {
        if (isLoginActive) {
            btnToggleLogin.setBackgroundTintList(ContextCompat.getColorStateList(AuthActivity.this, R.color.orange));
            btnToggleLogin.setTextColor(ContextCompat.getColor(AuthActivity.this, R.color.white));
            btnToggleRegister.setBackgroundTintList(ContextCompat.getColorStateList(AuthActivity.this, R.color.light_gray));
            btnToggleRegister.setTextColor(ContextCompat.getColor(AuthActivity.this, R.color.black));
        } else {
            btnToggleRegister.setBackgroundTintList(ContextCompat.getColorStateList(AuthActivity.this, R.color.orange));
            btnToggleRegister.setTextColor(ContextCompat.getColor(AuthActivity.this, R.color.white));
            btnToggleLogin.setBackgroundTintList(ContextCompat.getColorStateList(AuthActivity.this, R.color.light_gray));
            btnToggleLogin.setTextColor(ContextCompat.getColor(AuthActivity.this, R.color.black));
        }
    }

    private void performLogin() {
        String email = edtLoginEmail.getText().toString().trim();
        String password = edtLoginPassword.getText().toString().trim();

        if (!isValidInput(email, password, edtLoginEmail, edtLoginPassword)) return;

        showProgressBar(true);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    showProgressBar(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            showToast("Login Successful");
                            navigateToMainActivity(); // Redirect to MainActivity
                        }
                    } else {
                        showToast("Login Failed: " + task.getException().getMessage());
                    }
                });
    }

    private void performRegistration() {
        String name = edtRegisterName.getText().toString().trim();
        String email = edtRegisterEmail.getText().toString().trim();
        String password = edtRegisterPassword.getText().toString().trim();

        if (name.isEmpty()) {
            edtRegisterName.setError("Name is required");
            return;
        }
        if (!isValidInput(email, password, edtRegisterEmail, edtRegisterPassword)) return;

        showProgressBar(true);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    showProgressBar(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            showToast("Registration Successful");
                            navigateToMainActivity(); // Redirect to MainActivity
                        }
                    } else {
                        showToast("Registration Failed: " + task.getException().getMessage());
                    }
                });
    }

    private void sendPasswordResetEmail() {
        String email = edtLoginEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            edtLoginEmail.setError("Invalid email");
            return;
        }

        showProgressBar(true);
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    showProgressBar(false);
                    if (task.isSuccessful()) {
                        showToast("Password reset email sent!");
                    } else {
                        showToast("Failed to send reset email: " + task.getException().getMessage());
                    }
                });
    }

    private boolean isValidInput(String email, String password, EditText emailField, EditText passwordField) {
        if (email.isEmpty() || !isValidEmail(email)) {
            emailField.setError("Invalid email");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            passwordField.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressBar(boolean isVisible) {
        progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
