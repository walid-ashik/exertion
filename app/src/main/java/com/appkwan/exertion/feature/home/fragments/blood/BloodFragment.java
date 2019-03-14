package com.appkwan.exertion.feature.home.fragments.blood;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.home.fragments.Post;
import com.appkwan.exertion.feature.home.fragments.tuition.TuitionAdapter;
import com.appkwan.exertion.feature.home.fragments.tuition.TuitionPresenter;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodFragment extends Fragment implements BloodView {


    @BindView(R.id.mBloodRecyclerView)
    RecyclerView mBloodRecyclerView;
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
        mPresenter = new BloodPresenter(this);
        mPresenter.getAllTuitionPosts();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPostLoaded(List<Post> postList) {

        Collections.reverse(postList);

        mAdapter = new BloodAdapter(postList, getContext());
        mBloodRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPostLoadingError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void initRecyclerView(View view) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        mBloodRecyclerView.setLayoutManager(mLayoutManager);
        mBloodRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
