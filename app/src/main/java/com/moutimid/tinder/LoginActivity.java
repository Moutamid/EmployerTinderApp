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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.tinder.R;
import com.moutimid.tinder.helpers.QuickHelp;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private TextView btnSignup, btnReset;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // set the view now
        setContentView(R.layout.activity_login);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (TextView) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (TextView) findViewById(R.id.btn_reset_password);

        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    show_toast("Email address is not yet provided", 0);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    show_toast("Password is not yet provided", 0);

                    return;
                }


                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6) {
                                        show_toast(getString(R.string.minimum_password), 0);
                                    } else {
                                        show_toast(getString(R.string.auth_failed), 0);
                                    }
                                } else {
                                    Dialog lodingbar = new Dialog(LoginActivity.this);
                                    lodingbar.setContentView(R.layout.loading);
                                    Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
                                    lodingbar.setCancelable(false);
                                    lodingbar.show();

                                    FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String type = snapshot.child("type").getValue().toString();
                                            String name = snapshot.child("name").getValue().toString();
                                            Stash.put("user_name", name);
                                            Stash.put("type", type);
                                            show_toast("Successfully Login", 1);
                                            lodingbar.dismiss();
                                            if (type.equals("Employer")) {
                                                QuickHelp.goToActivityAndFinish(LoginActivity.this, MainActivity.class);
                                            } else {
                                                QuickHelp.goToActivityAndFinish(LoginActivity.this, HomePage.class);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                        });
            }
        });
    }

    public void show_toast(String message, int type) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}