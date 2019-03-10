package com.appkwan.exertion.feature.signup.otpsignup;

public interface MobileSignUpView {
    void navigateToMainActivity();
    void onError(String error);
    void showLoader();
    void hideLoader();
}
