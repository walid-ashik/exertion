package com.appkwan.exertion.feature.message;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.appkwan.exertion.feature.home.User;
import com.appkwan.exertion.feature.message.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagePresenter {

    private static final String TAG = "MessagePresenter";

    private MessageView mView;

    private final DatabaseReference mUserRef, mRootRef;
    private final FirebaseAuth mAuth;
    private String thisUserId;
    private String mMessagingUserId = "";
    private List<Message> messageList = new ArrayList<>();

    public MessagePresenter(MessageView mView) {
        this.mView = mView;
        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        thisUserId = mAuth.getCurrentUser().getUid();
    }


    public void getUserId(String postType, String postId) {
        mRootRef.child(postType)
                .child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("user_id")) {
                            mView.onMessagingUserIdLoaded(dataSnapshot.child("user_id").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getMessageUserDetaild(String messagingUserId) {
        mUserRef.child(messagingUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            User messageingUser = dataSnapshot.getValue(User.class);
                            mView.onMessageUserDetailsLoaded(messageingUser);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getMessageThreadId(String messagingUserId) {

        this.mMessagingUserId = messagingUserId;

        mUserRef.child(messagingUserId)
                .child("Message")
                .child(thisUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String messageThreadId = snapshot.child("messageThreadId").getValue().toString();
                                mView.onMessageThreadLoaded(messageThreadId);
                            }
                        } else {
                            mView.onMessageThreadNotFound();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getMessages(String messageThreadId) {

        mRootRef.child("Message")
                .child(messageThreadId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        messageList.clear();

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Message message = snapshot.getValue(Message.class);
                                messageList.add(message);
                            }
                        } else {
                            mView.onMessageThreadNotFound();
                        }

                        mView.onMessagesLoaded(messageList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void saveNewMessage(String messageThreadId, String message) {
        String newMessageThreadId;
        if (messageThreadId.isEmpty()) {
            newMessageThreadId = mRootRef.child(thisUserId).child("Message").push().getKey();
            saveNewMessageThread(newMessageThreadId);
            saveMessageToDatabase(newMessageThreadId, message);
        } else {
            newMessageThreadId = messageThreadId;
            saveMessageToDatabase(newMessageThreadId, message);
        }
    }

    private void saveMessageToDatabase(String messageThreadId, String message) {

        Map map = new HashMap();
        map.put("isSeen", false);
        map.put("message", message);
        map.put("timestamp", System.currentTimeMillis());
        map.put("userId", thisUserId);

        String messageKey = mRootRef.child("Message").child(messageThreadId).push().getKey();

        mRootRef.child("Message")
                .child(messageThreadId)
                .child(messageKey)
                .setValue(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.e(TAG, "saveMessageToDatabase: message saved successfully: " + message);
                        mView.onMessageSavedSuccess();
                    } else {
                        Log.e(TAG, "saveMessageToDatabase: error saving message: " + task.getException().getMessage());
                        mView.onMessageSavingError(task.getException().getMessage());
                    }
                });
    }

    private void saveNewMessageThread(String newMessageThreadId) {
        saveMessageThreadUnderUser(newMessageThreadId, thisUserId, mMessagingUserId);
        saveMessageThreadUnderUser(newMessageThreadId, mMessagingUserId, thisUserId);
    }

    private void saveMessageThreadUnderUser(String newMessageThreadId, String thisUserId, String messagingUserId) {
        mUserRef.child(messagingUserId)
                .child("Message")
                .child(thisUserId)
                .child(newMessageThreadId)
                .child("messageThreadId")
                .setValue(newMessageThreadId);
    }
}
