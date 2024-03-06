package com.moutimid.tinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.moutamid.tinder.R;
import com.moutimid.tinder.model.UserModel;
import com.squareup.picasso.Picasso;

public class UserDetailsActivity extends AppCompatActivity {
    private RelativeLayout pBody;
    private ImageView backArrow;
    private TextView pageTitle, productName, edtEmail, edtPhone, iAm, time, modalities, population;
    private ImageView userImage, resume;

    private RelativeLayout chatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        pBody = findViewById(R.id.PBody);
        backArrow = findViewById(R.id.backArrow);
        pageTitle = findViewById(R.id.PageTitle);
        productName = findViewById(R.id.name);
        edtEmail = findViewById(R.id.edtEmail);
//        edtPhone = findViewById(R.id.edtPhone);
        iAm = findViewById(R.id.i_am);
        time = findViewById(R.id.time);
        modalities = findViewById(R.id.modalities);
        population = findViewById(R.id.population);
        userImage = findViewById(R.id.user_image);
        resume = findViewById(R.id.resume);
        chatBtn = findViewById(R.id.chat_btn);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        UserModel user = (UserModel) Stash.getObject("user", UserModel.class);
        productName.setText(user.name);
        edtEmail.setText(user.email);
//        edtPhone.setText(user.phone);
        iAm.setText(user.selectedText);
        time.setText(user.time_in_fields);
        modalities.setText(user.modalities);
        population.setText(user.population);
        Picasso.get().load(user.profile_img).into(userImage);
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDetailsActivity.this, PdfViewerActivity.class));
                finish();
            }
        });
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDetailsActivity.this, ChatActivity.class);
               intent.putExtra("selected_user_id", user.uid);
               intent.putExtra("name", user.name);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserModel user = (UserModel) Stash.getObject("user", UserModel.class);
        productName.setText(user.name);
        edtEmail.setText(user.email);
//        edtPhone.setText(user.phone);
        iAm.setText(user.selectedText);
        time.setText(user.time_in_fields);
        modalities.setText(user.modalities);
        population.setText(user.population);
        Picasso.get().load(user.profile_img).into(userImage);
    }

    public void backPress(View view) {
        onBackPressed();
    }
}