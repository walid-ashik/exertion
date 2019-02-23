package com.appkwan.exertion.feature.userinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.home.MainActivity;
import com.appkwan.exertion.feature.home.MainPresenter;
import com.appkwan.exertion.feature.home.MainView;
import com.appkwan.exertion.feature.signup.SignUpPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends AppCompatActivity implements UserInfoView {

    @BindView(R.id.mUserINfoProfile)
    ImageView mUserINfoProfile;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.register_company_person_name_edittext)
    EditText mUserName;
    @BindView(R.id.mSpinnerGender)
    TextView mSpinnerGender;
    @BindView(R.id.mPresentAddress)
    EditText mPresentAddress;
    @BindView(R.id.mPermanentAddress)
    EditText mPermanentAddress;
    @BindView(R.id.mUserPhone)
    EditText mUserPhone;
    @BindView(R.id.mCreateButton)
    Button mCreateButton;
    @BindView(R.id.cardView)
    CardView cardView;

    private UserInfoPresenter mPresenter;
    private ProgressDialog mProgressDialog;
    private String mGender = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);

        mPresenter = new UserInfoPresenter(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
    }

    @Override
    public void showLoader() {
        mProgressDialog.show();
    }

    @Override
    public void hideLoader() {
        mProgressDialog.hide();
    }

    @Override
    public void onUserInfoError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.mSpinnerGender)
    public void selectGenderClicked(View view) {
        showGenderSelectionDialog();
    }

    @OnClick(R.id.mCreateButton)
    public void createButtonClicked(View view) {

        String name = mUserName.getText().toString().trim();
        String gender = mGender;
        String presentAddress = mPresentAddress.getText().toString().trim();
        String permanentAddress = mPermanentAddress.getText().toString().trim();
        String phone = mUserPhone.getText().toString().trim();

        if (isUserInputtedAll(name, gender, presentAddress, permanentAddress, phone)) {
            mPresenter.uploadUserInfo(name, gender, presentAddress, permanentAddress, phone);
        }
    }

    private boolean isUserInputtedAll(String name, String gender, String presentAddress, String permanentAddress, String phone) {
        if (name.isEmpty() || gender.isEmpty() || presentAddress.isEmpty() || permanentAddress.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please input all", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showGenderSelectionDialog() {
        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_gender_selection, null);

        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        final android.support.v7.app.AlertDialog dialog = alertDialogBuilder.create();

        TextView maleTextView = view.findViewById(R.id.maleTextView);
        TextView femaleTextView = view.findViewById(R.id.femaleTextView);

        dialog.show();

        maleTextView.setOnClickListener(v -> {
            mGender = maleTextView.getText().toString();
            mSpinnerGender.setText("Gender: Male");
            dialog.hide();
        });

        femaleTextView.setOnClickListener(v -> {
            mSpinnerGender.setText("Gender: Female");
            mGender = femaleTextView.getText().toString();
            dialog.hide();
        });


    }
}
