package com.appkwan.exertion.feature.message;

import com.appkwan.exertion.feature.home.User;

public interface MessageView {
    void onMessagingUserIdLoaded(String messagingUserId);

    void onMessageUserDetailsLoaded(User messageingUser);
}
