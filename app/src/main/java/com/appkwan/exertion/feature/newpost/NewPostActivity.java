package com.appkwan.exertion.feature.newpost;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appkwan.exertion.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewPostActivity extends AppCompatActivity {

    @BindView(R.id.mBackImageView)
    ImageView mBackImageView;
    @BindView(R.id.mShareTextViewButton)
    TextView mShareTextViewButton;
    @BindView(R.id.mUserImageCircleImageView)
    CircleImageView mUserImageCircleImageView;
    @BindView(R.id.mUserNameTextView)
    TextView mUserNameTextView;
    @BindView(R.id.mPostEditText)
    EditText mPostEditText;
    @BindView(R.id.constraintLayout5)
    ConstraintLayout constraintLayout5;
    @BindView(R.id.mBloodButton)
    Button mBloodButton;
    @BindView(R.id.mTuitionButton)
    Button mTuitionButton;
    @BindView(R.id.mLocationEditText)
    EditText mLocationEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.mBackImageView, R.id.mShareTextViewButton, R.id.mBloodButton, R.id.mTuitionButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBackImageView:
                break;
            case R.id.mShareTextViewButton:
                break;
            case R.id.mBloodButton:
                break;
            case R.id.mTuitionButton:
                break;
        }
    }
}
