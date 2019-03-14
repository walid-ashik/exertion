package com.appkwan.exertion.feature.message.allmessage;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.message.model.Message;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AllMessageAdapter extends RecyclerView.Adapter<AllMessageAdapter.ViewHolder> {

    private static final String TAG = "AllMessageAdapter";

    private List<Message> mLastMessageList;
    private Context mContext;

    public AllMessageAdapter(List<Message> mLastMessageList, Context mContext) {
        this.mLastMessageList = mLastMessageList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_last_message_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Message message = mLastMessageList.get(position);

        holder.getUserNameFromDatabase(message.getUserId());
        holder.mLastMessageTextView.setText(message.getMessage());

        Log.e(TAG, "onBindViewHolder: " + message.getUserId());
    }

    @Override
    public int getItemCount() {
        return mLastMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mUserImage)
        CircleImageView mUserImage;
        @BindView(R.id.mUserNameTv)
        TextView mUserNameTv;
        @BindView(R.id.mLastMessageTextView)
        TextView mLastMessageTextView;

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
                    if (dataSnapshot.hasChild("profile_image")) {
                        setUserImage(dataSnapshot.child("profile_image").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void setUserName(String name) {
            mUserNameTv.setText(name);
        }

        private void setUserImage(String imageUrl) {
            Glide.with(mContext)
                    .load(imageUrl)
                    .apply(RequestOptions.placeholderOf(mContext.getResources().getDrawable(R.drawable.ic_avatar_app)))
                    .into(mUserImage);
        }
    }
}
