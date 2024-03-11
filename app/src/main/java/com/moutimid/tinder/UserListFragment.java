package com.moutimid.tinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.tinder.R;
import com.moutimid.tinder.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends Fragment implements UserAdapter.OnItemClickListener {

    private DatabaseReference usersRef;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserModel> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this);

        recyclerView.setAdapter(userAdapter);

        usersRef = FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("Users");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserModel user = userSnapshot.getValue(UserModel.class);
                    if (userSnapshot.hasChild("name")) {
                        if (user.type.equals("Candidate")) {
                            userList.add(user);
                        }
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("UserListFragment", "loadUsers:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Error loading users", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position) {

        if (!Stash.getBoolean("premium")) {
            CustomDialogClass cdd = new CustomDialogClass(getActivity());
            cdd.show();
        }
        else
        {
            UserModel selectedUser = userList.get(position);
            Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
            Stash.put("user", selectedUser);
            startActivity(intent);
        }


    }
}
