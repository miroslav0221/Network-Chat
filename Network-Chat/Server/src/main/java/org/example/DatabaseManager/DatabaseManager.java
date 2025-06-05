package org.example.DatabaseManager;

import org.example.Message.Message;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
public class DatabaseManager {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1234";

    private Connection connection;

    public DatabaseManager() {
        try {
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            createTablesIfNotExist();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            System.exit(1);
        }
    }


    private void createTablesIfNotExist() throws SQLException {
        System.out.println("print try");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS messages (" +
                    "message_id SERIAL PRIMARY KEY, " +
                    "sender TEXT NOT NULL, " +
                    "recipient TEXT NOT NULL, " +
                    "content TEXT NOT NULL, " +
                    "sent_at TEXT NOT NULL)");
        } catch (Exception e) {
            System.err.println("Error create bd: " + e.getMessage());
        }
        System.out.println("yes2");
    }


    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();

        String sql = "SELECT sender, recipient, content, sent_at FROM messages ORDER BY sent_at";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String text = rs.getString("content");
                String sender = rs.getString("sender");
                String recipient = rs.getString("recipient");
                String time = rs.getString("sent_at");

                Message message = new Message(sender, text, time, recipient);
                messages.add(message);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
        }

        return messages;
    }







    public void saveMessage(Message message) throws SQLException {
        String sender = message.getSender();
        String recipient = message.getRecipient();
        String content = message.getText();
        String sentAt = message.getSendingTime();

        String sql = "INSERT INTO messages (sender, recipient, content, sent_at) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sender);
            stmt.setString(2, recipient);
            stmt.setString(3, content);
            stmt.setString(4, sentAt);
            stmt.executeUpdate();
        }
    }




}
