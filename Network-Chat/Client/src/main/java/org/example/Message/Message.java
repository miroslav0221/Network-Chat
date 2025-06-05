package org.example.Message;

import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private String recipient;
    private String text;
    private String sendingTime;

    public Message(String sender, String text, String sendingTime, String recipient) {
        this.sender = sender;
        this.sendingTime = sendingTime;
        this.text = text;
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public String getRecipient() {
        return recipient;
    }

}
