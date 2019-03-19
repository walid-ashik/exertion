package com.appkwan.exertion.feature.profile;

import com.appkwan.exertion.feature.home.User;

public interface ProfileView {
    void showUserDetails(User user);

    void onError(String message);

    void onImageUrlSavedSuccess();

    void onCvUrlSavedSuccess();

    void onRatingLoaded(double rating, int ratingCount);

    void otherUserRatedThisUser(boolean isOtherUserRated);

    void onRatingSuccess();

    void onRatingError(String message);
}
