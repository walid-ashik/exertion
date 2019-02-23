package com.appkwan.exertion.feature.home;

import com.google.firebase.auth.FirebaseAuth;

public class MainPresenter  {
    private MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    void logoutUser(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
            view.navigateToLoginActivity();
        }
    }
}
