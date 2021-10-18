package pucrs.redes.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import pucrs.redes.network.MessageData;
import pucrs.redes.network.NetworkPacketManager;

public class ClientApp {

    public static void main(String[] args) throws IOException, InterruptedException {
        Client client;
        if (args.length != 0) {
            int port = Integer.parseInt(args[0]);
            System.out.println("port " + port);
            client = new Client(port);
        } else {
            client = new Client(1235);
        }
        client.start();
        InetSocketAddress serverAddress = new InetSocketAddress(InetAddress.getByName("127.0.0.1"),
                NetworkPacketManager.SERVER_PORT);
        client.sendMessage(MessageData.buildMessage("init"), serverAddress);
        client.join();
        System.out.println("Client stops");
        System.exit(2);
    }
}
