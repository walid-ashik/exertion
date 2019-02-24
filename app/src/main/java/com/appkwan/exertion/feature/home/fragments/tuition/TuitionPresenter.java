package com.appkwan.exertion.feature.home.fragments.tuition;

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

public class TuitionPresenter {

    private static final String TAG = "TuitionPresenter";

    private TuitionView view;
    private DatabaseReference mRootDataRef;
    private List<Post> postList = new ArrayList<>();

    public TuitionPresenter(TuitionView view) {
        this.view = view;
        mRootDataRef = FirebaseDatabase.getInstance().getReference();
    }

    void getAllTuitionPosts(){
        mRootDataRef.child("Tuition").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Post post = snapshot.getValue(Post.class);
                        Log.e(TAG, "onDataChange: " +post.getPost());
                        Log.e(TAG, "onDataChange: " +post.getLocation());
                        postList.add(post);
                        view.onPostLoaded(postList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
