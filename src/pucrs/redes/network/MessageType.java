package pucrs.redes.network;

public enum MessageType {
    STOP(0),
    CLIENT_MESSAGE(1),
    SERVER_MESSAGE(2),
    CLIENT_STOP(3);

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
