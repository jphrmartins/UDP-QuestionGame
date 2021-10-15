package pucrs.redes.network;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Message {
    private InetSocketAddress from;
    private MessageData messageData;

    public Message(InetSocketAddress inetSocketAddress, String data) {
        this.from = inetSocketAddress;
        this.messageData = MessageData.buildFrom(data);
    }

    public Message(InetSocketAddress inetSocketAddress, MessageData data) {
        this.from = inetSocketAddress;
        this.messageData = data;
    }

    public InetSocketAddress getFrom() {
        return from;
    }

    public MessageData getMessageData() {
        return messageData;
    }

    public byte[] getByteMessage() {
        return messageData.toBytes();
    }

    public String getMessageId() {
        return messageData.getId();
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageData=" + messageData +
                '}';
    }
}
