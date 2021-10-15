package pucrs.redes.client;

import pucrs.redes.network.Message;
import pucrs.redes.network.MessageData;
import pucrs.redes.network.NetworkPacketManager;
import pucrs.redes.network.ServerType;

import java.io.IOException;
import java.util.Scanner;

public class Client extends NetworkPacketManager {
    public Scanner scanner;
    public Client(int portToListen) {
        super(portToListen, ServerType.CLIENT);
        scanner = new Scanner(System.in);
    }

    @Override
    protected void handleMessage(Message message) throws IOException {
        System.out.println("Received from server: " + message.getMessageData().getData());
        System.out.print("InputData: ");
        String data = scanner.nextLine();
        sendMessage(MessageData.buildMessage(data), message.getFrom());
    }

    @Override
    protected void callWhenStop() throws IOException {

    }
}
