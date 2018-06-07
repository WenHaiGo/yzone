package com.yzone.model;

import java.util.Date;

public class Message {

    int id;
    int uid;
    int otherUid;
    //表示发表的内容
    String send;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String userName;
    int isRead;
    Date createTime;

    Date modifyTime;

    public int getOtherUid() {
        return otherUid;
    }

    public void setOtherUid(int otherUid) {
        this.otherUid = otherUid;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }


    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }


}
