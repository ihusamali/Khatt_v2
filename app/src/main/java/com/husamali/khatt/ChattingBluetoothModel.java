package com.husamali.khatt;

public class ChattingBluetoothModel {
    private String sender;
    private String receiver;
    private String message;

    public ChattingBluetoothModel(String sender, String receiver, String msg) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = msg;
    }

    public ChattingBluetoothModel() {
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }
}
