package pucrs.redes.server;

import pucrs.redes.network.Message;
import pucrs.redes.network.MessageData;
import pucrs.redes.network.NetworkPacketManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Server extends NetworkPacketManager {

    public Map<InetSocketAddress, CurrentPlayerState> states;

    public Server() {
        super(NetworkPacketManager.SERVER_PORT);
        states = new HashMap<>();
    }

    @Override
    protected void handleMessage(Message message) throws IOException {
        String messageData = message.getMessageData().getData();
        System.out.println(messageData);
        sendMessage(MessageData.buildMessage("Received"), message.getFrom());
    }

    @Override
    protected void callWhenStop() throws IOException {

    }
}
