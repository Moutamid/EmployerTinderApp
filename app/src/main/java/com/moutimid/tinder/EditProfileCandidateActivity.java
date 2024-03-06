package com.moutimid.tinder;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.tinder.R;
import com.moutimid.tinder.model.UserModel;

import java.util.Objects;


public class EditProfileCandidateActivity extends AppCompatActivity {
    public static final String TAG = EditProfileCandidateActivity.class.getSimpleName();
    private Button signOut;
    private EditText name, newEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_edit_profile);
        signOut = (Button) findViewById(R.id.sign_out);
        UserModel userModel = (UserModel) Stash.getObject("user", UserModel.class);
        newEmail = (EditText) findViewById(R.id.new_email);
        name = (EditText) findViewById(R.id.name);
        name.setText(userModel.name);
        newEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newEmail.getText().toString().isEmpty() || name.getText().toString().isEmpty()) {
                    Toast.makeText(EditProfileCandidateActivity.this, "Please enter name /email", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Dialog lodingbar = new Dialog(EditProfileCandidateActivity.this);
                    lodingbar.setContentView(R.layout.loading);
                    Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
                    lodingbar.setCancelable(false);
                    lodingbar.show();
                    FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(name.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(newEmail.getText().toString());
                    Stash.put("user_name", name.getText().toString());
                    lodingbar.dismiss();
                    finish();


                }
            }
        });
    }


    public void backPress(View view) {
        onBackPressed();
    }
}