package com.appkwan.exertion.feature.profile;

import com.appkwan.exertion.feature.home.User;

public interface ProfileView {
    void showUserDetails(User user);

    void onError(String message);

    void onImageUrlSavedSuccess();
}
