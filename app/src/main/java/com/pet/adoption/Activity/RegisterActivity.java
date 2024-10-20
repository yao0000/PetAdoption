package com.pet.adoption.Activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pet.adoption.R;
import com.pet.adoption.entities.Account;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etReenterPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        onLoad();
    }

    private void onLoad(){
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etReenterPassword = findViewById(R.id.etRePassword);
        findViewById(R.id.btnRegister).setOnClickListener(e -> btnRegister_Click());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void btnRegister_Click(){
        etUsername.setError(null);
        etPassword.setError(null);
        etReenterPassword.setError(null);

        String email = String.valueOf(etEmail.getText());
        String password = String.valueOf(etPassword.getText());
        String username = String.valueOf(etUsername.getText()).trim();

        if (username.isEmpty()){
            etUsername.setError(getString(R.string.required_field));
            return;
        }

        if (!password.equals(String.valueOf(etReenterPassword.getText()))){
            etPassword.setError(getString(R.string.password_mismatch));
            etReenterPassword.setError(getString(R.string.password_mismatch));
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this
                                        , Objects.requireNonNull(task.getException()).getMessage()
                                        , Toast.LENGTH_SHORT).show();
                        return;
                    }

                    addUserDB(new Account(email
                                        , username
                                        , Objects.requireNonNull(mAuth.getCurrentUser()).getUid()));
                });
    }

    private void addUserDB(Account account){
        db.collection("users")
                .add(account)
                .addOnSuccessListener(e -> {
                    Toast.makeText(RegisterActivity.this
                                    ,getString(R.string.success_register)
                                    , Toast.LENGTH_SHORT).show();

                    RegisterActivity.this.finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this
                                    , e.getMessage()
                                    , Toast.LENGTH_SHORT).show();

                    Objects.requireNonNull(mAuth.getCurrentUser())
                            .delete();
                });
    }
}