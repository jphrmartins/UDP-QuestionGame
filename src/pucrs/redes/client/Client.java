package pucrs.redes.client;

import pucrs.redes.network.*;

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
        System.out.println("Received from server: \n\n" + message.getMessageData().getData() + "\n\n");
        if (message.getMessageData().getType() != MessageType.STOP_GAME) {
            System.out.print("InputData: ");
            String data = scanner.nextLine();
            sendMessage(MessageData.buildMessage(data), message.getFrom());
        }
    }
}
