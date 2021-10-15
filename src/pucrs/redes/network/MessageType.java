package pucrs.redes.network;

public enum MessageType {
    STOP(0),
    MESSAGE(1),
    ACK(2),
    STOP_SERVER(3);

    private int value;

    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MessageType from(int number) {
        for (MessageType type: values()) {
            if (type.getValue() == number) return type;
        }
        return MessageType.STOP;
    }
}
