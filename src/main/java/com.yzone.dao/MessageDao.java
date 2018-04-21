package com.yzone.dao;

import com.yzone.model.Message;

import java.util.List;

public interface MessageDao {
    int save(Message message);

    int updateReadStateByUid(int uid);

    List<Message> checkUnreadByUid(int otherUid);

}
