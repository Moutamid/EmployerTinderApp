package com.moutimid.tinder;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.makeramen.roundedimageview.RoundedImageView;
import com.moutamid.tinder.R;
import com.moutimid.tinder.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.GalleryPhotosViewHolder> {
    Context ctx;
    private List<UserModel> userList;
    private GestureDetector gestureDetector;
    public static int pos;
    public static UserModel userModel_current;

    public CardStackAdapter(Context ctx, List<UserModel> userList) {
        this.ctx = ctx;
        this.userList = userList;
    }

    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_spot, parent, false);
        gestureDetector = new GestureDetector(ctx, new MyGestureListener());
        return new GalleryPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        UserModel userModel = userList.get(position);
        userModel_current = userModel;
        holder.item_name.setText(userModel.name);
        Picasso.get().load(userModel.profile_img).into(holder.item_image);
        holder.itemView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            pos = position;
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {

        TextView item_name;
        RoundedImageView item_image;

        public GalleryPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_image = itemView.findViewById(R.id.item_image);
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                    return true;
                }
            }
            return false;
        }

        private void onSwipeRight() {
            showToast("Right Swipe!");
        }

        private void onSwipeLeft() {
            showToast("Left Swipe!");
        }

        private void showToast(String message) {
            UserModel selectedUser = userList.get(pos);

            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
        }
    }
}
