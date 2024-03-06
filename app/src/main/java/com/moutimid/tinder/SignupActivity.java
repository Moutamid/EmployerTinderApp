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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputConfirmPassword;
    private EditText modalitiesTextInputEditText, timeTextInputEditText, populationTextInputEditText;
    private Button btnSignUp;
    private TextView btnSignIn;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    String selectedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_candidate);
        auth = FirebaseAuth.getInstance();
        inputName = (EditText) findViewById(R.id.name);
        inputConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        btnSignIn = (TextView) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        modalitiesTextInputEditText = (EditText) findViewById(R.id.modalitiesTextInputEditText);
        timeTextInputEditText = (EditText) findViewById(R.id.timeTextInputEditText);
        populationTextInputEditText = (EditText) findViewById(R.id.populationTextInputEditText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String name = inputName.getText().toString().trim();
                String confirm_password = inputConfirmPassword.getText().toString().trim();
                String modalitiesTextInputEditText_str = modalitiesTextInputEditText.getText().toString().trim();
                String timeTextInputEditText_str = timeTextInputEditText.getText().toString().trim();
                String populationTextInputEditText_str = populationTextInputEditText.getText().toString().trim();
                Stash.put("name", name);
                if (TextUtils.isEmpty(modalitiesTextInputEditText_str)) {
                    show_toast("Please enter complete details", 0);
                    return;
                }
                if (TextUtils.isEmpty(timeTextInputEditText_str)) {
                    show_toast("Please enter complete details", 0);

                    return;
                }
                if (TextUtils.isEmpty(populationTextInputEditText_str)) {
                    show_toast("Please enter complete details", 0);
                    return;
                }
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
                RadioGroup radioGroup = findViewById(R.id.are_you);
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    selectedText = selectedRadioButton.getText().toString();
                } else {
                    Toast.makeText(SignupActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    return;
                }
                Dialog lodingbar = new Dialog(SignupActivity.this);
                lodingbar.setContentView(R.layout.loading);
                Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
                lodingbar.setCancelable(false);
                lodingbar.show();
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
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
                                    userModel.modalities = modalitiesTextInputEditText_str;
                                    userModel.time_in_fields = timeTextInputEditText_str;
                                    userModel.population = populationTextInputEditText_str;
                                    userModel.selectedText = selectedText;
                                    userModel.type = "Candidate";
                                    Stash.put("type", "Candidate");

                                    FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Stash.put("user", userModel);
                                            Stash.put("user_name", name);
                                            show_toast("Account is created successfully", 1);
                                            startActivity(new Intent(SignupActivity.this, AddCandidateDetailsActivity.class));
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
