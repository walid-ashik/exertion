package com.appkwan.exertion.feature.message;

import android.support.annotation.NonNull;

import com.appkwan.exertion.feature.home.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessagePresenter {

    private MessageView mView;

    private final DatabaseReference mUserRef, mRootRef;
    private final FirebaseAuth mAuth;

    public MessagePresenter(MessageView mView) {
        this.mView = mView;
        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }


    public void getUserId(String postType, String postId) {
        mRootRef.child(postType)
                .child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("user_id")){
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
                        if(dataSnapshot.exists()){
                            User messageingUser = dataSnapshot.getValue(User.class);
                            mView.onMessageUserDetailsLoaded(messageingUser);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
