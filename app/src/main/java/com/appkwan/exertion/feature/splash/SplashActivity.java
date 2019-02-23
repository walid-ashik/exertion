package com.appkwan.exertion.feature.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.appkwan.exertion.feature.home.MainActivity;
import com.appkwan.exertion.R;
import com.appkwan.exertion.feature.login.LoginActivity;
import com.google.firebase.FirebaseApp;

public class SplashActivity extends AppCompatActivity implements SplashContract.View{

    private SplashPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mPresenter = new SplashPresenter(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.checkIfUserLoggedIn();
            }
        }, 3000);


    }

    @Override
    public void navigateToLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        return;
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        return;
    }

}