package com.appkwan.exertion.feature.message.allmessage;

import com.appkwan.exertion.feature.message.model.Message;

import java.util.ArrayList;

public interface AllMessageView {
    void onMessagedUserListLoaded(ArrayList<String> mMessageThreadList);

    void onMessageThreadAdded(ArrayList<String> mMessageThreadList);

    void onLastMessageLoaded(Message message);
}
