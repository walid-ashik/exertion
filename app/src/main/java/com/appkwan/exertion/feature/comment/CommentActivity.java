package com.appkwan.exertion.feature.comment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.utitlity.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentActivity extends AppCompatActivity implements CommentView {

    private static final String TAG = "CommentActivity";

    @BindView(R.id.mCommentRecyclerView)
    RecyclerView mCommentRecyclerView;
    @BindView(R.id.mCommentEditText)
    EditText mCommentEditText;
    @BindView(R.id.mPostCommentImageViewButton)
    ImageView mPostCommentImageViewButton;
    @BindView(R.id.mNoCommentTextView)
    TextView mNoCommentTextView;

    private CommentPresenter mPresenter;
    private CommentAdapter mAdapter;

    private String mPostId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            mPostId = getIntent().getStringExtra(Constant.POST_ID_INTENT_KEY);
            Log.e(TAG, "post id: " + mPostId);
        }

        mPresenter = new CommentPresenter(this);
        mPresenter.getAllComments(mPostId);

        initRecyclerView();
    }

    @Override
    public void onAllCommentsLoaded(List<Comment> mCommentList) {
        mAdapter = new CommentAdapter(this, mCommentList);
        mCommentRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void noCommentsFound() {
        mNoCommentTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCommentSavedSuccess() {
        mNoCommentTextView.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();

        //remove the comments from edittext when it saved successfully
        mCommentEditText.setText("");
    }

    @Override
    public void onCommentSavingError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.mPostCommentImageViewButton)
    public void onPostCommentButtonClicked(View view) {

        String comment = mCommentEditText.getText().toString().trim();

        if (comment.isEmpty()) {
            Toast.makeText(this, "Write comment first!", Toast.LENGTH_SHORT).show();
            return;
        }

        mPresenter.postNewComment(mPostId, comment);
    }

    private void initRecyclerView() {
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }
}
