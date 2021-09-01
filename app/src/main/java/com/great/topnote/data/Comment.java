package com.great.topnote.data;

public class Comment {
    String UserName,UserId,Note;

    public Comment(String userName, String userId, String note) {
        UserName = userName;
        UserId = userId;
        Note = note;
    }

    public Comment() {
    }


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
