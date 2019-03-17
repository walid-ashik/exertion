package com.appkwan.exertion.feature.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.accountchoice.AccountChoiceActivity;
import com.appkwan.exertion.feature.home.fragments.blood.BloodFragment;
import com.appkwan.exertion.feature.home.fragments.tuition.TuitionFragment;
import com.appkwan.exertion.feature.message.allmessage.AllMessageActivity;
import com.appkwan.exertion.feature.newpost.NewPostActivity;
import com.appkwan.exertion.feature.profile.ProfileActivity;
import com.appkwan.exertion.feature.userinfo.UserInfoActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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
    private OnSearchTextListener onSearchTextListener;

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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
            case R.id.action_profile:
                navigateToProfileActivity();
                break;
            case R.id.action_messages:
                navigateToMessagesActivity();
                break;
            case R.id.action_search:
                decideWhicheSearchUserWants();
                break;
        }
        return true;
    }

    @SuppressLint("RestrictedApi")
    private void decideWhicheSearchUserWants() {
        //TUITION Fragment
        if (getSupportFragmentManager().getFragments().get(0).isMenuVisible()) {
            showUserCustomerSearchDialog("Subject", "Location");
        }

        //BLOOD Fragment
        if (getSupportFragmentManager().getFragments().get(1).isMenuVisible()) {
            showUserCustomerSearchDialog("Blood Group", "Area");
        }
    }

    private void showUserCustomerSearchDialog(String subject_group_search_text, String location) {
        new AlertDialog.Builder(this)
                .setMessage("Select what you want to search?")
                .setPositiveButton(subject_group_search_text, (dialog, which) -> {
                    showSearchDialog(subject_group_search_text);
                })
                .setNegativeButton(location, (dialog, which) -> {
                    showSearchDialog(location);
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showSearchDialog(String searchType) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.item_search_layout, null);

        final EditText mSearchArea = mView.findViewById(R.id.searchArea);
        mSearchArea.setHint("Enter your " + searchType);
        Button mSearchButton = mView.findViewById(R.id.searchButtonSearch);

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

        mSearchButton.setOnClickListener(view -> {
            String queryText = mSearchArea.getText().toString().trim();
            postSearchQueryEvent(queryText, searchType);
            dialog.hide();
        });

    }

    @SuppressLint("RestrictedApi")
    private void postSearchQueryEvent(String queryText, String queryType) {
        SearchLocationEvent event = new SearchLocationEvent();
        event.setSearchText(queryText.toLowerCase());

        if (getSupportFragmentManager().getFragments().get(0).isMenuVisible()) {
            event.setSearchType(queryType);
        } else {
            event.setSearchType(queryType);
        }

        EventBus.getDefault().post(event);
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
    public void createNewPost(View view) {
        startActivity(new Intent(this, NewPostActivity.class
        ));
    }

    public void setOnSearchTextListener(OnSearchTextListener listener) {
        onSearchTextListener = listener;
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
    }

    private void navigateToProfileActivity() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    private void navigateToMessagesActivity() {
        startActivity(new Intent(this, AllMessageActivity.class));
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

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

}
