package com.moutimid.tinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.moutamid.tinder.R;
import com.moutimid.tinder.model.UserModel;

public class ProfileFragment extends Fragment {

    TextView name_txt, name_latter, textView7;
    RelativeLayout delete_data, share, profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        name_txt = view.findViewById(R.id.textView6);
        profile = view.findViewById(R.id.profile);
        delete_data = view.findViewById(R.id.delete_data);
        share = view.findViewById(R.id.share);
        name_latter = view.findViewById(R.id.textView5);
        String userName = Stash.getString("user_name");
        name_txt.setText(userName);
        char c = userName.charAt(0);
        name_latter.setText(c + "");
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));

            }
        });
        delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), WelcomeActivity.class));
                getActivity().finishAffinity();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getActivity().getApplicationContext().getPackageName() + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        String userName = Stash.getString("user_name");
        name_txt.setText(userName);
        char c = userName.charAt(0);
        name_latter.setText(c + "");

        super.onResume();
    }
}
