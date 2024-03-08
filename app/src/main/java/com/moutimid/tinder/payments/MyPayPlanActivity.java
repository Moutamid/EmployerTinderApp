package com.moutimid.tinder.payments;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.moutamid.tinder.R;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.moutamid.tinder.R;
import com.moutimid.tinder.helpers.Config;
import com.moutimid.tinder.helpers.QuickHelp;
import com.moutimid.tinder.model.UserModel;

public class MyPayPlanActivity extends AppCompatActivity {

    private ImageView backImageView;
    private ImageView subscription;
    private TextView txtInfo;
    private TextView activeDate;
    private TextView title;
    private TextView description;
    private RadioButton productListItemRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pay_plana);

        backImageView = findViewById(R.id.backImageView);
        subscription = findViewById(R.id.subscription);
        txtInfo = findViewById(R.id.txtInfo);
        activeDate = findViewById(R.id.active_date);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        productListItemRadio = findViewById(R.id.productListItem_radio);
        UserModel userModel= new UserModel();

//        if (!mCurrentUser.getPayPlan().isEmpty() && mCurrentUser.getPremium()!=null){
        if (userModel.premium!=null){
            txtInfo.setVisibility(View.GONE);
            productListItemRadio.setVisibility(View.VISIBLE);
            activeDate.setVisibility(View.VISIBLE);

            if (userModel.premium){
                String date = String.valueOf(userModel.premium);
                String subString = date.substring(0,10);
                activeDate.setText("Active For: "+subString);
            }else {
                String date = String.valueOf(userModel.premium);
                String subString = date.substring(0,10);
                activeDate.setText("Expired Date:"+subString);
            }

            if (userModel.getPayPlan().equals(Config.PAY_PLAN_ONE_WEEK)){
                title.setText("1 week/5.99 USD");
                description.setText("Unlock messages for one week");

            }else if (userModel.getPayPlan().equals(Config.PAY_PLAN_ONE_MONTH)){
                title.setText("1 month/9.99 USD");
                description.setText("Unlock messages for one month");
            }else if (userModel.getPayPlan().equals(Config.PAY_PLAN_ONE_YEAR)){
                title.setText("1 year/29.99 USD");
                description.setText("Unlock messages for 12 months");
            }
        }else {
            txtInfo.setVisibility(View.VISIBLE);
            productListItemRadio.setVisibility(View.GONE);
            activeDate.setVisibility(View.GONE);
        }

        backImageView.setOnClickListener(v -> {
            finish();
        });

        subscription.setOnClickListener(v -> {
            QuickHelp.goToActivityWithNoClean(MyPayPlanActivity.this, PaymentsActivity.class);
        });
    }
}
