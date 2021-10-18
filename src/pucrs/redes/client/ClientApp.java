package pucrs.redes.client;

import java.io.IOException;
import java.net.InetSocketAddress;

import pucrs.redes.network.MessageData;
import pucrs.redes.network.NetworkPacketManager;

public class ClientApp {

    public static void main(String[] args) throws IOException {
        //int port = Integer.parseInt(args[0]);
        System.out.println("port" + 1235);
        Client client = new Client(1235);
        client.start();
        client.sendMessage(MessageData.buildMessage("init"), new InetSocketAddress(NetworkPacketManager.SERVER_PORT));
    }
}
