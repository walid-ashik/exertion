package com.appkwan.exertion.feature.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.accountchoice.AccountChoiceActivity;
import com.appkwan.exertion.feature.home.MainActivity;
import com.appkwan.exertion.feature.signup.SignUpActivity;
import com.appkwan.exertion.feature.signup.SignUpPresenter;
import com.appkwan.exertion.feature.signup.otpsignup.MobileSignInActivity;
import com.google.android.gms.common.oob.SignUp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.mEmailEditText)
    EditText mEmailEditText;
    @BindView(R.id.mPasswordEditText)
    EditText mPasswordEditText;
    @BindView(R.id.mLoginButton)
    Button mLoginButton;
    @BindView(R.id.mPhoneRegistrationButton)
    Button mPhoneRegistrationButton;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.mDontHaveAccount)
    TextView mDontHaveAccountTextView;

    private LoginPresenter mPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mPresenter = new LoginPresenter(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
    }

    @OnClick(R.id.mLoginButton)
    public void loginButtonClicked(View view){
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        if(email.isEmpty()){
            Toast.makeText(this, "Input your email", Toast.LENGTH_SHORT).show();
            mEmailEditText.setHintTextColor(getResources().getColor(R.color.warning));
            mEmailEditText.setHint("Enter email");
            return;
        }

        if(password.isEmpty()){
            Toast.makeText(this, "Input your password", Toast.LENGTH_SHORT).show();
            mPasswordEditText.setHintTextColor(getResources().getColor(R.color.warning));
            mPasswordEditText.setHint("Enter password");
            return;
        }

        if(!email.isEmpty() && !password.isEmpty()){
            mPresenter.loginUser(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString());
        }

    }

    @OnClick(R.id.mPhoneRegistrationButton)
    public void loginWithPhoneClicked(View view){
        startActivity(new Intent(this, MobileSignInActivity.class));
    }

    @OnClick(R.id.mDontHaveAccount)
    public void dontHaveAccountClicked(View view){
        startActivity(new Intent(this, AccountChoiceActivity.class));
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        return;
    }

    @Override
    public void onLoginError(String e) {
        Toast.makeText(this, e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoader() {
        mProgressDialog.show();
    }

    @Override
    public void hideLoader() {
        mProgressDialog.hide();
    }

}
