package pucrs.redes.server;

import pucrs.redes.network.Message;
import pucrs.redes.network.NetworkPacketManager;

public class Server extends Thread {

    private NetworkPacketManager networkPacketManeger;

    public Server(NetworkPacketManager networkPacketManeger) {
        super("Server Thread");
        this.networkPacketManeger = networkPacketManeger;
    }

    @Override
    public void run() {
        while (true) {
            if (networkPacketManeger.hasMessageToCompute()) {
                Message message = networkPacketManeger.getNextMessage();

            }
        }
    }
}
