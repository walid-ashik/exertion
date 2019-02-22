package com.appkwan.exertion.feature.userinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.appkwan.exertion.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInfoActivity extends AppCompatActivity {

    @BindView(R.id.mUserINfoProfile)
    ImageView mUserINfoProfile;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.register_company_person_name_edittext)
    EditText registerCompanyPersonNameEdittext;
    @BindView(R.id.mSpinnerGender)
    TextView mSpinnerGender;
    @BindView(R.id.mPresentAddress)
    EditText mPresentAddress;
    @BindView(R.id.mPermanentAddress)
    EditText mPermanentAddress;
    @BindView(R.id.mUserPhone)
    EditText mUserPhone;
    @BindView(R.id.register_company_create_button)
    Button registerCompanyCreateButton;
    @BindView(R.id.cardView)
    CardView cardView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
    }
}
