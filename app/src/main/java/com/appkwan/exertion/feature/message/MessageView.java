package com.appkwan.exertion.feature.message;

import com.appkwan.exertion.feature.home.User;
import com.appkwan.exertion.feature.message.model.Message;

import java.util.List;

public interface MessageView {
    void onMessagingUserIdLoaded(String messagingUserId);

    void onMessageUserDetailsLoaded(User messageingUser);

    void onMessageThreadLoaded(String messageThreadId);

    void onMessageThreadNotFound();

    void onMessagesLoaded(List<Message> messageList);

    void onMessageSavingError(String message);

    void onMessageSavedSuccess();
}
