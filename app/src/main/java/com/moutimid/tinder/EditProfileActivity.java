package com.moutimid.tinder;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.tinder.R;
import com.moutimid.tinder.model.UserModel;

import java.util.Map;
import java.util.Objects;


public class EditProfileActivity extends AppCompatActivity {
    public static final String TAG = EditProfileActivity.class.getSimpleName();
    private Button signOut;
    private EditText name, newEmail;
    private LinearLayout questionsLayout;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        signOut = findViewById(R.id.sign_out);

        questionsLayout = findViewById(R.id.questionsLayout);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("TinderEmployeeApp")
                .child("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child("question");

        // Load questions from Firebase
        loadQuestions();

        UserModel userModel = (UserModel) Stash.getObject("employee_user_model", UserModel.class);

        newEmail = findViewById(R.id.new_email);
        name = findViewById(R.id.name);
        name.setText(userModel.name);
        newEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_str = name.getText().toString().trim();

                if (TextUtils.isEmpty(name_str)) {
                    show_toast(name);
                    return;
                }

                Dialog lodingbar = new Dialog(EditProfileActivity.this);
                lodingbar.setContentView(R.layout.loading);
                Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(android.icu.lang.UCharacter.JoiningType.TRANSPARENT));
                lodingbar.setCancelable(false);
                lodingbar.show();
                UserModel userModel1 = new UserModel();

                userModel1.uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                userModel1.email = newEmail.getText().toString();
                userModel1.name = name.getText().toString();
                userModel1.type = "Employer";
                Stash.put("type", "Employer");

                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("Users");
                usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid").setValue(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(newEmail.getText().toString());
                usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(name.getText().toString());
                usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("type").setValue("Employer")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Stash.put("employee_user_model", userModel1);
                                Stash.put("employee_name", name.getText().toString());
//                                loadQuestions();
//                                show_data("Account is created successfully", 1);
                                lodingbar.dismiss();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                lodingbar.dismiss();
                                show_data("Something went wrong. Please try again", 0);
                            }
                        });

            }

        });
    }

    // Add this method to set up the TextWatcher
    private void setupAnswerTextWatcher(TextInputEditText textInputEditText, String questionKey) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No implementation needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Save the updated answer to Firebase whenever the text changes
                String updatedAnswer = s.toString().trim();
                saveAnswerToFirebase(questionKey, updatedAnswer);
            }
        });
    }

    // Modify the loadQuestions method to use the setupAnswerTextWatcher method
    private void loadQuestions() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    String questionKey = questionSnapshot.getKey();
                    String questionText = (String) questionSnapshot.child("text").getValue();
                    String answerText = (String) questionSnapshot.child("answer").getValue();

                    // Create TextInputLayout
                    TextInputLayout textInputLayout = (TextInputLayout) LayoutInflater.from(EditProfileActivity.this)
                            .inflate(R.layout.item_question_, questionsLayout, false);
                    textInputLayout.setHint(questionText);

                    // Create TextInputEditText
                    TextInputEditText textInputEditText = textInputLayout.findViewById(R.id.editTextQuestion);
                    textInputEditText.setText(answerText);

                    // Set up TextWatcher for the TextInputEditText
                    setupAnswerTextWatcher(textInputEditText, questionKey);

                    // Add TextInputLayout to LinearLayout
                    questionsLayout.addView(textInputLayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void saveAnswerToFirebase(String questionKey, String updatedAnswer) {
        databaseReference.child(questionKey).child("answer").setValue(updatedAnswer)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            showToast("Answer updated successfully");
                        } else {
//                            showToast("Failed to update answer");
                        }
                    }
                });
    }

    public void show_toast(EditText editText) {
        editText.setText("required");
    }

    public void show_data(String message, int type) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    public void backPress(View view) {
        onBackPressed();
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
