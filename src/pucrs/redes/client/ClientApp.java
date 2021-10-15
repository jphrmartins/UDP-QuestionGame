package pucrs.redes.client;

import pucrs.redes.network.MessageData;
import pucrs.redes.network.NetworkPacketManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class ClientApp {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Client client = new Client(1235);
        client.start();
        System.out.println("Send Message to server: ");
        String data = scanner.nextLine();
        client.sendMessage(MessageData.buildMessage(data), new InetSocketAddress(NetworkPacketManager.SERVER_PORT));
    }
}
