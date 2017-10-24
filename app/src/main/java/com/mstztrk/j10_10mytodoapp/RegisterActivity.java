package com.mstztrk.j10_10mytodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mstztrk.j10_10mytodoapp.base.BaseActivity;

public class RegisterActivity extends BaseActivity {
    EditText txtEmail, txtPassword;
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initMyComponent();
    }

    private void initMyComponent() {
        txtEmail = (EditText) findViewById(R.id.register_txtEmail);
        txtPassword = (EditText) findViewById(R.id.register_txtPassword);
        btnLogin = (Button) findViewById(R.id.register_btnLogin);
        btnRegister = (Button) findViewById(R.id.register_btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
    }

    private void createUser() {
        if (!validateForm(txtEmail, txtPassword)) {
            return;
        }
        showProgress("Kullanıcı Kaydediliyor");
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Kayıt işleminde bir hata oluştu", Toast.LENGTH_SHORT).show();
                } else {
                    hideProgress();
                    finish();
                }
            }
        });
    }
}
