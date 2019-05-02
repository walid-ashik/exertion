package com.appkwan.exertion.feature.ratinghistory;

import java.util.List;

public interface RatingView {
    void showEmptyView();
    void onRatingLoaded(List<Rating> mRatingList);
}
