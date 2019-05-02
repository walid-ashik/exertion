package com.appkwan.exertion.feature.home.fragments.tuition;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.home.MainActivity;
import com.appkwan.exertion.feature.home.OnSearchTextListener;
import com.appkwan.exertion.feature.home.SearchLocationEvent;
import com.appkwan.exertion.feature.home.fragments.Post;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TuitionFragment extends Fragment implements TuitionView, OnSearchTextListener {

    private static final String TAG = "TuitionFragment";

    @BindView(R.id.mTuitionRecyclerView)
    RecyclerView mTuitionRecyclerView;
    @BindView(R.id.tv_empty_text)
    TextView mReload;
    @BindView(R.id.iv_top_image)
    ImageView ivTopImage;
    @BindView(R.id.layout_empty)
    RelativeLayout layoutEmpty;

    private TuitionPresenter mPresenter;

    TuitionAdapter mAdapter;

    Unbinder unbinder;

    public TuitionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tution, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRecyclerView(view);
        ((MainActivity) getActivity()).setOnSearchTextListener(this);
        mPresenter = new TuitionPresenter(this);
        EventBus.getDefault().register(this);
        mPresenter.getAllTuitionPosts();
        return view;
    }

    @OnClick(R.id.tv_empty_text)
    public void onReloadButtonClicked() {
        mPresenter.getAllTuitionPosts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(SearchLocationEvent searchEvent) {

        if (searchEvent.getSearchType().equals("Location")) {
            mPresenter.querySearch("query_location", searchEvent.getSearchText());
        } else if (searchEvent.getSearchType().equals("Subject")) {
            mPresenter.querySearch("query_subject", searchEvent.getSearchText());
        }
    }

    @Override
    public void onPostLoaded(List<Post> postList) {
        if (layoutEmpty != null)
            layoutEmpty.setVisibility(View.GONE);
        if (mTuitionRecyclerView != null)
            mTuitionRecyclerView.setVisibility(View.VISIBLE);

        Collections.reverse(postList);

        mAdapter = new TuitionAdapter(postList, getContext());
        mTuitionRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPostLoadingError(String message) {
        layoutEmpty.setVisibility(View.VISIBLE);
        mTuitionRecyclerView.setVisibility(View.GONE);
    }

    private void initRecyclerView(View view) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mTuitionRecyclerView.setLayoutManager(mLayoutManager);
        mTuitionRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onQueryTextEntered(String query) {
        Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
    }
}
