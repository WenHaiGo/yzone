package com.yzone.service.impl;

import com.yzone.dao.MessageDao;
import com.yzone.model.Message;
import com.yzone.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageDao messageDao;
    @Override
    public int save(Message message) {
        return messageDao.save(message);
    }

    @Override
    public int updateReadStateByUid(int uid) {
        System.out.println("数据库总的是"+uid);
       int a  = messageDao.updateReadStateByUid(uid);
        return a;
    }

    @Override
    public List<Message> checkUnreadByUid(int otherUid) {


        return messageDao.checkUnreadByUid(otherUid);
    }
}
