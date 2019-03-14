package com.appkwan.exertion.feature.message.allmessage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.message.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllMessageActivity extends AppCompatActivity implements AllMessageView {

    private static final String TAG = "AllMessageActivity";

    @BindView(R.id.mAllMessageRecyclerView)
    RecyclerView mAllMessageRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AllMessagePresenter mPresenter;
    private String mUserId;
    private boolean isUserListLoadedRequestSent = true;
    private Query query;
    private List<Message> mLastMessageList = new ArrayList<>();
    private AllMessageAdapter mAllMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_message);
        ButterKnife.bind(this);
        initToolBar();
        mPresenter = new AllMessagePresenter(this);
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        initRecyclerView();
        mPresenter.getMessagedUserList(mUserId);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Messages");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onMessagedUserListLoaded(ArrayList<String> mMessagedUserList) {

        //only getMessageThreadList() once, next time it'll return
        if (isUserListLoadedRequestSent) {
            isUserListLoadedRequestSent = false;
        } else {
            return;
        }
        for (String messagedUserId : mMessagedUserList) {
            Log.e(TAG, "onMessagedUserListLoaded: " + messagedUserId);
            mPresenter.getMessageThreadList(messagedUserId);
        }
    }

    @Override
    public void onMessageThreadAdded(ArrayList<String> mMessageThreadList) {

        for (String thread : mMessageThreadList) {
            Log.e(TAG, "onMessageThreadAdded: " + thread);
            query = FirebaseDatabase.getInstance().getReference()
                    .child("Message")
                    .child(thread)
                    .limitToLast(1);
        }
        mPresenter.getLastMessage(query);
    }


    @Override
    public void onLastMessageLoaded(Message message) {
        mLastMessageList.add(message);

        mAllMessageAdapter = new AllMessageAdapter(mLastMessageList, this);
        mAllMessageRecyclerView.setAdapter(mAllMessageAdapter);
        mAllMessageAdapter.notifyDataSetChanged();

        Log.e(TAG, "onLastMessageLoaded: " + message.getMessage());
    }

    private void initRecyclerView() {
        mAllMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }
}
