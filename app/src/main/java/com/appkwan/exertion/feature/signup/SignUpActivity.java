package com.appkwan.exertion.feature.signup;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.home.MainActivity;
import com.appkwan.exertion.feature.utitlity.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

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
    @BindView(R.id.textviewEmailAllowanceStudent)
    TextView mTextviewEmailAllowanceStudent;
    @BindView(R.id.textview_privacy_policy)
    TextView mTextviewPrivacyPolicy;
    @BindView(R.id.textView5)
    TextView textView5;

    private SignUpPresenter mPresenter;
    private String mUserType = "";

    private ProgressDialog mProgressDialog;
    private String verificationId = "";

    private FirebaseAuth mAuth;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        if(getIntent() != null){
            mUserType = getIntent().getStringExtra(Constant.USER_TYPE_KEY);
        }

        if(mUserType.equals("Student")){
            mTextviewEmailAllowanceStudent.setText(Html.fromHtml(getResources().getString(R.string.email_allowance)));
        }else{
            mTextviewEmailAllowanceStudent.setVisibility(View.GONE);
        }

        mPresenter = new SignUpPresenter(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
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

        if(mUserType.equals("Student")){
            if( ! email.contains("@diu.edu.bd")){
                Toast.makeText(this, "You have to use @diu.edu.bd email for registration!", Toast.LENGTH_SHORT).show();
                return;
            }
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

    @OnClick(R.id.mPhoneRegistrationButton)
    public void sendToMobileSignUpActivity(){
       startActivity(new Intent(this, MobileSignUpActivity.class));
    }

}
