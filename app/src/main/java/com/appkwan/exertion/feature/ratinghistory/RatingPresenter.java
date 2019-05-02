package com.appkwan.exertion.feature.ratinghistory;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RatingPresenter {

    private RatingView view;
    private List<Rating> mRatingList = new ArrayList<>();

    RatingPresenter(RatingView view){
        this.view = view;

    }

    public void getRatings(DatabaseReference mRatingRef) {
        mRatingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Rating rating = snapshot.getValue(Rating.class);
                        rating.setUserId(snapshot.getKey());
                        mRatingList.add(rating);
                    }
                    view.onRatingLoaded(mRatingList);

                }else{
                    view.showEmptyView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
