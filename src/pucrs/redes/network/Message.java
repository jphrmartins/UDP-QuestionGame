package pucrs.redes.network;

import java.net.InetAddress;

public class Message {
    private int port;
    private InetAddress from;
    private MessageData messageData;

    public Message(int port, InetAddress from, String data) {
        this.port = port;
        this.from = from;
        this.messageData = buildMessageData(data);
    }

    public Message(int port, InetAddress from, MessageData data) {
        this.port = port;
        this.from = from;
        this.messageData = data;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getFrom() {
        return from;
    }

    public MessageData getMessageData() {
        return messageData;
    }

    private MessageData buildMessageData(String data) {
        String[] messageData =  data.split(";;");
        MessageType type = getMessageType(messageData[1]);
        return new MessageData(messageData[0], type);
    }

    private MessageType getMessageType(String type) {
        int messageType = Integer.parseInt(type);
        return MessageType.from(messageType);
    }
}
