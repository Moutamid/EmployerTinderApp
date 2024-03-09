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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.tinder.R;
import com.moutimid.tinder.helpers.QuickHelp;
import com.moutimid.tinder.model.UserModel;
import com.moutimid.tinder.payments.SignupDialogClass;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView btnReset;
    Button btnLogin;
    private TextInputLayout emailtextInputLayout;
    private EditText inputEmail;

    private TextInputLayout passwordtextInputLayout;
    private EditText inputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.text_password_input_edit_text);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (TextView) findViewById(R.id.forgot_pass);
        auth = FirebaseAuth.getInstance();


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
                    inputEmail.setError("Please Enter");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Please Enter");

                    return;
                }


                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
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
                                            UserModel user = snapshot.getValue(UserModel.class);
                                            Stash.put("employee_user_model", user);
                                            String type = snapshot.child("type").getValue().toString();
                                            String name = snapshot.child("name").getValue().toString();
                                            Stash.put("employee_name", name);
                                            Stash.put("type", type);
                                            show_toast("Successfully Login", 1);
                                            lodingbar.dismiss();
                                            if (type.equals("Employer")) {
//                                                if (!Stash.getBoolean("premium")) {
//                                                    SignupDialogClass cdd = new SignupDialogClass(LoginActivity.this);
//                                                    cdd.show();
//                                                } else {
                                                    QuickHelp.goToActivityAndFinish(LoginActivity.this, MainActivity.class);
//                                                }
                                            } else {
                                                String fcmToken = snapshot.child("fcmToken").getValue().toString();
                                                String profile_img = snapshot.child("profile_img").getValue().toString();
                                                String pdfUrl = snapshot.child("pdfUrl").getValue().toString();
                                                Stash.put("cand_img",profile_img );
                                                Stash.put("pdfUrl",pdfUrl );
                                                Stash.put("token",fcmToken );
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

    public void back(View view) {
        onBackPressed();
    }
}