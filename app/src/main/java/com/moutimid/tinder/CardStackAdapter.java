package com.moutimid.tinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.tinder.R;


public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.GalleryPhotosViewHolder> {


    Context ctx;
    String name;

    public CardStackAdapter(Context ctx, String name) {
        this.ctx = ctx;
        this.name = name;
    }

    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_spot, parent, false);
        return new GalleryPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
         holder.item_name.setText(name);

    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {

        TextView item_name;

        public GalleryPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
              }
    }
}
