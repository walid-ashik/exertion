package com.appkwan.exertion.feature.home.fragments.blood;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.comment.CommentActivity;
import com.appkwan.exertion.feature.home.fragments.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class BloodAdapter extends RecyclerView.Adapter<BloodAdapter.ViewHolder> {

    private List<Post> postList;
    private Context context;

    public BloodAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_layout, parent, false);
        return new BloodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = postList.get(position);

        holder.setPostToTextView(post.getPost());
        holder.getUserNameFromDatabase(post.getUser_id());
        holder.setLocation(post.getLocation());
        holder.setBlood(post.getGroup());

        holder.mCommentButton.setOnClickListener( v ->{
            Intent intent = new Intent(context, CommentActivity.class);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        DatabaseReference mUserRef;
        @BindView(R.id.mUserImageView)
        CircleImageView mUserImageView;
        @BindView(R.id.mUserNameTextView)
        TextView mUserNameTextView;
        @BindView(R.id.imageView4)
        ImageView imageView4;
        @BindView(R.id.mLocationTextView)
        TextView mLocationTextView;
        @BindView(R.id.mBloodIconImageView)
        ImageView mBloodIconImageView;
        @BindView(R.id.mBloodTextView)
        TextView mBloodTextView;
        @BindView(R.id.mPostTextVIew)
        TextView mPostTextVIew;
        @BindView(R.id.mCommentButton)
        LinearLayout mCommentButton;
        @BindView(R.id.mSendButton)
        LinearLayout mSendButton;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        }

        public void setPostToTextView(String post) {
            mPostTextVIew.setText(post);
        }

        public void getUserNameFromDatabase(String user_id) {
            mUserRef.child(user_id).addValueEventListener(new ValueEventListener() {
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
            mUserNameTextView.setText(name);
        }

        public void setLocation(String location) {
            mLocationTextView.setText(location);
        }

        public void setBlood(String group) {
            mBloodTextView.setText(group);
        }
    }
}
