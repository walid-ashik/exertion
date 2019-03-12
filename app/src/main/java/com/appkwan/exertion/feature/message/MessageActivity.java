package com.appkwan.exertion.feature.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.home.User;
import com.appkwan.exertion.feature.utitlity.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity implements MessageView{

    @BindView(R.id.mBackImageButton)
    ImageButton mBackImageButton;
    @BindView(R.id.mUserImage)
    CircleImageView mUserImage;
    @BindView(R.id.mUserNameTv)
    TextView mUserNameTv;
    @BindView(R.id.mNoCommentTextView)
    TextView mNoCommentTextView;
    @BindView(R.id.mMessageRecyclerView)
    RecyclerView mMessageRecyclerView;
    @BindView(R.id.mMessageEditText)
    EditText mMessageEditText;
    @BindView(R.id.mPostMessageButton)
    ImageView mPostMessageButton;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;

    private MessagePresenter mPresenter;

    private String mPostId;
    private String mPostType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        mPresenter = new MessagePresenter(this);

        if(getIntent().getExtras() != null){
            mPostId = getIntent().getStringExtra(Constant.POST_ID_INTENT_KEY);
            mPostType = getIntent().getStringExtra(Constant.POST_TYPE_KEY);
            mPresenter.getUserId(mPostType, mPostId);
        }
    }

    @Override
    public void onMessagingUserIdLoaded(String messagingUserId) {
        mPresenter.getMessageUserDetaild(messagingUserId);
    }

    @Override
    public void onMessageUserDetailsLoaded(User messagingUser) {
        mUserNameTv.setText(messagingUser.getName());

        Glide.with(this)
                .load(messagingUser.getProfile_image())
                .apply(RequestOptions.placeholderOf(this.getResources().getDrawable(R.drawable.ic_avatar_app)))
                .into(mUserImage);
    }
}
