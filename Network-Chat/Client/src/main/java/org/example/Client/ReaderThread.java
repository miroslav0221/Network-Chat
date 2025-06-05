package org.example.Client;

import org.example.Message.Message;
import org.example.View.ClientWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Objects;

public class ReaderThread extends Thread {
    private DataInputStream in;
    private ClientWindow window;
    private boolean isWorking;
    private String username;

    public ReaderThread(DataInputStream in, ClientWindow window, String username) {
        this.in = in;
        this.window = window;
        this.username = username;
    }


    public void stopWorking() {
        isWorking = false;
    }

    private String readXml() throws IOException {
        int length = in.readInt();
        byte[] buf = new byte[length];
        in.readFully(buf);
        return new String(buf, "UTF-8");
    }

    private Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }

    private void processMessage(Document document) {
        Element cmd = document.getDocumentElement();
        String name = cmd.getAttribute("name");
        if (Objects.equals(name, "message")) {
            String text = cmd.getElementsByTagName("text").item(0).getTextContent();
            String time = cmd.getElementsByTagName("time").item(0).getTextContent();
            String sender = cmd.getElementsByTagName("sender").item(0).getTextContent();
            String recipient = cmd.getElementsByTagName("recipient").item(0).getTextContent();
            if(recipient.equals(" ") || recipient.equals(username) || sender.equals(username)) {
                window.addMessage(new Message(sender, text, time, recipient), username);
            }
        }
        else if (Objects.equals(name, "userlist")) {
            ArrayList<String> usernames = new ArrayList<>();
            var userNodes = cmd.getElementsByTagName("user");
            for (int i = 0; i < userNodes.getLength(); i++) {
                String user = userNodes.item(i).getTextContent();
                if (user != null && !user.isEmpty()) {
                    usernames.add(user);
                }
            }
            window.updateUserList(usernames);
        }
        else if(Objects.equals(name, "messagelist")) {
            var textNodes = cmd.getElementsByTagName("text");
            var senderNodes = cmd.getElementsByTagName("sender");
            var timeNodes = cmd.getElementsByTagName("time");
            var recipientNodes = cmd.getElementsByTagName("recipient");

            for (int i = 0; i < textNodes.getLength(); i++) {
                String text = textNodes.item(i).getTextContent();
                String sender = senderNodes.item(i).getTextContent();
                String time = timeNodes.item(i).getTextContent();
                String recipient = recipientNodes.item(i).getTextContent();
                if (sender != null && text != null && time != null && recipient != null) {
                    if(recipient.equals(" ") || recipient.equals(username) || sender.equals(username)) {
                        window.addMessage(new Message(sender, text, time, recipient), username);
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        isWorking = true;
        while (isWorking) {
            try {
                Document doc = parseXml(readXml());
                processMessage(doc);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
