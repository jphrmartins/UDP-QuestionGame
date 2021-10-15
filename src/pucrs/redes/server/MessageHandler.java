package pucrs.redes.server;

import pucrs.redes.network.MessageData;

public interface MessageHandler {

    boolean mustHandle(MessageData data, CurrentPlayerState playerState);

    String handle(MessageData data, CurrentPlayerState playerState);
}
