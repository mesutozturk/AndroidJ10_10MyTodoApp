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

public class LoginActivity extends BaseActivity {
    EditText txtEmail, txtPassword;
    Button btnLogin, btnRegister, btnForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initMyComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }

    private void initMyComponents() {
        txtEmail = (EditText) findViewById(R.id.login_txtEmail);
        txtPassword = (EditText) findViewById(R.id.login_txtPassword);
        btnLogin = (Button) findViewById(R.id.login_btnLogin);
        btnRegister = (Button) findViewById(R.id.login_btnRegister);
        btnForget = (Button) findViewById(R.id.login_btnForgetPass);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });
    }

    private void doLogin() {
        if (!validateForm(txtEmail, txtPassword)) return;
        showProgress("Sisteme giriş yapılıyor");
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    checkUser();
                } else if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Login işleminde bir hata oluştu", Toast.LENGTH_SHORT).show();
                }
                hideProgress();
            }
        });
    }

    private void checkUser() {
        showProgress("Sisteme bağlanıyor");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                Toast.makeText(LoginActivity.this, "Hoşgeldiniz", Toast.LENGTH_SHORT).show();
                //ana sayfa açılacak
            } else {
                verifyUser();
            }
        }
    }

    private void verifyUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(LoginActivity.this, "Posta kutunuzu kontrol ediniz", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
