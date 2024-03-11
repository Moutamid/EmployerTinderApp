package com.moutimid.tinder.payments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.tinder.R;
import com.moutimid.tinder.MainActivity;
import com.moutimid.tinder.model.UserModel;

import java.util.HashMap;
import java.util.Map;

public class SignupDialogClass extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public TextView yes, no;

    public SignupDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.plan_dailogue);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        yes = (TextView) findViewById(R.id.yes);
        no = (TextView) findViewById(R.id.no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.yes) {
            Stash.put("premium", true);
            UserModel employeeUserModel = (UserModel) Stash.getObject("employee_user_model", UserModel.class);
            Map<String, Object> currentUser = new HashMap<>();
            currentUser.put("name",employeeUserModel.name );
            currentUser.put("email",employeeUserModel.email );
            currentUser.put("type",employeeUserModel.type );
            currentUser.put("uid",employeeUserModel.uid );

            FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    Toast.makeText(c, "Account is created successfully", Toast.LENGTH_SHORT).show();
                    c.startActivity(intent);
                    c.finishAffinity();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        } else if (id == R.id.no) {
            Stash.put("premium", false);

            dismiss();
        }
        dismiss();
    }
}
