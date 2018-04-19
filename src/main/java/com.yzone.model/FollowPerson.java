package com.yzone.model;

import java.util.Date;

public class FollowPerson {

    int id;
    int uid;
    int followUid;
    Date modifyTime;
    Date createTime;

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

    public int getFollowUid() {
        return followUid;
    }

    public void setFollowUid(int followUid) {
        this.followUid = followUid;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
