package com.appkwan.exertion.feature.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.dbhelper.imagehelper.ImageUploader;
import com.appkwan.exertion.feature.dbhelper.imagehelper.OnImageUploaderListener;
import com.appkwan.exertion.feature.home.User;
import com.appkwan.exertion.feature.message.MessageActivity;
import com.appkwan.exertion.feature.utitlity.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.hsalf.smilerating.SmileRating;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements ProfileView, OnImageUploaderListener, SmileRating.OnSmileySelectionListener {

    private static final String TAG = "ProfileActivity";

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
    @BindView(R.id.mAddYourCvTextView)
    TextView mAddYourCvTextView;
    @BindView(R.id.mEditImageView)
    ImageView mEditImageView;
    @BindView(R.id.mRatingTextView)
    TextView mRatingTextView;
    @BindView(R.id.ratingBar)
    RatingBar mRatingBar;
    @BindView(R.id.mUserTypeImageView)
    ImageView mUserTypeImageView;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;

    private List<EditText> mAllEditTexts = new ArrayList<>();
    private List<TextView> mEditButtons = new ArrayList<>();

    private ProfilePresenter mPresenter;
    private ImageUploader mImageUploader;

    private String mUserId, mOtherUserId;

    private ProgressDialog mProgressDialog;
    private String mUserCvUrl = "";

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
            mPresenter.checkIfOtherUserRatedThisPerson(mOtherUserId);
            hideEditButtons();
            hideAddYourCvButton();
        }

        setToolbar();
        removeRatingSmileName();
        mPresenter.getRating();
    }


    @OnClick(R.id.mAddYourCvTextView)
    public void onAddYourCvClicked() {

        if (mAddYourCvTextView.getText().equals("Show Your CV")) {
            showUserCv();
            return;
        }

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), Constant.PDF_RQ_KEY);
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


    @OnClick(R.id.mRatingTextView)
    public void onAddRatingTextViewClicked() {

        if (mOtherUserId == null) {
            return;
        } else if (mRatingTextView.getText().toString().equals(getString(R.string.you_ve_rated))) {
            return;
        }

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.item_rating_dialog, null);

        SmileRating smileRating = mView.findViewById(R.id.ratingView);
        Button rateButton = mView.findViewById(R.id.rateButton);

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

        rateButton.setOnClickListener(view -> {

            if (smileRating.getRating() != 0) {

                int rating = smileRating.getRating();
                mPresenter.setRating(mOtherUserId, rating);

                dialog.hide();
            } else {
                Toast.makeText(this, "Please add rating", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSmileySelected(int smiley, boolean reselected) {
        switch (smiley) {
            case SmileRating.BAD:
                Log.i(TAG, "Bad");
                break;
            case SmileRating.GOOD:
                Log.i(TAG, "Good");
                break;
            case SmileRating.GREAT:
                Log.i(TAG, "Great");
                break;
            case SmileRating.OKAY:
                Log.i(TAG, "Okay");
                break;
            case SmileRating.TERRIBLE:
                Log.i(TAG, "Terrible");
                break;
        }
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
                navigateToMessageActivity();
                break;

            case R.id.action_cv:
                showUserCv();
                break;
        }
        return true;
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
        mUserCvUrl = user.getCv();
        setShowCvText(mUserCvUrl);
        changeDesignAccordingToUserType(user.getUserType());
    }

    private void changeDesignAccordingToUserType(String userType) {
        if (userType.equals("Student")) {
            mUserTypeImageView.setBackground(getResources().getDrawable(R.drawable.ic_student_cap));
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.ic_guardian_bg_color));
            toolbar.setBackgroundColor(getResources().getColor(R.color.ic_guardian_bg_color));
        } else {
            mUserTypeImageView.setBackground(getResources().getDrawable(R.drawable.ic_white_avater));
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void setShowCvText(String mUserCvUrl) {
        if (mUserCvUrl != null) {
            if (!mUserCvUrl.isEmpty()) {
                mAddYourCvTextView.setText("Show Your CV");
            }
        }
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
    public void onCvUrlSavedSuccess() {
        if (mProgressDialog.isShowing())
            mProgressDialog.hide();
        mAddYourCvTextView.setText("Your CV is uploaded successfully!");
    }

    @Override
    public void onRatingLoaded(double rating, int ratingCount) {
        Log.e(TAG, "onRatingLoaded: " + rating);
        mRatingBar.setRating((float) rating);
    }

    @Override
    public void otherUserRatedThisUser(boolean isOtherUserRated) {
        if (isOtherUserRated) {
            mRatingTextView.setText(getString(R.string.you_ve_rated));
        } else {
            Drawable img = getResources().getDrawable(R.drawable.ic_add_black_24dp);
            mRatingTextView.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            mRatingTextView.setText("Add Rating");
        }
    }

    @Override
    public void onRatingSuccess() {
        Toast.makeText(this, "Rating successfully saved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRatingError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        if (mProgressDialog.isShowing())
            mProgressDialog.hide();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCvUploadedSuccess(String cvUrl) {
        if (mProgressDialog.isShowing())
            mProgressDialog.hide();
        mPresenter.saveUserCvDownloadUrl(cvUrl);
    }

    @Override
    public void onCvUploadingError(String message) {
        if (mProgressDialog.isShowing())
            mProgressDialog.hide();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.PICK_IMAGE_CODE) {
            //image is selected
            mProgressDialog.show();
            uploadImage(data);
        }

        if (requestCode == Constant.PDF_RQ_KEY) {
            mProgressDialog.show();
            uploadCv(data);
        }
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

    private void navigateToMessageActivity() {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra(Constant.USER_ID_KEY, mOtherUserId);
        startActivity(intent);
    }

    private void requestImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constant.PICK_IMAGE_CODE);
    }

    private void uploadImage(Intent data) {
        mImageUploader.uploadUserProfileImage(data);
    }

    private void uploadCv(Intent data) {
        mImageUploader.uploadUserCv(data);
    }

    private void loadProfileImage(String imageUrl) {
        Glide.with(getApplicationContext())
                .load(imageUrl)
                .apply(RequestOptions.placeholderOf(getResources().getDrawable(R.drawable.ic_avatar_app)))
                .into(circleImageView);
    }

    private void hideAddYourCvButton() {
        mAddYourCvTextView.setVisibility(View.INVISIBLE);
    }

    private void showUserCv() {

        if (mUserCvUrl != null) {
            if (!mUserCvUrl.isEmpty()) {
                showUserCvDownloadOrViewChoiceDialog();
            }
        } else {
            Toast.makeText(this, "This user doesn't have CV uploaded!", Toast.LENGTH_SHORT).show();
        }

    }

    private void showUserCvDownloadOrViewChoiceDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Select what you want?")

                .setPositiveButton("View", (dialog, which) -> {

                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.setDataAndType(Uri.parse(mUserCvUrl), "application/pdf");
                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    Intent intent = Intent.createChooser(target, "Open File");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // Instruct the user to install a PDF reader here, or something
                    }

                })
                .setNegativeButton("Download", (dialog, which) -> {
                    Intent downloadIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUserCvUrl));
                    startActivity(downloadIntent);
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void removeRatingSmileName() {
//        mRatingView.setNameForSmile(BaseRating.TERRIBLE, "");
//        mRatingView.setNameForSmile(BaseRating.BAD, "");
//        mRatingView.setNameForSmile(BaseRating.OKAY, "");
//        mRatingView.setNameForSmile(BaseRating.GOOD, "");
//        mRatingView.setNameForSmile(BaseRating.GREAT, "");
    }
}
