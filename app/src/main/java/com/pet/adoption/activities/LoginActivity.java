package com.pet.adoption.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.pet.adoption.entities.Account;
import com.pet.adoption.R;

public class LoginActivity extends AppCompatActivity{
    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        onLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null)
            startActivity(new Intent(LoginActivity.this, FragmentActivity.class));
    }

    private void onLoad(){
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        mAuth = FirebaseAuth.getInstance();

        setEventListener();
    }

    private void setEventListener(){
        findViewById(R.id.btnLogin).setOnClickListener(e -> btnLogin_Click());
        findViewById(R.id.tvRegister).setOnClickListener(e -> tvRegister_Click());
    }

    public void btnLogin_Click(){
        String email = String.valueOf(etEmail.getText());
        String password = String.valueOf(etPassword.getText());

        Account account = new Account(email, password);
        account.login()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Toast.makeText(LoginActivity.this
                                , task.getException().getMessage()
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }

                    startActivity(new Intent(LoginActivity.this, FragmentActivity.class));
                    finish();
                });
    }

    public void tvRegister_Click(){
        startActivity(new Intent(this, RegisterActivity.class));
    }
}