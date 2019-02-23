package com.appkwan.exertion.feature.login;

import android.widget.EditText;

public interface LoginContract {

    interface View{
        void navigateToHome();
        void onLoginError(String e);
    }

    interface Presenter{
        void loginUser(String email, String password);
        void saveUserId(String userId, String email, String tokenId);
    }

}
