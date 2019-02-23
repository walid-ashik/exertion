package com.appkwan.exertion.feature.userinfo;

public interface UserInfoView {
    void showLoader();

    void hideLoader();

    void onUserInfoError(String message);

    void navigateToHome();
}
