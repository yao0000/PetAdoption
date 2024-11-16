package com.pet.adoption.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.pet.adoption.R;
import com.pet.adoption.entities.Account;
import com.pet.adoption.services.firebase.FirestoreHelper;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etReenterPassword;

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

        Account account = new Account(email, password, username);
        account.signUp()
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this
                                        , task.getException().getMessage()
                                        , Toast.LENGTH_LONG)
                                .show();
                        return;
                    }

                    account.setUserUID(FirebaseAuth.getInstance().getUid());

                    FirestoreHelper.upload("users", FirebaseAuth.getInstance().getUid(), account)
                            .addOnCompleteListener(this, helperTask -> {
                                if (!helperTask.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this
                                            , Objects.requireNonNull(task.getException()).getMessage()
                                            , Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(RegisterActivity.this
                                        ,getString(R.string.success_register)
                                        , Toast.LENGTH_SHORT).show();

                                RegisterActivity.this.finish();
                                FirebaseAuth.getInstance().signOut();
                            });
                });
    }

}