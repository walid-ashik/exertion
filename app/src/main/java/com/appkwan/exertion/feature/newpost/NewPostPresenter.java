package com.appkwan.exertion.feature.newpost;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.appkwan.exertion.feature.home.User;
import com.appkwan.exertion.feature.home.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewPostPresenter implements UserInfo {

    private String userId;
    private NewPostView view;
    private final DatabaseReference mRootDataRef;
    private DatabaseReference mRootUserDataRef;
    private User user;

    public NewPostPresenter(NewPostView view) {
        this.view = view;
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRootUserDataRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mRootDataRef = FirebaseDatabase.getInstance().getReference();
    }

    void getUserName(){

        mRootUserDataRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("name")){
                    view.showUserName(dataSnapshot.child("name").getValue().toString());
                }
                if(dataSnapshot.hasChild("profile_image")){
                    view.loadUserImage(dataSnapshot.child("profile_image").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void saveThePostToDataBase(String mPostType, String post, String location, String mBloodGroup) {

        view.showLoader();

        Map map = new HashMap();
        map.put("post", post);
        map.put("location", location);
        map.put("query_location", location.toLowerCase());
        map.put("group", mBloodGroup);
        map.put("query_group", mBloodGroup.toLowerCase());
        map.put("user_id", userId);

        String pushKey = mRootDataRef.push().getKey();

        mRootDataRef.child(mPostType).child(pushKey).updateChildren(map).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                view.hideLoader();
                view.onPostSuccess();
            }else{
                view.hideLoader();
                view.onPostDatabaseError(task.getException().getMessage());
            }
        });

    }

    @Override
    public void onUserInfoSucces(User user) {
        this.user = user;
    }
}
