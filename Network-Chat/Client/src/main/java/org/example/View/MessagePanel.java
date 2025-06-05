//package org.example.View;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class MessagePanel extends JPanel {
//    public MessagePanel(String username, String text, String time) {
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//
//        JLabel userLabel = new JLabel(time + " " + username);
//        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
//
//        JLabel messageLabel = new JLabel(text);
//        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//
//        add(userLabel);
//        add(Box.createRigidArea(new Dimension(0, 5)));
//        add(messageLabel);
//
//        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//    }
//}
package org.example.View;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MessagePanel extends JPanel {
    private final Color backgroundColor;
    private final boolean isOwnMessage;
    private final JPanel contentPanel;

    public MessagePanel(String username, String text, String time, boolean isOwnMessage) {
        this.isOwnMessage = isOwnMessage;
        this.backgroundColor = isOwnMessage ? new Color(225, 245, 254) : new Color(250, 250, 250);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        if (isOwnMessage) add(Box.createHorizontalGlue());

        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 20;
                g2.setColor(backgroundColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));
                g2.dispose();
            }
        };
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        contentPanel.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));

        JLabel userLabel = new JLabel(time + "  " + username);
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userLabel.setForeground(new Color(100, 100, 100));

        JTextArea messageArea = new JTextArea(text);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setEditable(false);
        messageArea.setOpaque(false);
        messageArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(userLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(messageArea);

        add(contentPanel);

        if (!isOwnMessage) add(Box.createHorizontalGlue());
    }
}

