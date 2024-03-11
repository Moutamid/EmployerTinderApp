package com.moutimid.tinder.Admin;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.tinder.R;
import com.moutimid.tinder.Admin.Adapter.QuestionAdapter;
import com.moutimid.tinder.Admin.Model.Question;
import com.moutimid.tinder.LoginActivity;
import com.moutimid.tinder.WelcomeActivity;
import com.moutimid.tinder.helpers.QuickHelp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private List<Question> questions = new ArrayList<>();
    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    private EditText editTextQuestion;
    private int editPosition = -1; // To keep track of the question being edited
    private DatabaseReference databaseReference;
    TextView all_questions;
    Dialog lodingbar;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        logout = findViewById(R.id.logout);
        all_questions = findViewById(R.id.all_questions);
        editTextQuestion = findViewById(R.id.editTextQuestion);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        recyclerView = findViewById(R.id.recyclerViewQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuestionAdapter(MainActivity.this, questions);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("questions");
        lodingbar = new Dialog(MainActivity.this);
        lodingbar.setContentView(R.layout.loading);
        Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
        lodingbar.setCancelable(false);
        lodingbar.show();
        // Load questions from Firebase
        loadQuestions();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stash.put("admin_login", false);
                QuickHelp.goToActivityAndFinish(MainActivity.this, WelcomeActivity.class);

            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionText = editTextQuestion.getText().toString().trim();
                if (!questionText.isEmpty()) {
                    if (editPosition == -1) {
                        addQuestion(questionText);
                    } else
                    {
                        editQuestion(editPosition, questionText);
                        editPosition = -1;
                        }
                    editTextQuestion.setText("");
                }
            }
        });

        // Item click listener for editing questions
        adapter.setOnItemClickListener(new QuestionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Question question = questions.get(position);
                editTextQuestion.setText(question.getText());
                editPosition = position;
            }
        });

        // Item long click listener for deleting questions
        adapter.setOnItemLongClickListener(new QuestionAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                deleteQuestion(position);
            }
        });
    }

    private void addQuestion(String questionText) {
        // Generate a unique key for the question
        String key = databaseReference.push().getKey();
        Question question = new Question(questionText);
        databaseReference.child(key).setValue(question);
    }

    private void editQuestion(int position, String newText) {
        Question question = questions.get(position);
        question.setText(newText);
        databaseReference.child(question.getKey()).setValue(question).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                lodingbar.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                lodingbar.dismiss();

            }
        });
    }

    private void deleteQuestion(int position) {
        Question question = questions.get(position);
        databaseReference.child(question.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                lodingbar.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                lodingbar.dismiss();

            }
        });
    }

    private void loadQuestions() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questions.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    if (question != null) {
                        question.setKey(snapshot.getKey());
                        questions.add(question);
                    }
                }
                if (questions.size() > 0) {
                    all_questions.setVisibility(View.VISIBLE);
                } else {
                    all_questions.setVisibility(View.GONE);

                }
                lodingbar.dismiss();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                lodingbar.dismiss();
            }
        });
    }
}
