package com.moutimid.tinder;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.moutamid.tinder.R;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {
    ImageView logout;
    private BottomNavigationView bottomNavigationView;
    private SmoothBottomBar bottomBar;
    private TextView FragmentTitle;
    private FirebaseAuth mAuth;
    final int PERMISSION_REQUEST_CODE = 112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout);
        bottomBar = findViewById(R.id.bottomBar);
        FragmentTitle = (TextView) findViewById(R.id.FragmentTitle);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.Bottom_view);
        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")) {
                getNotificationPermission();
            }
        }
        Fragment SelectedFragment = null;
        SelectedFragment = new SwipeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, SelectedFragment).commit();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckLogout();
            }
        });

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int id) {
                Fragment SelectedFragment = null;
                if (id == 0) {
                    SelectedFragment = new SwipeFragment();
                    FragmentTitle.setText("Explore");
                } else if (id == 1) {
                    SelectedFragment = new UserListFragment();
                    FragmentTitle.setText("Home");
                } else if (id == 2) {
                    SelectedFragment = new InboxFragment();
                    FragmentTitle.setText("Inbox");
                } else if (id ==3) {
                    SelectedFragment = new ProfileFragment();
                    FragmentTitle.setText("Profile");
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, SelectedFragment).commit();

                return false;
            }
        });

    }




    private void CheckLogout() {
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(MainActivity.this);
        checkAlert.setMessage("Do you want to Logout?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = checkAlert.create();
        alert.setTitle("LogOut");

        alert.show();

    }


    public void getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // allow

                } else {
                    Toast.makeText(this, "Permisssion denied", Toast.LENGTH_SHORT).show();
                }
                return;

        }

    }

}