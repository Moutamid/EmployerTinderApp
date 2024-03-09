package com.moutimid.tinder;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.moutamid.tinder.R;
import com.moutimid.tinder.helpers.QuickHelp;


public class WelcomeActivity extends AppCompatActivity {

    BottomSheetDialog sheetDialog;
    private TextView mTermsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        mTermsText = findViewById(R.id.textRegistrationTerms);

//        setmTermsText(mTermsText);

        findViewById(R.id.buttonLogin).setOnClickListener(view -> {
            QuickHelp.goToActivityWithNoClean(WelcomeActivity.this, LoginActivity.class);
        });
        findViewById(R.id.buttonRegister).setOnClickListener(view -> {
            registerSheetDialoge();
        });

    }


    public void registerSheetDialoge() {
        sheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        sheetDialog.setContentView(R.layout.register_bottom_sheet);
        sheetDialog.setCancelable(true);
        sheetDialog.setCanceledOnTouchOutside(true);
        Button findRoomButton = sheetDialog.findViewById(R.id.findRoomButton);
        LinearLayout listRoomButton = sheetDialog.findViewById(R.id.listRoomButton);
        if (listRoomButton != null) {
            listRoomButton.setOnClickListener(v -> {
                if (sheetDialog.isShowing()) {
                    sheetDialog.dismiss();
                    QuickHelp.goToActivityWithNoClean(WelcomeActivity.this, SignupActivityEmployee.class);
                }
            });
        }
        if (findRoomButton != null) {
            findRoomButton.setOnClickListener(v -> {
                if (sheetDialog.isShowing()) {
                    sheetDialog.dismiss();
                    QuickHelp.goToActivityWithNoClean(WelcomeActivity.this, SignupActivity.class);
                }
            });
        }
        if (sheetDialog != null && !sheetDialog.isShowing()) {
            sheetDialog.show();
        }
    }
}
