package pucrs.redes.network;

import java.io.IOException;
import java.net.*;
import java.util.*;

public abstract class NetworkPacketManager extends Thread {
    public static final Integer SERVER_PORT = 5321;
    private static final int DELAY_TO_RESEND_MESSAGE = 5000;
    private Map<String, TimerTask> messagesToResend;
    private static final int bufferSize = 2048;
    private Timer timer;
    private DatagramSocket socket;
    protected boolean shouldStop;

    protected NetworkPacketManager(int portToListen) {
        try {
            this.socket = new DatagramSocket(portToListen);
            shouldStop = false;
            messagesToResend = new HashMap<>();
            timer = new Timer();
        } catch (SocketException e) {
            System.out.println("Could not bind to port " + portToListen);
            e.printStackTrace();
        }
    }

    protected abstract void handleMessage(Message message) throws IOException;

    protected abstract void callWhenStop() throws IOException;

    public void sendMessage(MessageData message, InetSocketAddress recipient) throws IOException {
        byte[] data = message.toBytes();
        DatagramPacket packet = new DatagramPacket(data, 0, data.length, recipient);
        String id = message.getId();
        TimerTask messageToResendTask = createResendTask(new Message(recipient, message));
        messagesToResend.put(id, messageToResendTask);
        socket.send(packet);
        loadReSender(messageToResendTask);
    }

    public void stopListening() throws IOException {
        shouldStop = true;
        callWhenStop();
    }

    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(new byte[bufferSize], bufferSize);
        System.out.println("Will Start To Listen");
        while (!this.shouldStop) {
            try {
                socket.receive(packet);
                System.out.println("Packet received");
                byte[] byteData = packet.getData();
                String data = new String(trimData(byteData));
                InetSocketAddress socketAddress = new InetSocketAddress(packet.getAddress(), packet.getPort());
                Message message = new Message(socketAddress, data);
                handleReceivedPacket(message);
                packet = new DatagramPacket(new byte[bufferSize], bufferSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] trimData(byte[] byteData) {
        int i = byteData.length - 1;
        while (i >=0 && byteData[i] == 0) {
            i--;
        }
        return Arrays.copyOf(byteData, i +1);
    }

    private void handleReceivedPacket(Message message) throws IOException {
        if (message.getMessageData().getType() == MessageType.ACK) {
            handleAck(message);
        } else if (message.getMessageData().getType() == MessageType.STOP) {
            this.shouldStop = true;
            System.out.println("Will stop listener");
        } else {
            sendAck(message);
            handleMessage(message);
        }
    }

    private void handleAck(Message message) {
        String messageId = message.getMessageId();
        System.out.println("Removing ACK");
        messagesToResend.remove(messageId);
    }

    private void sendAck(Message message) throws IOException {
        MessageData messageToAck = MessageData.buildAckMessageFor(message.getMessageData().getId());
        byte[] data = messageToAck.toBytes();
        DatagramPacket packet = new DatagramPacket(data, 0, data.length, message.getFrom());
        socket.send(packet);
    }

    private void loadReSender(TimerTask messageToResendTask) {
        timer.schedule(messageToResendTask, DELAY_TO_RESEND_MESSAGE);
    }

    private TimerTask createResendTask(Message message) {
        return new TimerTask() {
            @Override
            public void run() {
                String messageId = message.getMessageId();
                if (messagesToResend.containsKey(messageId)) {
                    messagesToResend.remove(messageId);
                    try {
                        System.out.println("Will Resend data, waiting for ack");
                        sendMessage(MessageData.buildMessage(message.getMessageData().getData()), message.getFrom());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // task was cancelled.
            }
        };
    }
}
