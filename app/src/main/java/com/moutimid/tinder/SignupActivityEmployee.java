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
import android.widget.TextView;
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

import java.util.Objects;

public class SignupActivityEmployee extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputConfirmPassword;
    private Button btn_login;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

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
                Stash.put("name", name);
                Stash.put("password", password);
                if (TextUtils.isEmpty(name)) {
                    show_toast("Enter name", 0);
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    show_toast("Enter email address!", 0);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    show_toast("Enter password!", 0);
                    return;
                }
                if (TextUtils.isEmpty(confirm_password)) {
                    show_toast("Enter confirm password!", 0);
                    return;
                }


                if (password.length() < 6) {
                    show_toast("Password too short, enter minimum 6 characters!", 0);
                    return;
                }
                if (!password.equals(confirm_password)) {
                    show_toast("Password is not matched", 0);
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
                                    show_toast("Authentication failed." + task.getException(), 0);
                                } else {
                                    UserModel userModel = new UserModel();
                                    userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    userModel.email = email;
                                    userModel.name = name;
                                    userModel.type = "Employer";
                                    Stash.put("type", "Employer");
                                    FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Stash.put("user", userModel);
                                            Stash.put("user_name", name);
                                            show_toast("Account is created successfully", 1);
                                            startActivity(new Intent(SignupActivityEmployee.this, MainActivity.class));
                                            lodingbar.dismiss();
                                            finishAffinity();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            lodingbar.dismiss();
                                            show_toast("Something went wrong. Please try again", 0);
                                        }
                                    });

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

    public void show_toast(String message, int type) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
