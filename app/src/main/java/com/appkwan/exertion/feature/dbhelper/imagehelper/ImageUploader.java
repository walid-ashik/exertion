package com.appkwan.exertion.feature.dbhelper.imagehelper;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageUploader {

    private String mUserId;
    OnImageUploaderListener listener;
    private StorageReference mStorageRef;
    private StorageReference mStorageCvRef;
    private UploadTask uploadTask;

    public ImageUploader(OnImageUploaderListener listener) {
        this.listener = listener;
        mStorageRef = FirebaseStorage.getInstance().getReference().child("profile_images");
        mStorageCvRef = FirebaseStorage.getInstance().getReference().child("cv");
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void uploadUserProfileImage(Intent data) {
        Uri uri = data.getData();
        StorageReference imageRef = mStorageRef.child(mUserId + uri.getLastPathSegment());

        uploadTask = imageRef.putFile(uri);

         uploadTask.continueWithTask(task -> {
             if (!task.isSuccessful()) {
                 throw task.getException();
             }
             return imageRef.getDownloadUrl();
         }).addOnCompleteListener(task -> {
             if (task.isSuccessful()) {
                 Uri downloadUri = task.getResult();
                 listener.onImageUploadedSuccess(downloadUri.toString());
             } else {
                listener.onImageUploadingError(task.getException().getMessage());
             }
         });
    }

    public void uploadUserCv(Intent data) {
        Uri uri = data.getData();
        StorageReference cvRef = mStorageCvRef.child(mUserId + uri.getLastPathSegment());

        uploadTask = cvRef.putFile(uri);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return cvRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                listener.onCvUploadedSuccess(downloadUri.toString());
            } else {
                listener.onCvUploadingError(task.getException().getMessage());
            }
        });
    }
}
