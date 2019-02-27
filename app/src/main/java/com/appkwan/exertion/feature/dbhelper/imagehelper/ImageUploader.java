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
    private UploadTask uploadTask;

    public ImageUploader(OnImageUploaderListener listener) {
        this.listener = listener;
        mStorageRef = FirebaseStorage.getInstance().getReference().child("profile_images");
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void uploadUserProfileImage(Intent data) {
        Uri uri = data.getData();
        StorageReference riversRef = mStorageRef.child(mUserId + uri.getLastPathSegment());

        uploadTask = riversRef.putFile(uri);

         uploadTask.continueWithTask(task -> {
             if (!task.isSuccessful()) {
                 throw task.getException();
             }
             return riversRef.getDownloadUrl();
         }).addOnCompleteListener(task -> {
             if (task.isSuccessful()) {
                 Uri downloadUri = task.getResult();
                 listener.onImageUploadedSuccess(downloadUri.toString());
             } else {
                listener.onImageUploadingError(task.getException().getMessage());
             }
         });
    }
}
