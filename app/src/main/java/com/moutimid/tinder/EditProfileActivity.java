package com.moutimid.tinder;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.tinder.R;
import com.moutimid.tinder.model.UserModel;

import java.util.Objects;


public class EditProfileActivity extends AppCompatActivity {
    public static final String TAG = EditProfileActivity.class.getSimpleName();
    private Button signOut;
    private EditText name, newEmail, company_name, bussiness_address, practice_time, hirings, phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        signOut = (Button) findViewById(R.id.sign_out);
        UserModel userModel = (UserModel) Stash.getObject("employee_user_model", UserModel.class);
        company_name = (EditText) findViewById(R.id.company_name);
        bussiness_address = (EditText) findViewById(R.id.bussiness_address);
        practice_time = (EditText) findViewById(R.id.practice_time);
        hirings = (EditText) findViewById(R.id.hirings);
        phone_number = (EditText) findViewById(R.id.phone_number);

        newEmail = (EditText) findViewById(R.id.new_email);
        name = (EditText) findViewById(R.id.name);
        name.setText(userModel.name);
        company_name.setText(userModel.company_name);
        bussiness_address.setText(userModel.bussiness_address);
        practice_time.setText(userModel.practice_time);
        hirings.setText(userModel.hirings);
        phone_number.setText(userModel.phone_number);
        newEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_str = name.getText().toString().trim();

                String company_name_str = company_name.getText().toString().trim();
                String bussiness_address_str = bussiness_address.getText().toString().trim();
                String practice_time_str = practice_time.getText().toString().trim();
                String hirings_str = hirings.getText().toString().trim();
                String phone_number_str = phone_number.getText().toString().trim();
                if (TextUtils.isEmpty(name_str)) {
                    show_toast(name);
                    return;
                }


                if (TextUtils.isEmpty(company_name_str)) {
                    show_toast(company_name);
                    return;
                }
                if (TextUtils.isEmpty(bussiness_address_str)) {
                    show_toast(bussiness_address);
                    return;
                }
                if (TextUtils.isEmpty(practice_time_str)) {
                    show_toast(practice_time);
                    return;
                }
                if (TextUtils.isEmpty(phone_number_str)) {
                    show_toast(phone_number);
                    return;
                }

                Dialog lodingbar = new Dialog(EditProfileActivity.this);
                lodingbar.setContentView(R.layout.loading);
                Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
                lodingbar.setCancelable(false);
                lodingbar.show();
                UserModel userModel1 = new UserModel();
                userModel1.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                userModel1.email = newEmail.getText().toString();
                userModel1.name = name.getText().toString();
                userModel1.type = "Employer";
                userModel1.company_name = company_name.getText().toString();
                userModel1.bussiness_address = bussiness_address.getText().toString();
                userModel1.practice_time = practice_time.getText().toString();
                userModel1.hirings = hirings.getText().toString();
                userModel1.phone_number = phone_number.getText().toString();
                Stash.put("type", "Employer");
                FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userModel1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Stash.put("employee_user_model", userModel1);
                        Stash.put("employee_name", name.getText().toString());
                        show_data("Account is created successfully", 1);
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

    public void show_toast(EditText editText) {
        editText.setText("required");
    }

    public void show_data(String message, int type) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    public void backPress(View view) {
        onBackPressed();
    }
}