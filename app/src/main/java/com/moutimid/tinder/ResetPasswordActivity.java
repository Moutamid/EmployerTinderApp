package com.moutimid.tinder;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.moutamid.tinder.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnReset;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private TextInputLayout textInputLayout;
    private EditText textInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = (EditText) findViewById(R.id.email);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();


        textInputLayout = findViewById(R.id.email_address);
        textInputEditText = findViewById(R.id.text_input_edit_text);

        textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Change background when user starts typing or clicks on the TextInputEditText
                    textInputLayout.setBackgroundResource(R.drawable.bg_button_register);
                } else {
                    // Revert back to the original background when focus is lost
                    textInputLayout.setBackgroundResource(R.drawable.overlay_black_bg);
                }
            }
        });

        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Change background when text is being typed
                textInputLayout.setBackgroundResource(R.drawable.bg_button_register);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = textInputEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    show_toast("Enter your registered email", 0);
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override

                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    show_toast("\"We have sent you a link on this email (\"+email+\") to reset your password!\"", 1);
                                } else {
                                    show_toast("\"Failed to send reset email!\"", 0);
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    public void back(View view) {
        onBackPressed();
    }
    public void show_toast(String message, int type) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}