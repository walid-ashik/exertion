package com.appkwan.exertion.feature.signup.otpsignup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class MobileSignUpPresenter {
    private MobileSignUpView mView;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootDataRef;

    public MobileSignUpPresenter(MobileSignUpView mView) {
        this.mView = mView;
        mRootDataRef = FirebaseDatabase.getInstance().getReference().child("Users");

    }


    public void saveUserInfo(String userId, String userType) {
        String tokenId = FirebaseInstanceId.getInstance().getToken();
        saveUserId(userId, "email", tokenId, userType);
    }

    void saveUserId(String userId, String email, String tokenId, String userType) {

        mView.showLoader();

        Map map = new HashMap();
        map.put("email", email);
        map.put("tokenId", tokenId);
        map.put("userType", userType);

        mRootDataRef.child(userId).updateChildren(map).addOnCompleteListener((OnCompleteListener<Void>) task -> {
            if (task.isSuccessful()) {
                mView.hideLoader();
                mView.navigateToMainActivity();
            } else {
                mView.hideLoader();
                mView.onError(task.getException().getMessage());
            }
        });
    }
}
