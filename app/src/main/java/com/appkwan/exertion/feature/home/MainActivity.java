package com.appkwan.exertion.feature.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.accountchoice.AccountChoiceActivity;
import com.appkwan.exertion.feature.home.fragments.blood.BloodFragment;
import com.appkwan.exertion.feature.home.fragments.tuition.TuitionFragment;
import com.appkwan.exertion.feature.newpost.NewPostActivity;
import com.appkwan.exertion.feature.userinfo.UserInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.mNewPostFloatButton)
    FloatingActionButton mNewPostFloatButton;

    private MainPresenter mPresenter;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new MainPresenter(this);

        initToolBar();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mPresenter.logoutUser();
                break;
        }
        return true;
    }

    @Override
    public void navigateToLoginActivity() {
        startActivity(new Intent(this, AccountChoiceActivity.class));
        finish();
    }

    @Override
    public void navigateToUserInfoActivity() {
        startActivity(new Intent(this, UserInfoActivity.class));
        finish();
    }

    @OnClick(R.id.mNewPostFloatButton)
    public void createNewPost(View view){
        startActivity(new Intent(this, NewPostActivity.class
        ));
    }
    private void initToolBar() {
        setSupportActionBar(toolbar);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new TuitionFragment();
                    break;
                case 1:
                    fragment = new BloodFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "TUITION";
                case 1:
                    return "BLOOD";
            }
            return null;
        }
    }
}
