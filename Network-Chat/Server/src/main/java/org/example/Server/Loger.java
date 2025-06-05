package org.example.Server;

import org.example.Message.Message;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Loger {
    private BufferedWriter writer;
    private String log;

    public Loger() {
        try {
            writer = new BufferedWriter(new FileWriter("src/main/resources/log.log", true));
        }
        catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    private void writeLog() {
        try {
            writer.write(log);
            writer.newLine();
            writer.flush();
        }
        catch (Exception e) {
            System.err.println("Failed write");
        }
    }

    public void wasGetMessage(Message message) {
        log = message.getSendingTime() + " : " + message.getSender() + " get message";
        writeLog();
    }

    public void sendMessage(Message message) {
        log = message.getSendingTime() + " : " + message.getSender() + " send message";
        writeLog();
    }

    public void connectUser(String string) {
        log = string + " connected";
        writeLog();
    }

    public void deleteUser(String user) {
        log = user + " deleted";
        writeLog();

    }

    public void sendListUsers() {
        log = "Send list users";
        writeLog();
    }
}
