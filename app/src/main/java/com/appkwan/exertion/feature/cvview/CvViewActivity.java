package com.appkwan.exertion.feature.cvview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.utitlity.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CvViewActivity extends AppCompatActivity {

    @BindView(R.id.mCvView)
    WebView mCvView;
    private String mCvUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv_view);
        ButterKnife.bind(this);

        if(getIntent() != null){
            mCvUrl = getIntent().getStringExtra(Constant.CV_KEY);
        }

        mCvView.getSettings().setJavaScriptEnabled(true);
        mCvView.getSettings().setSupportZoom(true);
        mCvView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + mCvUrl);
    }
}
