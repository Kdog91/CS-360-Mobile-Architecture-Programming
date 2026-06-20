package com.kevinsimmons.inventoryappkevinsimmons;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);
    }

    // Called when the Login button is clicked
    public void loginUser(View view) {
        EditText usernameField = findViewById(R.id.usernameField);
        EditText passwordField = findViewById(R.id.passwordField);

        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check the username and password against the database
        boolean validUser = databaseHelper.checkUser(username, password);

        if (validUser) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            // Move to the inventory screen
            Intent intent = new Intent(LoginActivity.this, InventoryActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    // Called when the Create Account button is clicked
    public void createAccount(View view) {
        EditText usernameField = findViewById(R.id.usernameField);
        EditText passwordField = findViewById(R.id.passwordField);

        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.userExists(username)) {
            Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = databaseHelper.registerUser(username, password);

        if (success) {
            Toast.makeText(this, "Account created! You can now log in.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error creating account", Toast.LENGTH_SHORT).show();
        }
    }
}