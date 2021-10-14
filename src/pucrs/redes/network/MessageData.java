package pucrs.redes.network;

public class MessageData {
    private String data;
    private MessageType type;

    public MessageData(String data, MessageType type) {
        this.data = data;
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public MessageType getType() {
        return type;
    }
}
