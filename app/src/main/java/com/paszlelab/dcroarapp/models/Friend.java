package com.paszlelab.dcroarapp.models;

import java.io.Serializable;

public class Friend implements Serializable {

    private String userId, friendFirst, friendLast, friendEmail, friendId, id;
    private Boolean isAdded;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendFirst() {
        return friendFirst;
    }

    public void setFriendFirst(String friendFirst) {
        this.friendFirst = friendFirst;
    }

    public String getFriendLast() {
        return friendLast;
    }

    public void setFriendLast(String friendLast) {
        this.friendLast = friendLast;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getAdded() {
        return isAdded;
    }

    public void setAdded(Boolean added) {
        isAdded = added;
    }
}
