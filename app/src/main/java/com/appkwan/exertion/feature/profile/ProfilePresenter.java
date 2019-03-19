package com.appkwan.exertion.feature.profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appkwan.exertion.feature.home.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ProfilePresenter {

    private static final String TAG = "ProfilePresenter";

    ProfileView mView;
    private DatabaseReference mUserRef;
    public ProfilePresenter(ProfileView mView) {
        this.mView = mView;
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void getUserDetails(String userId) {
        mUserRef.child(userId).addValueEventListener(new ValueEventListener() {
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

    public void saveUserUploadedImageDownloadUrl(String imageUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserRef.child(userId)
                .child("profile_image")
                .setValue(imageUrl)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        mView.onImageUrlSavedSuccess();
                    }
                });
    }

    public void saveUserCvDownloadUrl(String cvUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserRef.child(userId)
                .child("cv")
                .setValue(cvUrl)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        mView.onCvUrlSavedSuccess();
                    }
                });
    }

    public void getRating() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserRef.child(userId)
                .child("Rating")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            int ratingCount = 0;
                            int rating = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                String rate = snapshot.child("rate").getValue().toString();
                                rating += Integer.valueOf(rate);
                                ratingCount++;
                            }
                            calculateRating(rating, ratingCount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void calculateRating(int rating, int ratingCount) {
        mView.onRatingLoaded((double) rating / ratingCount, ratingCount);
    }

    public void checkIfOtherUserRatedThisPerson(String mOtherUserId) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = mUserRef.child(userId)
                .child("Rating")
                .child(mOtherUserId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mView.otherUserRatedThisUser(true);
                }else{
                    mView.otherUserRatedThisUser(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setRating(String mOtherUserId, int rating) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserRef.child(userId)
                .child("Rating")
                .child(mOtherUserId)
                .child("rate")
                .setValue(rating)
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       mView.onRatingSuccess();
                   }else{
                       mView.onRatingError(task.getException().getMessage());
                   }
                });
    }
}
