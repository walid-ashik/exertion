package com.appkwan.exertion.feature.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.home.MainActivity;
import com.appkwan.exertion.feature.utitlity.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements SignUpView{

    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.mEmail)
    EditText mEmail;
    @BindView(R.id.mPassword)
    EditText mPassword;
    @BindView(R.id.mConfirmPassword)
    EditText mConfirmPassword;
    @BindView(R.id.mSignUpButton)
    Button mSignUpButton;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textview_privacy_policy)
    TextView mTextviewPrivacyPolicy;
    @BindView(R.id.textView5)
    TextView textView5;

    private SignUpPresenter mPresenter;
    private String mUserType = "";

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        if(getIntent() != null){
            mUserType = getIntent().getStringExtra(Constant.USER_TYPE_KEY);
        }

        mPresenter = new SignUpPresenter(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Please wait...");
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        return;
    }

    @Override
    public void onSignUpError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoader() {
        mProgressDialog.show();
    }

    @Override
    public void hideLoader() {
        mProgressDialog.hide();
    }

    @OnClick(R.id.mSignUpButton)
    public void signUpButtonClicked(View view){
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confrimPassword = mConfirmPassword.getText().toString().trim();

        if(email.isEmpty()){
            Toast.makeText(this, "Input your email", Toast.LENGTH_SHORT).show();
            mEmail.setHintTextColor(getResources().getColor(R.color.warning));
            mEmail.setHint("Enter email");
            return;
        }

        if(password.isEmpty()){
            Toast.makeText(this, "Input your password", Toast.LENGTH_SHORT).show();
            mPassword.setHintTextColor(getResources().getColor(R.color.warning));
            mPassword.setHint("Enter password");
            return;
        }

        if(confrimPassword.isEmpty()){
            Toast.makeText(this, "Input your password", Toast.LENGTH_SHORT).show();
            mConfirmPassword.setHintTextColor(getResources().getColor(R.color.warning));
            mConfirmPassword.setHint("Enter password");
            return;
        }

        if(! password.equals(confrimPassword)){
            Toast.makeText(this, "Your password didn't match!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!email.isEmpty() && !password.isEmpty()){
            mPresenter.signUpNewUser(mEmail.getText().toString(), mPassword.getText().toString(), mUserType);
        }
    }


}
