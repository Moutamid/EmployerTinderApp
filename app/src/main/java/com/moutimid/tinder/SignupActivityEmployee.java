package com.moutimid.tinder;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.tinder.R;
import com.moutimid.tinder.Admin.Model.Question;
import com.moutimid.tinder.model.UserModel;
import com.moutimid.tinder.payments.SignupDialogClass;

import java.util.Objects;

public class SignupActivityEmployee extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputConfirmPassword;
    private Button btn_login;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private LinearLayout questionsLayout;
    DatabaseReference databaseReference, databaseReferencequestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        questionsLayout = findViewById(R.id.questionsLayout);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("questions");
        databaseReferencequestions = FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("Users");
        loadQuestions();
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
                                    Stash.put("type", "Employer");
                                    Stash.put("employee_user_model", userModel);
                                    Stash.put("employee_name", name);
                                    Stash.put("premium", false);
                                    saveEditedQuestions(userModel.uid);
                                    lodingbar.dismiss();


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

    private void loadQuestions() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    if (question != null) {
                        addEditText(question.getText(), snapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void addEditText(String text, String key) {
        TextInputLayout textInputLayout = (TextInputLayout) LayoutInflater.from(this).inflate(R.layout.item_edit_question, questionsLayout, false);
        textInputLayout.setHint(text);
        textInputLayout.setTag(key);
        questionsLayout.addView(textInputLayout);
    }

    private void saveEditedQuestions(String uid) {
        for (int i = 0; i < questionsLayout.getChildCount(); i++) {
            TextInputLayout textInputLayout = (TextInputLayout) questionsLayout.getChildAt(i);
            String key = (String) textInputLayout.getTag();
            String question = textInputLayout.getHint().toString();
            TextInputEditText textInputEditText = textInputLayout.findViewById(R.id.editTextQuestion);
            String answer = textInputEditText.getText().toString().trim();
            if (!answer.isEmpty()) {
                saveQuestionAndAnswer(key, question, answer, uid);
            }
        }
        SignupDialogClass cdd = new SignupDialogClass(SignupActivityEmployee.this);
        cdd.show();
    }

    private void saveQuestionAndAnswer(String key, String question, String answer, String uid) {
        databaseReferencequestions.child(uid).child("question").child(key).child("text").setValue(question);
        databaseReferencequestions.child(uid).child("question").child(key).child("answer").setValue(answer);
    }
}
