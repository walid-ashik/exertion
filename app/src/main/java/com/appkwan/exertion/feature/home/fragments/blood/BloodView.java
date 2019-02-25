package com.appkwan.exertion.feature.home.fragments.blood;

import com.appkwan.exertion.feature.home.fragments.Post;

import java.util.List;

public interface BloodView {
    void onPostLoaded(List<Post> postList);

    void onPostLoadingError(String message);
}
