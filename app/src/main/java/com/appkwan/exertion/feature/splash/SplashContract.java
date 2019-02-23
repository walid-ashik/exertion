package com.appkwan.exertion.feature.splash;

public interface SplashContract {

    interface View{
        void navigateToLoginActivity();
        void navigateToHome();
    }

    interface Presenter{
        void checkIfUserLoggedIn();
    }
}
