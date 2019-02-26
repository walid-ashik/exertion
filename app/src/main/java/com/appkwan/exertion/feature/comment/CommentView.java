package com.appkwan.exertion.feature.comment;

import java.util.List;

public interface CommentView {
    void onAllCommentsLoaded(List<Comment> mCommentList);

    void noCommentsFound();

    void onCommentSavedSuccess();

    void onCommentSavingError(String message);
}
