package OnCK.Practice.Bai5.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class server {
    // Tao lich su va bo dem thoi gian
    private static List<String> history = new CopyOnWriteArrayList<>();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // Tao gia khoi diem
    private static AtomicInteger startingPrice = new AtomicInteger(50);

    // Danh sach clients dang ket noi (de broadcast)
    private static List<DataOutputStream> clients = new CopyOnWriteArrayList<>();

    // Thoi diem bat dau vong dau gia
    private static AtomicInteger roundStartTime = new AtomicInteger((int) (System.currentTimeMillis() / 1000));

    // Nguoi thang hien tai
    private static String currentWinner = "Chua co";

    public static void main(String[] args) {
        try {
            // Khoi tao bo dem nguoc thoi gian 60s
            scheduler.scheduleAtFixedRate(() -> {
                System.out.println("[TIMER] 60s cycle - New round");
                newRound();
            }, 60, 60, TimeUnit.SECONDS);

            // Timer bao thoi gian con lai moi 5 giay
            scheduler.scheduleAtFixedRate(() -> {
                int elapsed = (int) (System.currentTimeMillis() / 1000) - roundStartTime.get();
                int remaining = 60 - elapsed;

                if (remaining > 0 && remaining <= 60) {
                    String message = "[TIMER] Thoi gian con lai: " + remaining + "s | Gia hien tai: "
                            + startingPrice.get() + "d | Nguoi dan dau: " + currentWinner;
                    System.out.println(message);
                    broadcastMessage(message);
                }
            }, 5, 5, TimeUnit.SECONDS);

            ServerSocket serverSocket = new ServerSocket(5001);
            ExecutorService executorService = Executors.newCachedThreadPool();
            System.out.println("Server is running on port 5001...");

            while (true) {
                Socket soc = serverSocket.accept();
                System.out.println("Client connected: " + soc.getInetAddress().getHostAddress() + ":" + soc.getPort());

                serverHandle handle = new serverHandle(soc);
                executorService.execute(handle);
            }
        } catch (Exception e) {
            System.out.println("Error in server!");
            e.printStackTrace();
        }
    }

    // Tao vong dau moi
    private static void newRound() {
        history.clear();
        startingPrice.set(50);
        roundStartTime.set((int) (System.currentTimeMillis() / 1000));
        currentWinner = "Chua co";

        System.out.println("[SERVER] New round - Starting price: " + startingPrice.get());
        broadcastMessage("[SERVER] Vong moi bat dau! Gia khoi diem: " + startingPrice.get() + "d");
    }

    public static int getStartingPrice() {
        return startingPrice.get();
    }

    // Broadcast message cho tat ca clients
    public static void broadcastMessage(String message) {
        for (DataOutputStream dos : clients) {
            try {
                dos.writeUTF(message);
                dos.flush();
            } catch (Exception e) {
                // Client da ngat ket noi, xoa khoi danh sach
                clients.remove(dos);
            }
        }
    }

    // Them client vao danh sach
    public static void addClient(DataOutputStream dos) {
        clients.add(dos);
        System.out.println("[SERVER] Client added. Total clients: " + clients.size());
    }

    // Xoa client khoi danh sach
    public static void removeClient(DataOutputStream dos) {
        clients.remove(dos);
        System.out.println("[SERVER] Client removed. Total clients: " + clients.size());
    }

    // Cap nhat nguoi thang
    public static void setCurrentWinner(String winner) {
        currentWinner = winner;
    }

    // Cap nhat gia khoi diem
    public static void updateStartingPrice(int price) {
        startingPrice.set(price);
    }

    // Them vao lich su
    public static void addHistory(String entry) {
        history.add(entry);
    }
}

class serverHandle extends Thread {
    private Socket soc;

    public serverHandle(Socket soc) {
        this.soc = soc;
    }

    @Override
    public void run() {
        int currentPrice = server.getStartingPrice();
        DataOutputStream dos = null;
        try {
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            dos = new DataOutputStream(soc.getOutputStream());

            // Dang ky client vao danh sach
            server.addClient(dos);

            // Gui thong bao chao mung
            dos.writeUTF("[SERVER] Chao mung den phien dau gia! Gia hien tai: " + currentPrice + "d");
            dos.flush();

            while (true) {
                String receivedData = dis.readUTF();
                String response = null;

                System.out.println("Server received: " + receivedData);

                // Xu ly khong hop le
                receivedData = receivedData.trim();
                int price = getPrice(receivedData);
                if (price == -1 || price <= currentPrice) {
                    response = "Gia khong hop le! Gia hien tai: " + currentPrice + "d";
                    dos.writeUTF(response);
                    dos.flush();
                    System.out.println("Server sent: " + response);

                    continue;
                }

                // Xu ly hop le
                currentPrice = price;
                server.updateStartingPrice(price);

                // Luu vao lich su
                String clientInfo = soc.getInetAddress().getHostAddress() + ":" + soc.getPort();
                String historyEntry = clientInfo + " - Gia: " + price + "d - " +
                        java.time.LocalDateTime.now().format(
                                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
                server.addHistory(historyEntry);
                server.setCurrentWinner(clientInfo);

                // Broadcast cho tat ca clients
                String broadcastMsg = "[DAU GIA MOI] " + clientInfo + " da dau gia " + price + "d!";
                server.broadcastMessage(broadcastMsg);

                // Gui phan hoi cho client dau gia
                response = "Dau gia thanh cong! Gia hien tai: " + price + "d";
                dos.writeUTF(response);
                dos.flush();
                System.out.println("Server sent: " + response);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + soc.getInetAddress().getHostAddress());
        } finally {
            // Huy dang ky client
            if (dos != null) {
                server.removeClient(dos);
            }
            try {
                soc.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private int getPrice(String price) {
        try {
            int a = Integer.parseInt(price);
            return a;
        } catch (Exception e) {
            return -1;
        }
    }

}