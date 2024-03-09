package com.moutimid.tinder;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.moutamid.tinder.R;
import com.moutimid.tinder.model.UserModel;

public class CandidateDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_details);
        UserModel userModel = (UserModel) Stash.getObject("employee_details_user_model", UserModel.class);
        TextView nameTextView = findViewById(R.id.name);
        TextView companyNameTextView = findViewById(R.id.company_name);
        TextView businessAddressTextView = findViewById(R.id.bussiness_address);
        TextView practiceTextView = findViewById(R.id.practice);
        TextView hiringTextView = findViewById(R.id.hiring);
        TextView phoneNumberTextView = findViewById(R.id.phone_number);
        TextView emailAddressTextView = findViewById(R.id.email_address);
        nameTextView.setText(userModel.name);
        companyNameTextView.setText(userModel.company_name);
        businessAddressTextView.setText(userModel.bussiness_address);
        practiceTextView.setText(userModel.practice_time);
        hiringTextView.setText(userModel.hirings);
        phoneNumberTextView.setText(userModel.phone_number);
        emailAddressTextView.setText(userModel.email);
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
}