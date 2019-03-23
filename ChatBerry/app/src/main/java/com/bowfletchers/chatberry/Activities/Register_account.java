package com.bowfletchers.chatberry.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.DatabaseManager.FirebaseDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register_account extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextUsername;
    private EditText editTextConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        mAuth = FirebaseDB.getFirebaseAuthReference();
        referenceUI();
    }

    public void cancelRegister(View view) {
        // navigate back to welcome page
        Intent backToSignInIntent = new Intent(Register_account.this, WelcomePage.class);
        startActivity(backToSignInIntent);
    }

    public void confirmRegister(View view) {
        // check if password match with confirm password input
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();

        if (password.equals(confirmPassword)) {
            // create User obj
            final Member member = new Member(username);

            // using email - password to sign up to firebase auth
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                // register ok
                                // let user log in and go to chat list
                                Intent chatIntent = new Intent(Register_account.this, ChatHistoryList.class);
                                chatIntent.putExtra("userName", member.getName());
                                startActivity(chatIntent);
                            } else {
                                Toast.makeText(Register_account.this, "Register failed", Toast.LENGTH_SHORT).show();
                            }
                         }
                    });
        }
        else {
            Toast.makeText(this, "Password input does not match", Toast.LENGTH_SHORT).show();
        }

    }

    private void referenceUI() {
        editTextEmail = findViewById(R.id.register_email_text);
        editTextUsername = findViewById(R.id.register_username_text);
        editTextPassword = findViewById(R.id.register_password_text);
        editTextConfirmPassword = findViewById(R.id.register_confirm_password_text);
    }
}
