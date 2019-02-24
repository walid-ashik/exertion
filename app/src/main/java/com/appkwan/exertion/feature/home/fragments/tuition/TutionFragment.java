package com.appkwan.exertion.feature.home.fragments.tuition;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.home.fragments.Post;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutionFragment extends Fragment implements TuitionView {

    @BindView(R.id.mTuitionRecyclerView)
    RecyclerView mTuitionRecyclerView;

    private TuitionPresenter mPresenter;

    TuitionAdapter mAdapter;

    Unbinder unbinder;


    public TutionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tution, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRecyclerView(view);
        mPresenter = new TuitionPresenter(this);
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
        mAdapter = new TuitionAdapter(postList, getContext());
        mTuitionRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private void initRecyclerView(View view) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mTuitionRecyclerView.setLayoutManager(mLayoutManager);
        mTuitionRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

}
