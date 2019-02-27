package com.appkwan.exertion.feature.dbhelper.imagehelper;

public interface OnImageUploaderListener {
    void onImageUploadedSuccess(String imageUrl);

    void onImageUploadingError(String message);
}
