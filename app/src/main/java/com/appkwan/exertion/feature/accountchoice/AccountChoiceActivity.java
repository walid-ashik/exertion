package com.appkwan.exertion.feature.accountchoice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.appkwan.exertion.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
}
