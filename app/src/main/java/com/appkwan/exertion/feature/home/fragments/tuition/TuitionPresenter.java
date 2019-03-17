package com.appkwan.exertion.feature.home.fragments.tuition;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appkwan.exertion.feature.home.fragments.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TuitionPresenter {

    private static final String TAG = "TuitionPresenter";

    private TuitionView view;
    private DatabaseReference mRootDataRef;
    private List<Post> postList;

    public TuitionPresenter(TuitionView view) {
        this.view = view;
        postList = new ArrayList<>();
        mRootDataRef = FirebaseDatabase.getInstance().getReference().child("Tuition");
    }

    void getAllTuitionPosts() {
        mRootDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    post.setPostId(snapshot.getKey());
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

    public void querySearch(String searchChild, String queryLocation) {
        Query query = mRootDataRef.orderByChild(searchChild)
                .startAt(queryLocation)
                .endAt(queryLocation + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();

                if(! dataSnapshot.exists()){
                    view.onPostLoadingError("");
                    return;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    post.setPostId(snapshot.getKey());
                    postList.add(post);
                }
                view.onPostLoaded(postList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
