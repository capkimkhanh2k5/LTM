package OnCK.Practice.Bai6.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    private static final List<String> chatHistory = Collections.synchronizedList(new ArrayList<>());
    private static final Set<SocketAddress> clientAddresses = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        new server();
    }

    public server() {
        try {
            System.out.println("Server is running in port 5000...");
            DatagramSocket socket = new DatagramSocket(5000);

            ExecutorService executorService = Executors.newCachedThreadPool();

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                serverHandle handle = new serverHandle(socket, packet, chatHistory, clientAddresses);
                executorService.execute(handle);
            }

        } catch (Exception e) {
            System.out.println("Error in server!");
            e.printStackTrace();
        }
    }
    
    public static List<String> getList() {
        return chatHistory;
    }
}

class serverHandle implements Runnable {
    private final DatagramSocket socket;
    private final DatagramPacket packet;
    private final List<String> chatHistory;
    private final Set<SocketAddress> clientAddresses;

    public serverHandle(DatagramSocket socket, DatagramPacket packet, List<String> chatHistory, Set<SocketAddress> clientAddresses) {
        this.socket = socket;
        this.packet = packet;
        this.chatHistory = chatHistory;
        this.clientAddresses = clientAddresses;
    }

    @Override
    public void run() {
        try {
            // Returns 'true' if the client is new.
            boolean isNewClient = clientAddresses.add(packet.getSocketAddress());

            if (isNewClient && !chatHistory.isEmpty()) {
                sendHistory(packet.getSocketAddress());
            }
            
            String msg = new String(packet.getData(), 0, packet.getLength()).trim();
            if (msg.isEmpty()) {
                return;
            }

            String formattedMsg = "[" + packet.getAddress().getHostAddress() + "] " + msg;
            chatHistory.add(formattedMsg);
            sendToAllClients(formattedMsg);

        } catch (Exception e) {
            System.out.println("Error in serverHandle!");
            e.printStackTrace();
        }
    }

    private void sendHistory(SocketAddress clientAddr) throws IOException {
        System.out.println("New client connected: " + clientAddr + ". Sending chat history...");
        List<String> historySnapshot;

        synchronized (chatHistory) {
            historySnapshot = new ArrayList<>(chatHistory);
        }

        String fullHistory = "-- Chat History --\n" + String.join("\n", historySnapshot) + "\n-- End of History --";
        
        byte[] buffer = fullHistory.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, clientAddr);
        socket.send(sendPacket);
    }

    private void sendToAllClients(String msg) {
        byte[] buffer = msg.getBytes();
        
        Set<SocketAddress> clients;
        synchronized(clientAddresses) {
            clients = new HashSet<>(clientAddresses);
        }

        for (SocketAddress clientAddr : clients) {
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, clientAddr);
            try {
                socket.send(sendPacket);
            } catch (IOException e) {
                System.out.println("Error sending packet to " + clientAddr);
                e.printStackTrace();
            }
        }
    }
}