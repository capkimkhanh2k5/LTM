package OnCK.CK2023.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class server {
    private static final ConcurrentHashMap<SocketAddress, AtomicInteger> clientCounters = new ConcurrentHashMap<>();

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

                serverHandle handle = new serverHandle(socket, packet, clientCounters);
                executorService.execute(handle);
            }

        } catch (Exception e) {
            System.out.println("Error in server main loop!");
            e.printStackTrace();
        }
    }
}

class serverHandle extends Thread {
    private DatagramSocket socket;
    private DatagramPacket packet;
    private ConcurrentHashMap<SocketAddress, AtomicInteger> counters;

    public serverHandle(DatagramSocket socket, DatagramPacket packet, ConcurrentHashMap<SocketAddress, AtomicInteger> counters) {
        this.socket = socket;
        this.packet = packet;
        this.counters = counters;
    }

    @Override
    public void run() {
        try {
            SocketAddress clientAddress = packet.getSocketAddress();
            String receivedData = new String(packet.getData(), 0, packet.getLength()).trim();

            System.out.println("Server received '" + receivedData + "' from " + clientAddress);

            if (receivedData.equals("1")) {
                counters.remove(clientAddress);
                System.out.println("Client " + clientAddress + " has finished. Counter removed.");
            } else {
                // Dùng computeIfAbsent để lấy hoặc tạo mới bộ đếm cho client một cách an toàn.
                AtomicInteger m = counters.computeIfAbsent(clientAddress, k -> new AtomicInteger(2));

                int currentValue = m.getAndIncrement();
                
                byte[] buffer = String.valueOf(currentValue).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                
                socket.send(sendPacket);
            }
        } catch (Exception e) {
            System.out.println("Error in serverHandle for " + packet.getSocketAddress());
            e.printStackTrace();
        }
    }
}