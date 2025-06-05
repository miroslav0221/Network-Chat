package org.example.Server;

import org.example.DatabaseManager.DatabaseManager;
import org.example.Message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private final int port = 8080;
    private int countUsers;
    public static final ArrayList<String> usersList = new ArrayList<>();
    public static final ArrayList<UserThread> userThreads = new ArrayList<>();
    public static final Loger loger = new Loger();


    public Server() {
        countUsers = 0;
    }


    public void onServer() throws IOException {
        DatabaseManager DB = new DatabaseManager();
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                try {
                    System.out.println("print");
                    Socket socket = server.accept();
                    UserThread userThread = new UserThread(socket, DB);
                    userThreads.add(userThread);
                    countUsers++;
                    System.out.println("Произошло подключение");
                    userThread.start();
                } catch (Exception e) {
                    System.out.println("Failed create socket");
                }

            }
        } catch (Exception e) {
            System.err.println("Failed connection");
        }
    }

}
