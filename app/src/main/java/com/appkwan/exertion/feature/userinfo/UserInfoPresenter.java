package com.appkwan.exertion.feature.userinfo;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserInfoPresenter {

    private final String userId;
    private UserInfoView view;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootDataRef;

    public UserInfoPresenter(UserInfoView view) {
        this.view = view;

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAuth = FirebaseAuth.getInstance();
        mRootDataRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    void uploadPicture(String pictureUrl){

    }

    void uploadUserInfo(String name, String gender, String presentAddress, String permanentAddress, String phone){

        view.showLoader();

        Map map = new HashMap();
        map.put("name", name);
        map.put("gender", gender);
        map.put("presentAddress", presentAddress );
        map.put("permanentAddress", permanentAddress );
        map.put("phone", phone );

        mRootDataRef.child(userId).updateChildren(map).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                view.hideLoader();
                view.navigateToHome();
            }else{
                view.hideLoader();
                view.onUserInfoError(task.getException().getMessage());
            }
        });
    }
}
