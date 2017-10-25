package com.mstztrk.j10_10mytodoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mstztrk.j10_10mytodoapp.base.BaseActivity;
import com.mstztrk.j10_10mytodoapp.model.Kisi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends BaseActivity {

    EditText txtName, txtSurname, txtEmail;
    TextView txtBirthdate;
    Button btnUpdate, btnCalender;
    Kisi currentUser;
    Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initMyComponets();
        getUserData();
    }

    private void getUserData() {
        showProgress("Kullanici bilgileri getiriliyor");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) finish();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users");
        Query query = myRef.child(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(Kisi.class);
                txtName.setText(currentUser.getName());
                txtSurname.setText(currentUser.getSurname());
                txtEmail.setText(currentUser.getEmail());
                if (currentUser.getBirthDate() != null) {
                    Date bd = new Date(currentUser.getBirthDate());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
                    selectedDate = bd;
                    txtBirthdate.setText(simpleDateFormat.format(bd));
                }
                hideProgress();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initMyComponets() {
        txtBirthdate = (TextView) findViewById(R.id.profile_birthdate);
        txtEmail = (EditText) findViewById(R.id.profile_txtEmail);
        txtName = (EditText) findViewById(R.id.profile_txtName);
        txtSurname = (EditText) findViewById(R.id.profile_txtSurName);
        btnCalender = (Button) findViewById(R.id.profile_btnCalender);
        btnUpdate = (Button) findViewById(R.id.profile_btnUpdate);

        btnCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR) - 20;
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        selectedDate = new Date(year - 1900, monthOfYear, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");

                        txtBirthdate.setText(simpleDateFormat.format(selectedDate));
                    }
                }, year, month, day);
                datePicker.setTitle("Select BirthDate");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Select", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", datePicker);

                datePicker.show();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isEmailChanged = !currentUser.getEmail().equals(user.getEmail());
                Kisi guncellenecekKisi = new Kisi();
                guncellenecekKisi.setId(user.getUid());
                guncellenecekKisi.setEmail(txtEmail.getText().toString());
                guncellenecekKisi.setSurname(txtSurname.getText().toString());
                guncellenecekKisi.setName(txtName.getText().toString());
                if (selectedDate != null) {
                    guncellenecekKisi.setBirthDate(selectedDate.toString());
                } else {
                    guncellenecekKisi.setBirthDate(currentUser.getBirthDate());
                }
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference().child("users");
                myRef.child(user.getUid()).setValue(guncellenecekKisi);
                if (isEmailChanged) {
                    mAuth = FirebaseAuth.getInstance();
                    user = mAuth.getCurrentUser();
                    user.updateEmail(txtEmail.getText().toString());
                    user.sendEmailVerification();
                    mAuth.signOut();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                }
                finish();
            }
        });
    }
}
