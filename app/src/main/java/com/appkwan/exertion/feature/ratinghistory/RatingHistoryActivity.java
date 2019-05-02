package com.appkwan.exertion.feature.ratinghistory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appkwan.exertion.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingHistoryActivity extends AppCompatActivity implements RatingView {

    String mUserId = "";
    @BindView(R.id.mRatingRecyclerView)
    RecyclerView mRatingRecyclerView;
    @BindView(R.id.tv_empty_text)
    TextView tvEmptyText;
    @BindView(R.id.iv_top_image)
    ImageView ivTopImage;
    @BindView(R.id.layout_empty)
    RelativeLayout layoutEmpty;

    private DatabaseReference mUserRatingDataRef;
    private RatingPresenter mPresenter;

    private RatingAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_history);
        ButterKnife.bind(this);
        initRecyclerView();
        mPresenter = new RatingPresenter(this);

        if (getIntent().getStringExtra("userId") != null) {
            mUserId = getIntent().getStringExtra("userId");
        }

        mUserRatingDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId).child("Rating");
        mPresenter.getRatings(mUserRatingDataRef);

    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRatingRecyclerView.setLayoutManager(mLayoutManager);
        mRatingRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void showEmptyView() {
        tvEmptyText.setText("No Rating Found.");
        mRatingRecyclerView.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRatingLoaded(List<Rating> mRatingList) {
        layoutEmpty.setVisibility(View.GONE);
        Collections.reverse(mRatingList);
        mAdapter = new RatingAdapter(mRatingList, this);
        mRatingRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
