package com.appkwan.exertion.feature.login;

public interface LoginContract {

    interface View{
        void navigateToHome();
        void onLoginError(String e);
        void showLoader();
        void hideLoader();
    }

    interface Presenter{
        void loginUser(String email, String password);
    }

}
