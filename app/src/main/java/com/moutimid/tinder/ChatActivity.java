package com.moutimid.tinder;
// ChatActivity.java

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.tinder.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText editTextMessage;
    private Button buttonSend;
    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;
    private DatabaseReference messagesRef;
    private String selectedUserId;
    private String name_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        name_str = getIntent().getStringExtra("name");
        TextView name = findViewById(R.id.name);
        name.setText(name_str);

        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);
        recyclerViewChat = findViewById(R.id.recycler_view_chat);

        recyclerViewChat.setHasFixedSize(true);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));

        selectedUserId = getIntent().getStringExtra("selected_user_id");
        Log.d("dataa", selectedUserId + "");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        messagesRef = firebaseDatabase.getReference().child("TinderEmployeeApp").child("messages").child(currentUser.getUid()).child(selectedUserId);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList, FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerViewChat.setAdapter(chatAdapter);

        loadMessages();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void loadMessages() {
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                Message message = dataSnapshot.getValue(Message.class);
                messageList.add(message);
                chatAdapter.notifyDataSetChanged();
                recyclerViewChat.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();

        if (!TextUtils.isEmpty(messageText)) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference newMessageRef = messagesRef.push();
                String messageId = newMessageRef.getKey();

                Message message = new Message(currentUserId, selectedUserId, messageText, false);

                Map<String, Object> messageValues = message.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/" + currentUserId + "/" + selectedUserId + "/" + messageId, messageValues);
                childUpdates.put("/" + selectedUserId + "/" + currentUserId + "/" + messageId, messageValues);

                Map<String, Object> lastMessageUpdates = new HashMap<>();
                lastMessageUpdates.put("/" + currentUserId + "/" + selectedUserId + "/lastMessage", messageText);
                lastMessageUpdates.put("/" + selectedUserId + "/" + currentUserId + "/lastMessage", messageText);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference().child("TinderEmployeeApp").child("messages").updateChildren(childUpdates);
                firebaseDatabase.getReference().child("TinderEmployeeApp").child("lastMessage").updateChildren(lastMessageUpdates);

                editTextMessage.setText("");
            }
        } else {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
