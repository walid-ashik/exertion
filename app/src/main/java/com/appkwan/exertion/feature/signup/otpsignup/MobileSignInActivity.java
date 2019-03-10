package com.appkwan.exertion.feature.signup.otpsignup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.home.MainActivity;
import com.appkwan.exertion.feature.utitlity.Constant;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MobileSignInActivity extends AppCompatActivity {

    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.phone_edittext)
    AppCompatEditText phoneEdittext;
    @BindView(R.id.send_code_button)
    Button sendCodeButton;
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.verification_code_edittext)
    AppCompatEditText verificationCodeEdittext;
    @BindView(R.id.verify_code_button)
    Button verifyCodeButton;
    @BindView(R.id.codeVerificationLayout)
    LinearLayout codeVerificationLayout;

    private FirebaseAuth mAuth;
    private String verificationId;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_sign_in);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait...");
    }


    @OnClick(R.id.send_code_button)
    public void onSendCodeButtonClicked() {

        String phoneNumber = phoneEdittext.getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog.show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+880" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);

        phoneEdittext.setVisibility(View.GONE);
        sendCodeButton.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);

        titleTextView.setText("Please enter the verification\ncode we sent to your number");
        codeVerificationLayout.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.verify_code_button)
    public void onVerificationButtonClicked() {
        String code = verificationCodeEdittext.getText().toString().trim();

        if (code.isEmpty()) {
            Toast.makeText(this, "Please enter code!", Toast.LENGTH_SHORT).show();
            return;
        }

        verifyCode(code);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            mProgressDialog.hide();

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                mProgressDialog.show();
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            mProgressDialog.hide();
            Toast.makeText(MobileSignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        mProgressDialog.hide();
                        Intent intent = new Intent(MobileSignInActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

    }


}
