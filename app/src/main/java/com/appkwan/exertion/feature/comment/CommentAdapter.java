package com.appkwan.exertion.feature.comment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List<Comment> mCommentList;

    public CommentAdapter(Context context, List<Comment> mCommentList) {
        this.context = context;
        this.mCommentList = mCommentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = mCommentList.get(position);

        holder.mCommentText.setText(comment.getComment());
        holder.getUserNameFromDatabase(comment.getUserId());
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mUserImage)
        CircleImageView mUserImage;
        @BindView(R.id.mUserName)
        TextView mUserName;
        @BindView(R.id.mCommentText)
        TextView mCommentText;

        DatabaseReference mUserRef;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

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
            mUserName.setText(name);
        }

    }
}
