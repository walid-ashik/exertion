package com.appkwan.exertion.feature.signup;

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

public class SignUpPresenter {

    private SignUpView view;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootDataRef;

    public SignUpPresenter(SignUpView view) {
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
        mRootDataRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    void signUpNewUser(final String email, String password, final String userType){

        view.showLoader();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String userId = task.getResult().getUser().getUid();
                    String tokenId = FirebaseInstanceId.getInstance().getToken();

                    saveUserId(userId, email, tokenId, userType);

                }else{
                    view.hideLoader();
                    view.onSignUpError(task.getException().getMessage());
                    return;
                }
            }
        });
    }

    void saveUserId(String userId, String email, String tokenId, String userType) {

        Map map = new HashMap();
        map.put("email", email);
        map.put("tokenId", tokenId);
        map.put("userType", userType);

        mRootDataRef.child(userId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    view.hideLoader();
                    view.navigateToHome();
                }else{
                    view.hideLoader();
                    view.onSignUpError(task.getException().getMessage());
                }
            }
        });
    }

}
