package com.great.topnote.data;

public class Message {
    private String id ,Message , Time;

    public Message() {
    }

    public Message(String id, String message, String time) {
        this.id = id;
        Message = message;
        Time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
