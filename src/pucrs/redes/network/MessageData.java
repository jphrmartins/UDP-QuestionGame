package pucrs.redes.network;

import java.util.UUID;

public class MessageData {
    private String id;
    private String data;
    private MessageType type;

    private MessageData(String id, String data, MessageType type) {
        this.id = id;
        this.data = data;
        this.type = type;
    }

    public static MessageData buildMessage(String message) {
        return new MessageData(UUID.randomUUID().toString(), message, MessageType.MESSAGE);
    }

    public static MessageData buildAckMessageFor(String id) {
        return new MessageData(id, "", MessageType.ACK);
    }

    public static MessageData buildFrom(String data) {
        String[] messageData = data.split(";;");
        MessageType type = getMessageType(messageData[2]);
        return new MessageData(messageData[0], messageData[1], type);
    }

    public static MessageData buildStop() {
        return new MessageData("", "", MessageType.STOP);
    }

    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public MessageType getType() {
        return type;
    }

    public byte[] toBytes() {
        int type =  this.type.getValue();
        return (this.id + ";;" + this.data + ";;" + type).getBytes();
    }

    private static MessageType getMessageType(String intType) {
        int messageType = Integer.parseInt(intType);
        return MessageType.from(messageType);
    }

    @Override
    public String toString() {
        return "MessageData{" +
                "id='" + id + '\'' +
                ", data='" + data + '\'' +
                ", type=" + type +
                '}';
    }
}
