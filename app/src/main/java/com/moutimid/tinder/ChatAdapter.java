package com.moutimid.tinder;// ChatAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.tinder.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private static String currentUserId;

    public ChatAdapter(List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewMessage, receiver_msg;
        private View messageContainer;
        RelativeLayout right_message_container, left_message_container;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            receiver_msg = itemView.findViewById(R.id.receiver_msg);
            textViewMessage = itemView.findViewById(R.id.text_view_message);
            messageContainer = itemView.findViewById(R.id.message_container);
            right_message_container = itemView.findViewById(R.id.right_message_container);
            left_message_container = itemView.findViewById(R.id.left_message_container);
        }

        public void bind(Message message) {
            textViewMessage.setText(message.getMessage());
            receiver_msg.setText(message.getMessage());
            if (message.getSenderId().equals(currentUserId)) {
                right_message_container.setVisibility(View.GONE);
                left_message_container.setVisibility(View.VISIBLE);
            } else {
                right_message_container.setVisibility(View.VISIBLE);
                left_message_container.setVisibility(View.GONE);
            }
        }
    }
}
