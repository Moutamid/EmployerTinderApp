package com.moutimid.tinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.fragment.app.Fragment;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.tinder.R;
import com.moutimid.tinder.model.UserModel;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class SwipeFragment extends Fragment {
    CardStackLayoutManager manager;
    CardStackView cardStackView;
    private List<UserModel> userList;

    // Firebase
    private DatabaseReference usersRef;

    public SwipeFragment() {
        // Required empty public constructor
    }

    public static SwipeFragment newInstance() {
        return new SwipeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Realtime Database reference
        usersRef = FirebaseDatabase.getInstance().getReference().child("TinderEmployeeApp").child("Users");

        // Initialize user list
        userList = new ArrayList<>();

        // Fetch data from Firebase
        fetchDataFromFirebase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_swipe, container, false);
        manager = new CardStackLayoutManager(getContext());
        manager = new CardStackLayoutManager(getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                // Not needed for this implementation
            }

            @Override
            public void onCardSwiped(Direction direction) {

              if (direction == Direction.Right) {
                    // Right swipe
                    showToast("Right Swipe!");
                }
            }

            @Override
            public void onCardRewound() {
                // Not needed for this implementation
            }

            @Override
            public void onCardCanceled() {
                // Not needed for this implementation
            }

            @Override
            public void onCardAppeared(View view, int position) {
                // Not needed for this implementation
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                // Not needed for this implementation
            }
        });
        CardStackAdapter adapter = new CardStackAdapter(getActivity(), userList);
        cardStackView = view.findViewById(R.id.card_stack_view);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());


        return view;
    }

    private void fetchDataFromFirebase() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    if (userModel.type.equals("Candidate")) {
                        userList.add(userModel);
                    }
                }
                if (cardStackView.getAdapter() != null) {
                    cardStackView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    public void like() {
        new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        cardStackView.swipe();
    }

    private void showToast(String message) {
        Intent intent = new Intent(getContext(), UserDetailsActivity.class);
        Stash.put("user", CardStackAdapter.userModel_current);
        startActivity(intent);
    }
}



