package com.moutimid.tinder;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.tinder.R;
import com.moutimid.tinder.model.UserModel;
import com.moutimid.tinder.payments.SignupDialogClass;

import java.util.Objects;

public class SignupActivityEmployee extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputConfirmPassword, company_name, bussiness_address, practice_time, hirings, phone_number;
    private Button btn_login;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        company_name = (EditText) findViewById(R.id.company_name);
        bussiness_address = (EditText) findViewById(R.id.bussiness_address);
        practice_time = (EditText) findViewById(R.id.practice_time);
        hirings = (EditText) findViewById(R.id.hirings);
        phone_number = (EditText) findViewById(R.id.phone_number);
        inputName = (EditText) findViewById(R.id.name);
        inputConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String name = inputName.getText().toString().trim();
                String confirm_password = inputConfirmPassword.getText().toString().trim();

                String company_name_str = company_name.getText().toString().trim();
                String bussiness_address_str = bussiness_address.getText().toString().trim();
                String practice_time_str = practice_time.getText().toString().trim();
                String hirings_str = hirings.getText().toString().trim();
                String phone_number_str = phone_number.getText().toString().trim();
                Stash.put("name", name);
                Stash.put("password", password);
                if (TextUtils.isEmpty(name)) {
                    show_toast(inputName);
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    show_toast(inputEmail);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    show_toast(inputPassword);
                    return;
                }
                if (TextUtils.isEmpty(confirm_password)) {
                    show_toast(inputConfirmPassword);
                    return;
                }
                if (TextUtils.isEmpty(company_name_str)) {
                    show_toast(company_name);
                    return;
                }
                if (TextUtils.isEmpty(bussiness_address_str)) {
                    show_toast(bussiness_address);
                    return;
                }
                if (TextUtils.isEmpty(practice_time_str)) {
                    show_toast(practice_time);
                    return;
                }
                if (TextUtils.isEmpty(phone_number_str)) {
                    show_toast(phone_number);
                    return;
                }
                if (password.length() < 6) {
                    inputPassword.setError("Password too short, enter minimum 6 characters!");
                    return;
                }
                if (!password.equals(confirm_password)) {
                    inputPassword.setError("Password too short, enter minimum 6 characters!");
                    inputConfirmPassword.setText("");
                    inputPassword.setText("");
                    return;
                }

//                progressBar.setVisibility(View.VISIBLE);
                Dialog lodingbar = new Dialog(SignupActivityEmployee.this);
                lodingbar.setContentView(R.layout.loading);
                Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
                lodingbar.setCancelable(false);
                lodingbar.show();

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivityEmployee.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    lodingbar.dismiss();
                                    show_data("Authentication failed." + task.getException(), 0);
                                } else {


                                    UserModel userModel = new UserModel();
                                    userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    userModel.email = email;
                                    userModel.name = name;
                                    userModel.type = "Employer";
                                    userModel.company_name = company_name_str;
                                    userModel.bussiness_address = bussiness_address_str;
                                    userModel.practice_time = practice_time_str;
                                    userModel.hirings = hirings_str;
                                    userModel.phone_number = phone_number_str;
                                    Stash.put("type", "Employer");
                                    Stash.put("employee_user_model", userModel);
                                    Stash.put("employee_name", name);
                                    Stash.put("premium", false);

                                    lodingbar.dismiss();

                                    SignupDialogClass cdd = new SignupDialogClass(SignupActivityEmployee.this);
                                    cdd.show();

                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void show_toast(EditText editText) {
        editText.setText("required");
    }


    public void show_data(String message, int type) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }
}
