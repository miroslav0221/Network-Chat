package org.example.View;

import org.example.Message.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClientWindow extends JFrame {
    private UserListPanel userListPanel;
    private JPanel messagePanel;
    private JTextField inputField;
    private JButton sendButton;
    private ClientWindowListener listener;

    private JPopupMenu whisperMenu;
    private boolean whisperMode = false;

    public ClientWindow() {
        setTitle("Chat Client");
        setSize(700, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        userListPanel = new UserListPanel();
        userListPanel.setPreferredSize(new Dimension(200, 0));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, userListPanel);
        splitPane.setDividerLocation(500);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        whisperMenu = new JPopupMenu();

        inputField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void checkWhisperTrigger() {
                String text = inputField.getText().trim();
                if (text.equals("/w")) {
                    whisperMode = true;
                    showWhisperUserMenu();
                } else {
                    whisperMode = false;
                    whisperMenu.setVisible(false);
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { checkWhisperTrigger(); }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { checkWhisperTrigger(); }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });

        sendButton.addActionListener(e -> handleSend());
        inputField.addActionListener(e -> handleSend());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (listener != null) {
                    listener.stopWorking();
                }
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void handleSend() {
        String text = inputField.getText();
        if (!text.isEmpty() && listener != null) {
            listener.sendMessage(text);
            inputField.setText("");
        }
        whisperMenu.setVisible(false);
    }

    public void addMessage(Message message, String recipient) {
        String username = message.getSender();
        String text = message.getText();
        String time = message.getSendingTime();
        StringBuilder sb = new StringBuilder(username);

        boolean isOwner = username.equals(userListPanel.getCurrentUsername());
        if(recipient.equals(message.getRecipient()) || (recipient.equals(message.getSender()) && !message.getRecipient().equals(" "))) {
            sb.append("(private)");
        }

        MessagePanel messagePanelComponent = new MessagePanel(sb.toString(), text, time, isOwner);
        messagePanelComponent.setAlignmentX(Component.LEFT_ALIGNMENT);

        messagePanel.add(messagePanelComponent);
        messagePanel.revalidate();
        messagePanel.repaint();

        JScrollBar vertical = ((JScrollPane) messagePanel.getParent().getParent()).getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public void updateUserList(List<String> users) {
        userListPanel.updateUsers(users);
    }

    public void setListener(ClientWindowListener listener) {
        this.listener = listener;
    }

    public void setCurrentUsername(String username) {
        userListPanel.setCurrentUsername(username);
    }

    private void showWhisperUserMenu() {
        whisperMenu.removeAll();

        List<String> users = userListPanel.getUserList();
        String currentUser = userListPanel.getCurrentUsername();

        if (users == null) return;

        for (String user : users) {
            if (!user.equals(currentUser)) {
                JMenuItem item = new JMenuItem(user);
                item.addActionListener(e -> {
                    inputField.setText("/w " + user + " ");
                    inputField.requestFocus();
                    whisperMenu.setVisible(false);
                });
                whisperMenu.add(item);
            }
        }

        whisperMenu.show(inputField, 0, -whisperMenu.getPreferredSize().height);
    }
}
