package com.appkwan.exertion.feature.home;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPresenter  {
    private MainView view;
    private UserInfo userInfoView;

    private final DatabaseReference mRootDataRef;
    private String userId;

    public MainPresenter(MainView view) {
        this.view = view;
        mRootDataRef = FirebaseDatabase.getInstance().getReference().child("Users");
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        checkIfUserInfoSavedInDatabase();

    }

    private void checkIfUserInfoSavedInDatabase() {
        mRootDataRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if( ! dataSnapshot.hasChild("name")){
                    view.navigateToUserInfoActivity();
                }
                if(! dataSnapshot.hasChild("gender")){
                    view.navigateToUserInfoActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    void logoutUser(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
            view.navigateToLoginActivity();
        }
    }
}
