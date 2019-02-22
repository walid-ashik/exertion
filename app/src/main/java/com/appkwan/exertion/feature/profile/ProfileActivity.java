package com.appkwan.exertion.feature.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.appkwan.exertion.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
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

    private List<EditText> mAllEditTexts = new ArrayList<>();
    private List<TextView> mEditButtons = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        addAllEditTextInList();
        addEditButtonsInList();
        setFocusableFalseEditText();
    }

    public void editButtonClicked(View view) {
        hideEditButtons();
        setFocusableTrueInEditText();
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
            editText.setText("Random Texts");
        }
    }

    private void setFocusableTrueInEditText() {
        for (EditText editText : mAllEditTexts) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        }
    }

    private void hideEditButtons() {
        for(TextView editButton : mEditButtons){
            editButton.setVisibility(View.GONE);
        }
    }
}
