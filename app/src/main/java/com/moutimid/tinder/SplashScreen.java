package com.moutimid.tinder;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.moutamid.tinder.R;
import com.moutimid.tinder.helpers.QuickHelp;
import com.moutimid.tinder.payments.SignupDialogClass;


public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        int splashInterval = 3000;
        new Handler().postDelayed(this::goToApp, splashInterval);
    }

    public void goToApp() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String type = Stash.getString("type");
            Log.d("dtaa", type + " test");
            if (type.equals("Employer")) {
//                if (!Stash.getBoolean("premium")) {
//                    SignupDialogClass cdd = new SignupDialogClass(SplashScreen.this);
//                    cdd.show();
//                } else {
                    QuickHelp.goToActivityAndFinish(this, MainActivity.class);
//                }
            } else {
                QuickHelp.goToActivityAndFinish(this, HomePage.class);
            }

        } else {
            QuickHelp.goToActivityAndFinish(this, WelcomeActivity.class);
        }
    }
}