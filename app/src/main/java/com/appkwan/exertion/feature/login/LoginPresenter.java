package com.appkwan.exertion.feature.login;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootDataRef;

    LoginPresenter(LoginContract.View view) {
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
        mRootDataRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public void loginUser(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String userId = task.getResult().getUser().getUid();
                    String tokenId = FirebaseInstanceId.getInstance().getToken();

                    saveUserId(userId, email, tokenId);

                }else{
                    view.onLoginError(task.getException().getMessage());
                    return;
                }
            }
        });
    }

    @Override
    public void saveUserId(String userId, String email, String tokenId) {

        Map map = new HashMap();
        map.put("email", email);
        map.put("tokenId", tokenId);

        mRootDataRef.child(userId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    view.navigateToHome();
                }else{
                    view.onLoginError(task.getException().getMessage());
                }
            }
        });
    }
}
