package com.appkwan.exertion.feature.home.fragments.blood;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appkwan.exertion.feature.home.fragments.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BloodPresenter {

    private static final String TAG = "BloodPresenter";

    private BloodView view;
    private DatabaseReference mRootDataRef;
    private List<Post> postList;

    public BloodPresenter(BloodView view) {
        this.view = view;
        postList = new ArrayList<>();
        mRootDataRef = FirebaseDatabase.getInstance().getReference().child("Blood");
    }

    void getAllTuitionPosts() {
        mRootDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    Log.e(TAG, "onDataChange: " + post.getPost());
                    Log.e(TAG, "onDataChange: " + post.getLocation());
                    postList.add(post);
                    view.onPostLoaded(postList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                view.onPostLoadingError(databaseError.getMessage());
            }
        });
    }

}