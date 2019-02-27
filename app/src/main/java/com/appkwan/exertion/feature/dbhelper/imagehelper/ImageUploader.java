package com.appkwan.exertion.feature.dbhelper.imagehelper;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImageUploader {

    OnImageUploaderListener listener;
    private StorageReference mStorageRef;

    public ImageUploader(OnImageUploaderListener listener) {
        this.listener = listener;
        mStorageRef = FirebaseStorage.getInstance().getReference().child("profile_images");
    }


}
