package com.appkwan.exertion.feature.signup;

public interface SignUpView {
    void navigateToHome();
    void onSignUpError(String message);
    void showLoader();
    void hideLoader();
}
