package pucrs.redes.client;

import pucrs.redes.network.MessageData;
import pucrs.redes.network.NetworkPacketManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class ClientApp {

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        System.out.println("port" + port);
        Client client = new Client(port);
        client.start();
        client.sendMessage(MessageData.buildMessage("start"), new InetSocketAddress(NetworkPacketManager.SERVER_PORT));
    }
}
