package pucrs.redes.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;

public class NetworkPacketManager extends Thread {

    private static final int bufferSize = 65000;
    private DatagramSocket socket;
    private Queue<Message> messageQueue;
    private boolean shouldStop;

    public NetworkPacketManager(int portToListen) {
        try {
            this.socket = new DatagramSocket(portToListen);
            messageQueue = new LinkedList<>();
            shouldStop = false;
        } catch (SocketException e) {
            System.out.println("Could not bind to port " + portToListen);
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) throws SocketException {
        //just send The message
    }

    public void gracefulStop() {
        //Stop to server
    }

    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(new byte[bufferSize], bufferSize);
        while (!this.shouldStop) {
            try {
                socket.receive(packet);
                String data = new String(packet.getData());
                Message message = new Message(packet.getPort(), packet.getAddress(), data);
                handleMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(Message message) {
        if (message.getMessageData().getType() == MessageType.STOP) {
            this.shouldStop = true;
            System.out.println("Will stop listener");
        } else {
            messageQueue.add(message);
        }
    }

    public boolean hasMessageToCompute() {
        return !messageQueue.isEmpty();
    }

    public Message getNextMessage() {
        return messageQueue.poll();
    }
}
