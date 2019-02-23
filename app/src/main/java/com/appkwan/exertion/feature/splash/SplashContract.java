package com.appkwan.exertion.feature.splash;

public interface SplashContract {

    interface View{
        void navigateToAccountChoiceActivity();
        void navigateToHome();
    }

    interface Presenter{
        void checkIfUserLoggedIn();
    }
}
