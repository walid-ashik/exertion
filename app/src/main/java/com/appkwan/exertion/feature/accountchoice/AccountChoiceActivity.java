package com.appkwan.exertion.feature.accountchoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.signup.SignUpActivity;
import com.appkwan.exertion.feature.utitlity.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountChoiceActivity extends AppCompatActivity {

    @BindView(R.id.mStudentLinearLayout)
    LinearLayout mStudentLinearLayout;
    @BindView(R.id.mGuardianLinearLayout)
    LinearLayout mGuardianLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accont_choice);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.mStudentLinearLayout)
    public void studentUserChoice(View view){
        sendIntentToSignUpPage("Student");
    }

    @OnClick(R.id.mGuardianLinearLayout)
    public void guardianUserChoice(View view){
        sendIntentToSignUpPage("Guardian");
    }

    private void sendIntentToSignUpPage(String userType) {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra(Constant.USER_TYPE_KEY, userType);
        startActivity(intent);
    }

}
