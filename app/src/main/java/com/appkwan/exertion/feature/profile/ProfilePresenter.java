package com.appkwan.exertion.feature.profile;

import android.support.annotation.NonNull;

import com.appkwan.exertion.feature.home.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePresenter {
    ProfileView mView;
    private DatabaseReference mRootRef;
    private String mUserId;
    public ProfilePresenter(ProfileView mView) {
        this.mView = mView;
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void getUserDetails() {
        mRootRef.child(mUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    mView.showUserDetails(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.onError(databaseError.getMessage());
            }
        });
    }
}
