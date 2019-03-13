package com.appkwan.exertion.feature.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.home.User;
import com.appkwan.exertion.feature.message.model.Message;
import com.appkwan.exertion.feature.utitlity.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private String mMessageThreadId = "";
    private MessageAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        mPresenter = new MessagePresenter(this);
        initRecyclerView();

        if(getIntent().getExtras() != null){
            mPostId = getIntent().getStringExtra(Constant.POST_ID_INTENT_KEY);
            mPostType = getIntent().getStringExtra(Constant.POST_TYPE_KEY);
            mPresenter.getUserId(mPostType, mPostId);
        }
    }

    @Override
    public void onMessagingUserIdLoaded(String messagingUserId) {
        mPresenter.getMessageUserDetaild(messagingUserId);
        mPresenter.getMessageThreadId(messagingUserId);
    }

    @Override
    public void onMessageUserDetailsLoaded(User messagingUser) {
        mUserNameTv.setText(messagingUser.getName());

        Glide.with(this)
                .load(messagingUser.getProfile_image())
                .apply(RequestOptions.placeholderOf(this.getResources().getDrawable(R.drawable.ic_avatar_app)))
                .into(mUserImage);
    }

    @Override
    public void onMessageThreadLoaded(String messageThreadId) {
        this.mMessageThreadId = messageThreadId;
        mPresenter.getMessages(messageThreadId);
    }

    @Override
    public void onMessageThreadNotFound() {
        mMessageRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onMessagesLoaded(List<Message> messageList) {
        mMessageRecyclerView.setVisibility(View.VISIBLE);

        mMessageAdapter = new MessageAdapter(this, messageList, mPresenter);
        mMessageRecyclerView.setAdapter(mMessageAdapter);
        mMessageRecyclerView.scrollToPosition(mMessageRecyclerView.getAdapter().getItemCount() - 1);

    }

    @Override
    public void onMessageSavingError(String message) {
    }

    @Override
    public void onMessageSavedSuccess() {
        mMessageAdapter.notifyDataSetChanged();
        mMessageEditText.setText("");
        mMessageRecyclerView.scrollToPosition(mMessageRecyclerView.getAdapter().getItemCount() - 1);
    }

    @OnClick(R.id.mPostMessageButton)
    public void onMessageSendButtonClicked(){
        String message = mMessageEditText.getText().toString();

        if(message.isEmpty()){
            return;
        }

        mPresenter.saveNewMessage(mMessageThreadId, message);

    }

    private void initRecyclerView() {
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

}
