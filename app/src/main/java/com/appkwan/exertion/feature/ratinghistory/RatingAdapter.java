package com.appkwan.exertion.feature.ratinghistory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appkwan.exertion.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {

    private List<Rating> mRatingList;
    private Context context;

    public RatingAdapter(List<Rating> mRatingList, Context context) {
        this.mRatingList = mRatingList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rating_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rating rating = mRatingList.get(position);
        holder.getUserName(rating.getUserId());
        holder.ratingBar.setRating((float) rating.getRate());
    }

    @Override
    public int getItemCount() {
        return mRatingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        DatabaseReference mUserRef;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        }

        public void getUserName(String userId) {
            mUserRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        setUserName(dataSnapshot.child("name").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void setUserName(String name) {
            userName.setText(name);
        }
    }
}
