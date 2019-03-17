package com.appkwan.exertion.feature.login;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private FirebaseAuth mAuth;

    LoginPresenter(LoginContract.View view) {
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void loginUser(final String email, final String password) {

        view.showLoader();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        view.hideLoader();
                        view.navigateToHome();
                    }else{
                        view.hideLoader();
                        view.onLoginError("Please verify your email first!");
                    }

                }else{
                    view.hideLoader();
                    view.onLoginError(task.getException().getMessage());
                    return;
                }
            }
        });
    }

}
