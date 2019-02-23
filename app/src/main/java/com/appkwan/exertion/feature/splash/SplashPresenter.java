package com.appkwan.exertion.feature.splash;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View view;
    private DatabaseReference mRootDataRef;

    SplashPresenter(SplashContract.View view){
        this.view = view;
        mRootDataRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public void checkIfUserLoggedIn() {
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            view.navigateToAccountChoiceActivity();
            return;
        }else{
            checkUserType();
        }
    }

    public void checkUserType() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(userId == null){
            view.navigateToAccountChoiceActivity();
            return;
        }else{

            mRootDataRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        view.navigateToHome();
                    }else{
                        view.navigateToAccountChoiceActivity();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
