package com.appkwan.exertion.feature.newpost;

public interface NewPostView {
    void showUserName(String name);

    void onPostSuccess();

    void onPostDatabaseError(String message);

    void hideLoader();

    void showLoader();

    void loadUserImage(String profile_image);
}
