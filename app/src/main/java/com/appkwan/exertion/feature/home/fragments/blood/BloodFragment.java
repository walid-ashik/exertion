package com.appkwan.exertion.feature.home.fragments.blood;


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
public class BloodFragment extends Fragment implements BloodView, OnSearchTextListener {


    @BindView(R.id.mBloodRecyclerView)
    RecyclerView mBloodRecyclerView;
    @BindView(R.id.tv_empty_text)
    TextView mReload;
    @BindView(R.id.iv_top_image)
    ImageView ivTopImage;
    @BindView(R.id.layout_empty)
    RelativeLayout layoutEmpty;

    Unbinder unbinder;

    private BloodPresenter mPresenter;

    BloodAdapter mAdapter;

    public BloodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blood, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRecyclerView(view);
        ((MainActivity) getActivity()).setOnSearchTextListener(this);
        mPresenter = new BloodPresenter(this);
        EventBus.getDefault().register(this);
        mPresenter.getAllTuitionPosts();
        return view;
    }

    @Subscribe
    public void onEvent(SearchLocationEvent searchEvent){
        if(searchEvent.getSearchType().equals("Area")){
            mPresenter.querySearch("query_location", searchEvent.getSearchText());
        }else if(searchEvent.getSearchType().equals("Blood Group")){
            mPresenter.querySearch("group", searchEvent.getSearchText().toLowerCase());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.tv_empty_text)
    public void onReloadButtonClicked(){
        mPresenter.getAllTuitionPosts();
    }

    @Override
    public void onPostLoaded(List<Post> postList) {

        layoutEmpty.setVisibility(View.GONE);
        mBloodRecyclerView.setVisibility(View.VISIBLE);

        Collections.reverse(postList);

        mAdapter = new BloodAdapter(postList, getContext());
        mBloodRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPostLoadingError(String message) {
        layoutEmpty.setVisibility(View.VISIBLE);
        mBloodRecyclerView.setVisibility(View.GONE);
    }

    private void initRecyclerView(View view) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mBloodRecyclerView.setLayoutManager(mLayoutManager);
        mBloodRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onQueryTextEntered(String query) {
        Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
    }
}
