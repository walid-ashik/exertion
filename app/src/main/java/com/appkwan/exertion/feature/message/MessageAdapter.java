package com.appkwan.exertion.feature.message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.message.model.Message;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int THIS_USER_VIEW_TYPE = 1;
    private static final int MESSAGING_USER_TYPE = 2;

    private Context mContext;
    private List<Message> mMessageList;
    private String mThisUserId;
    private MessagePresenter mPresenter;

    public MessageAdapter(Context mContext, List<Message> mMessageList, MessagePresenter mPresenter) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
        this.mPresenter = mPresenter;
        mThisUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == THIS_USER_VIEW_TYPE) {
            View thisUserMessageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_this_user_message_layout, parent, false);
            return new ThisUserViewHolder(thisUserMessageView);
        } else {
            View messagingUserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mesaging_user_layout, parent, false);
            return new MessagingUserViewHolder(messagingUserView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = mMessageList.get(position);

        if (holder instanceof ThisUserViewHolder) {
            ThisUserViewHolder thisUserViewHolder = (ThisUserViewHolder) holder;
            thisUserViewHolder.mMessageText.setText(message.getMessage());
        }
        if (holder instanceof MessagingUserViewHolder) {
            MessagingUserViewHolder messagingUserViewHolder = (MessagingUserViewHolder) holder;
            messagingUserViewHolder.mMessageText.setText(message.getMessage());
            messagingUserViewHolder.setUserImage(messagingUserViewHolder.mMessagingUserImage, message.getUserId());
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (mMessageList.get(position).getUserId().equals(mThisUserId)) {
            return THIS_USER_VIEW_TYPE;
        } else {
            return MESSAGING_USER_TYPE;
        }

    }

    public class ThisUserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mCommentText)
        TextView mMessageText;

        public ThisUserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class MessagingUserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mMessagingUserImage)
        CircleImageView mMessagingUserImage;
        @BindView(R.id.mCommentText)
        TextView mMessageText;

        DatabaseReference mUserRef;

        public MessagingUserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        }

        public void setUserImage(CircleImageView mMessagingUserImage, String userId) {
            mUserRef.child(userId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("profile_image")) {

                                Glide.with(mContext)
                                        .load(dataSnapshot.child("profile_image").getValue().toString())
                                        .apply(RequestOptions.placeholderOf(mContext.getResources().getDrawable(R.drawable.ic_avatar_app)))
                                        .into(mMessagingUserImage);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }
}
