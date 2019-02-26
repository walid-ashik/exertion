package com.appkwan.exertion.feature.comment;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentPresenter {

    private String userId = "";
    private CommentView mView;

    private FirebaseAuth mAuth;
    private DatabaseReference mCommentRootRef;
    private List<Comment> mCommentList = new ArrayList<>();

    public CommentPresenter(CommentView mView) {
        this.mView = mView;
        mAuth = FirebaseAuth.getInstance();
        mCommentRootRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public void getAllComments(String mPostId) {

        mCommentRootRef.child(mPostId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    mCommentList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Comment comment = snapshot.getValue(Comment.class);
                        mCommentList.add(comment);
                    }
                    mView.onAllCommentsLoaded(mCommentList);
                } else {
                    mView.noCommentsFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void postNewComment(String mPostId, String comment) {

        Map map = new HashMap();
        map.put("comment", comment);
        map.put("userId", userId);

        String pushKey = mCommentRootRef.push().getKey();

        mCommentRootRef.child(mPostId).child(pushKey).updateChildren(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mView.onCommentSavedSuccess();
                    } else {
                        mView.onCommentSavingError(task.getException().getMessage());
                    }
                });
    }
}
