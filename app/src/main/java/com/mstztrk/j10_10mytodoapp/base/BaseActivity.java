package com.mstztrk.j10_10mytodoapp.base;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mesut on 24.10.2017.
 */

public class BaseActivity extends AppCompatActivity {
    private final String EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    private final String PASSWORD_PATTERN = "^(?=.*\\d).{6,16}$";
    public ProgressDialog mProgressDialog;
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    public FirebaseDatabase database;
    public DatabaseReference myRef;

    public boolean validateForm(EditText txtEmail, EditText txtPassword) {
        boolean isvalid = true;
        String email = txtEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Gerekli!");
            return false;
        } else {
            txtEmail.setError(null);
        }
        String password = txtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("Gerekli.");
            return false;
        } else {
            txtPassword.setError(null);
        }
        if (!email.matches(EMAIL_PATTERN)) {
            txtEmail.setError("Lütfen geçerli bir email giriniz");
            return false;
        } else {
            txtEmail.setError(null);
        }
        if (!password.matches(PASSWORD_PATTERN)) {
            txtPassword.setError("en az 6 haneli ve rakamla biten bir şifre girmelisiniz");
            return false;
        } else {
            txtPassword.setError(null);
        }

        return isvalid;
    }

    public void showProgress(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Lütfen Bekleyiniz");
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
        }
    }

    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
