package com.appkwan.exertion.feature.signup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appkwan.exertion.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.mEmail)
    EditText mEmail;
    @BindView(R.id.mPassword)
    EditText mPassword;
    @BindView(R.id.mConfirmPassword)
    EditText mConfirmPassword;
    @BindView(R.id.button)
    Button mSignUpButton;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textview_privacy_policy)
    TextView mTextviewPrivacyPolicy;
    @BindView(R.id.textView5)
    TextView textView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }
}
