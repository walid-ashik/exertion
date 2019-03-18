package com.appkwan.exertion.feature.newpost;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.home.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewPostActivity extends AppCompatActivity implements NewPostView {

    @BindView(R.id.mBackImageView)
    ImageView mBackImageView;
    @BindView(R.id.mShareTextViewButton)
    TextView mShareTextViewButton;
    @BindView(R.id.mUserImageCircleImageView)
    CircleImageView mUserImageCircleImageView;
    @BindView(R.id.mUserNameTextView)
    TextView mUserNameTextView;
    @BindView(R.id.mPostEditText)
    EditText mPostEditText;
    @BindView(R.id.constraintLayout5)
    ConstraintLayout constraintLayout5;
    @BindView(R.id.mBloodButton)
    Button mBloodButton;
    @BindView(R.id.mTuitionButton)
    Button mTuitionButton;
    @BindView(R.id.mLocationEditText)
    EditText mLocationEditText;

    private NewPostPresenter mPresenter;

    private String mPostType = "";
    private String mBloodGroup = "";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        mPresenter = new NewPostPresenter(this);

        mPresenter.getUserName();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.mBackImageView, R.id.mShareTextViewButton, R.id.mBloodButton, R.id.mTuitionButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBackImageView:
                onBackPressed();
                break;
            case R.id.mShareTextViewButton:
                saveThePostInDatabase();
                break;
            case R.id.mBloodButton:
                mTuitionButton.setText("TUITION?");
                mTuitionButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                showBloodGroupDialog();
                break;
            case R.id.mTuitionButton:
                mPostType = "Tuition";
                mTuitionButton.setText("Ok " + mPostType);
                mTuitionButton.setBackgroundColor(getResources().getColor(R.color.darkTextColor));
                break;
        }
    }

    @Override
    public void showUserName(String name) {
        mUserNameTextView.setText(name);
    }

    @Override
    public void onPostSuccess() {
        onBackPressed();
        Toast.makeText(this, "Posted Successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostDatabaseError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoader() {
        mProgressDialog.hide();
    }

    @Override
    public void showLoader() {
        mProgressDialog.show();
    }

    @Override
    public void loadUserImage(String profile_image) {
        Glide.with(getApplicationContext())
                .load(profile_image)
                .apply(RequestOptions.placeholderOf(getApplicationContext().getResources().getDrawable(R.drawable.ic_avatar_app)))
                .into(mUserImageCircleImageView);
    }

    private void saveThePostInDatabase() {
        String post = mPostEditText.getText().toString().trim();
        String location = mLocationEditText.getText().toString().trim();

        if(isAllInputted(post, location)){
            if(mPostType.equals("Tuition")){
               requestUserInputSubjectName(post, location);
            }else{
                mPresenter.saveThePostToDataBase(mPostType, post, location, mBloodGroup, "");
            }
        }
    }

    private void requestUserInputSubjectName(String post, String location) {

        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.item_search_layout, null);

        final EditText mSearchArea = mView.findViewById(R.id.searchArea);
        ImageView searchImageView = mView.findViewById(R.id.search_image_view);
        searchImageView.setVisibility(View.GONE);

        mSearchArea.setHint("Enter which subject");
        Button mSearchButton = mView.findViewById(R.id.searchButtonSearch);
        mSearchButton.setText("Add Subject");
        mBuilder.setView(mView);

        android.app.AlertDialog dialog = mBuilder.create();
        dialog.show();

        mSearchButton.setOnClickListener(view -> {
            String subjectName = mSearchArea.getText().toString().trim();
            dialog.hide();
            mPresenter.saveThePostToDataBase(mPostType, post, location, mBloodGroup, subjectName.toLowerCase());
        });
    }

    private boolean isAllInputted(String post, String location) {
        if(post.isEmpty()){
            Toast.makeText(this, "Please write post", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(location.isEmpty()){
            Toast.makeText(this, "Please enter location", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mPostType.isEmpty()){
            Toast.makeText(this, "Please enter what you're looking", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mPostType.equals("Blood")){
            if(mBloodGroup.isEmpty()){
                Toast.makeText(this, "Please select blood group", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void showBloodGroupDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_blood_group, null);

        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        final AlertDialog dialog = alertDialogBuilder.create();

        RadioGroup bloodRadioGroup = view.findViewById(R.id.bloodRadioGroup);
        Button confirm = view.findViewById(R.id.buttonConfirm);

        bloodRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            RadioButton rb = radioGroup.findViewById(checkedId);

            if (null != rb && checkedId > -1) {
                mBloodGroup = rb.getText().toString().toLowerCase();
                mPostType = "Blood";
            }
        });

        confirm.setOnClickListener(view1 -> {
            if(! mBloodGroup.isEmpty()){
                mBloodButton.setText("Group: " + mBloodGroup);
                dialog.hide();
            }

        });

        dialog.show();

    }

}
