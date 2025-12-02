package OnCK.Practice.Bai5.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class server {
    // Tao lich su va bo dem thoi gian
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // Tao gia khoi diem
    private static AtomicInteger startingPrice = new AtomicInteger(50);

    // Danh sach clients (IP:Port) de broadcast
    // Su dung Set de tranh trung lap. Format string: "IP:Port"
    private static Set<String> clients = Collections.synchronizedSet(new HashSet<>());

    // Thoi diem bat dau vong dau gia
    private static AtomicInteger roundStartTime = new AtomicInteger((int) (System.currentTimeMillis() / 1000));

    // Nguoi thang hien tai
    private static String currentWinner = "Chua co";

    private static DatagramSocket socket;

    public static void main(String[] args) {
        try {
            socket = new DatagramSocket(5001);
            System.out.println("UDP Server is running on port 5001...");

            // Khoi tao bo dem nguoc thoi gian 60s
            scheduler.scheduleAtFixedRate(() -> {
                System.out.println("[TIMER] 60s cycle - End round");
                endRound();
            }, 60, 60, TimeUnit.SECONDS);

            // Timer bao thoi gian con lai moi 5 giay
            scheduler.scheduleAtFixedRate(() -> {
                int elapsed = (int) (System.currentTimeMillis() / 1000) - roundStartTime.get();
                int remaining = 60 - elapsed;

                if (remaining > 0 && remaining <= 60) {
                    String message = "[TIMER] Thoi gian con lai: " + remaining + "s | Gia hien tai: "
                            + startingPrice.get() + "d";
                    System.out.println(message);
                    broadcastMessage(message);
                }
            }, 5, 5, TimeUnit.SECONDS);

            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String clientKey = IPAddress.getHostAddress() + ":" + port;

                System.out.println("Received from " + clientKey + ": " + message);

                // Them client vao danh sach neu chua co
                if (clients.add(clientKey)) {
                    System.out.println("[SERVER] New client added: " + clientKey);
                }

                // Xu ly message
                processMessage(message, IPAddress, port, clientKey);
            }
        } catch (Exception e) {
            System.out.println("Error in server!");
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private static void processMessage(String message, InetAddress IPAddress, int port, String clientKey) {
        // Neu la message "JOIN" (client moi ket noi)
        if (message.equals("JOIN")) {
            String welcomeMsg = "[SERVER] Chao mung den phien dau gia! Gia hien tai: " + startingPrice.get() + "d";
            sendToClient(welcomeMsg, IPAddress, port);
            return;
        }

        // Xu ly dau gia
        int price = getPrice(message);
        int currentPrice = startingPrice.get();

        if (price == -1 || price <= currentPrice) {
            String response = "Gia khong hop le! Gia hien tai: " + currentPrice + "d";
            sendToClient(response, IPAddress, port);
            System.out.println("Server sent to " + clientKey + ": " + response);
        } else {
            // Cap nhat gia va nguoi thang
            updateStartingPrice(price);
            setCurrentWinner(clientKey);

            // Broadcast cho tat ca clients
            String broadcastMsg = "[DAU GIA MOI] " + clientKey + " da dau gia " + price + "d!";
            broadcastMessage(broadcastMsg);

            // Gui phan hoi rieng cho nguoi dau gia thanh cong
            String response = "Dau gia thanh cong! Gia hien tai: " + price + "d";
            sendToClient(response, IPAddress, port);
            System.out.println("Server sent to " + clientKey + ": " + response);
        }
    }

    // Ket thuc vong dau
    private static void endRound() {
        System.out.println("[SERVER] Round ended!");

        // Xac dinh nguoi thang
        if (!currentWinner.equals("Chua co")) {
            String winMsg = "[KET THUC] Chuc mung! Ban da thang voi gia: " + startingPrice.get() + "d";
            String loseMsg = "[KET THUC] Ban da thua! Nguoi thang: " + currentWinner + " voi gia: "
                    + startingPrice.get() + "d";

            // Gui thong bao cho tung client
            for (String clientKey : clients) {
                try {
                    String[] parts = clientKey.split(":");
                    InetAddress ip = InetAddress.getByName(parts[0]);
                    int port = Integer.parseInt(parts[1]);

                    if (clientKey.equals(currentWinner)) {
                        sendToClient(winMsg, ip, port);
                    } else {
                        sendToClient(loseMsg, ip, port);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Khong co ai dau gia
            String noWinnerMsg = "[KET THUC] Khong co ai dau gia trong vong nay!";
            broadcastMessage(noWinnerMsg);
        }

        // Reset cho vong moi (giu lai danh sach clients de ho tiep tuc choi)
        // Hoac co the clear neu muon bat buoc join lai. O day ta giu lai.
        // clients.clear();
        newRound();
    }

    // Tao vong dau moi
    private static void newRound() {
        startingPrice.set(50);
        roundStartTime.set((int) (System.currentTimeMillis() / 1000));
        currentWinner = "Chua co";
        System.out.println("[SERVER] New round - Starting price: " + startingPrice.get());
        broadcastMessage("[SERVER] Vong dau gia moi bat dau! Gia khoi diem: 50d");
    }

    // Broadcast message cho tat ca clients
    public static void broadcastMessage(String message) {
        byte[] sendData = message.getBytes();
        for (String clientKey : clients) {
            try {
                String[] parts = clientKey.split(":");
                InetAddress ip = InetAddress.getByName(parts[0]);
                int port = Integer.parseInt(parts[1]);

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
                socket.send(sendPacket);
            } catch (Exception e) {
                System.out.println("Error broadcasting to " + clientKey);
            }
        }
    }

    // Gui message cho 1 client cu the
    public static void sendToClient(String message, InetAddress ip, int port) {
        try {
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getPrice(String price) {
        try {
            return Integer.parseInt(price);
        } catch (Exception e) {
            return -1;
        }
    }

    public static void setCurrentWinner(String winner) {
        currentWinner = winner;
    }

    public static void updateStartingPrice(int price) {
        startingPrice.set(price);
    }
}