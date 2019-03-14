package com.appkwan.exertion.feature.message.allmessage;

import android.support.annotation.NonNull;

import com.appkwan.exertion.feature.message.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllMessagePresenter {

    private AllMessageView mView;
    private DatabaseReference mUserRef;
    private ArrayList<String> mMessagedUserList;
    private ArrayList<String> mMessageThreadList;
    private List<Message> mMessageList;
    private String userId;

    public AllMessagePresenter(AllMessageView mView) {
        this.mView = mView;
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mMessagedUserList = new ArrayList<>();
        mMessageThreadList = new ArrayList<>();
        mMessageList = new ArrayList<>();

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void getMessagedUserList(String mUserId) {
        mUserRef.child(mUserId)
                .child("Message")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mMessagedUserList.clear();

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                mMessagedUserList.add(snapshot.getKey());
                            }
                        }

                        mView.onMessagedUserListLoaded(mMessagedUserList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getMessageThreadList(String messagedUserId) {
        mUserRef.child(userId)
                .child("Message")
                .child(messagedUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                mMessageThreadList.add(snapshot.getKey());
                            }
                            mView.onMessageThreadAdded(mMessageThreadList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getLastMessage(Query query) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Message message = snapshot.getValue(Message.class);
                        mView.onLastMessageLoaded(message);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
