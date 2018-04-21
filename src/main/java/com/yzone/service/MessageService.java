package com.yzone.service;

import com.yzone.model.Message;

import java.util.List;

public interface MessageService {
    int save(Message message);
    int updateReadStateByUid(int uid);

    List<Message> checkUnreadByUid(int otherUid);

}
