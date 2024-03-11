package com.moutimid.tinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
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

public class CandidateDetailsActivity extends AppCompatActivity {
    private LinearLayout questionsLayout;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_details);
        UserModel userModel = (UserModel) Stash.getObject("employee_details_user_model", UserModel.class);

        questionsLayout = findViewById(R.id.questionsLayout);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("Users").child(userModel.uid).child("question");

        // Load questions from Firebase
        loadQuestions();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void backPress(View view) {
        onBackPressed();
    }

    public void back(View view) {
        onBackPressed();
    }
    private void loadQuestions() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    String questionKey = questionSnapshot.getKey();
                    String questionText = (String) questionSnapshot.child("text").getValue();
                    String answerText = (String) questionSnapshot.child("answer").getValue();

                    // Create LinearLayout
                    LinearLayout textInputLayout = (LinearLayout) LayoutInflater.from(CandidateDetailsActivity.this)
                            .inflate(R.layout.employer_details, questionsLayout, false);

                    // Create TextInputEditText
                    TextView question = textInputLayout.findViewById(R.id.question);
                    TextView ans = textInputLayout.findViewById(R.id.ans);
                    question.setText(questionText);
                    ans.setText(answerText);
                    questionsLayout.addView(textInputLayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

}