package com.appkwan.exertion.feature.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.Placeholder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.dbhelper.imagehelper.ImageUploader;
import com.appkwan.exertion.feature.dbhelper.imagehelper.OnImageUploaderListener;
import com.appkwan.exertion.feature.home.User;
import com.appkwan.exertion.feature.utitlity.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements ProfileView, OnImageUploaderListener {

    private static final String TAG = "ProfileActivity";

    private static final int PICK_IMAGE_CODE = 0001;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
    @BindView(R.id.mUserNameTextView)
    TextView mUserNameTextView;
    @BindView(R.id.mUserTypeTextView)
    TextView mUserTypeTextView;
    @BindView(R.id.mBloodGroupEditText)
    EditText mBloodGroupEditText;
    @BindView(R.id.mGenderEdiTText)
    EditText mGenderEdiTText;
    @BindView(R.id.mEmailEditText)
    EditText mEmailEditText;
    @BindView(R.id.mPresentAddress)
    EditText mPresentAddress;
    @BindView(R.id.mPermanentAddress)
    EditText mPermanentAddress;
    @BindView(R.id.mPhoneNumber)
    EditText mPhoneNumber;
    @BindView(R.id.editBloodButton)
    TextView editBloodButton;
    @BindView(R.id.editGenderButton)
    TextView editGenderButton;
    @BindView(R.id.editEmailButton)
    TextView editEmailButton;
    @BindView(R.id.editPresentAddress)
    TextView editPresentAddress;
    @BindView(R.id.editPermanentAddress)
    TextView editPermanentAddress;
    @BindView(R.id.editPhoneButton)
    TextView editPhoneButton;
    @BindView(R.id.mEditImageView)
    ImageView mEditImageView;

    private List<EditText> mAllEditTexts = new ArrayList<>();
    private List<TextView> mEditButtons = new ArrayList<>();

    private ProfilePresenter mPresenter;
    private ImageUploader mImageUploader;

    private String mUserId, mOtherUserId;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        mPresenter = new ProfilePresenter(this);
        mImageUploader = new ImageUploader(this);

        addAllEditTextInList();
        addEditButtonsInList();
        setFocusableFalseEditText();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");

        if (getIntent().getStringExtra(Constant.USER_ID_KEY) == null) {
            //user itself visits this activity, hence show all edit buttons
            mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mPresenter.getUserDetails(mUserId);
        } else {
            //this is another user in this activity, hence hide edit buttons, show call options to
            //other user
            mOtherUserId = getIntent().getStringExtra(Constant.USER_ID_KEY);
            mPresenter.getUserDetails(mOtherUserId);
            mEditImageView.setVisibility(View.GONE);
            hideEditButtons();
        }

        setToolbar();

    }

    public void editButtonClicked(View view) {
        hideEditButtons();
        setFocusableTrueInEditText();
    }

    public void onEditImageButtonClicked(View view) {
        //only this user can change image
        if (mEditImageView.getVisibility() != View.GONE) {
            requestImagePicker();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //only show toolbar for other user
        if (mOtherUserId != null) {
            getMenuInflater().inflate(R.menu.profile_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_call:
                makeCall();
                break;
            case R.id.action_message:
                sendMessage();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_CODE) {
            //image is selected
            mProgressDialog.show();
            uploadImage(data);
        }
    }

    @Override
    public void showUserDetails(User user) {
        loadProfileImage(user.getProfile_image());
        mUserNameTextView.setText(user.getName());
        mUserTypeTextView.setText(user.getUserType());
        mBloodGroupEditText.setText(user.getBlood());
        mGenderEdiTText.setText(user.getGender());
        mEmailEditText.setText(user.getEmail());
        mPresentAddress.setText(user.getPresentAddress());
        mPermanentAddress.setText(user.getPermanentAddress());
        mPhoneNumber.setText(user.getPhone());
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImageUrlSavedSuccess() {
        if (mProgressDialog.isShowing())
            mProgressDialog.hide();
    }

    @Override
    public void onImageUploadedSuccess(String imageUrl) {
        if (mProgressDialog.isShowing())
            mProgressDialog.hide();
        loadProfileImage(imageUrl);
        mPresenter.saveUserUploadedImageDownloadUrl(imageUrl);
    }

    @Override
    public void onImageUploadingError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void addAllEditTextInList() {
        mAllEditTexts.add(mBloodGroupEditText);
        mAllEditTexts.add(mEmailEditText);
        mAllEditTexts.add(mGenderEdiTText);
        mAllEditTexts.add(mPresentAddress);
        mAllEditTexts.add(mPermanentAddress);
        mAllEditTexts.add(mPhoneNumber);
    }

    private void addEditButtonsInList() {
        mEditButtons.add(editBloodButton);
        mEditButtons.add(editGenderButton);
        mEditButtons.add(editEmailButton);
        mEditButtons.add(editPresentAddress);
        mEditButtons.add(editPermanentAddress);
        mEditButtons.add(editPhoneButton);
    }

    private void setFocusableFalseEditText() {
        for (EditText editText : mAllEditTexts) {
            editText.setFocusable(false);
        }
    }

    private void setFocusableTrueInEditText() {
        for (EditText editText : mAllEditTexts) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        }
    }

    private void hideEditButtons() {
        for (TextView editButton : mEditButtons) {
            editButton.setVisibility(View.GONE);
        }
    }

    private void makeCall() {
        String thisUserPhoneNumber = mPhoneNumber.getText().toString();

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + thisUserPhoneNumber));
        startActivity(intent);
    }

    private void sendMessage() {

    }

    private void requestImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE);
    }

    private void uploadImage(Intent data) {
        mImageUploader.uploadUserProfileImage(data);
    }

    private void loadProfileImage(String imageUrl) {
        Glide.with(getApplicationContext())
                .load(imageUrl)
                .apply(RequestOptions.placeholderOf(getResources().getDrawable(R.drawable.ic_avatar_app)))
                .into(circleImageView);
    }

}
