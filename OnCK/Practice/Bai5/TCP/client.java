package OnCK.Practice.Bai5.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class client implements Runnable {
    private Socket soc;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        try {
            soc = new Socket("localhost", 5001);
            System.out.println("Client connected: " + soc.getInetAddress() + ":" + soc.getPort());
        } catch (Exception e) {
            System.out.println("Error in connection!");
            e.printStackTrace();
            return;
        }

        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

            // Tao luong moi: Thread lang nghe broadcast tu server
            Thread listenThread = new Thread(() -> {
                try {
                    while (true) {
                        String serverMsg = dis.readUTF();
                        System.out.println("\n" + serverMsg);
                        System.out.print("Nhap gia dau gia (hoac 'exit' de thoat): ");
                    }
                } catch (Exception e) {
                    System.out.println("\n[INFO] Ngat ket noi voi server");
                }
            });
            listenThread.setDaemon(true);
            listenThread.start();

            // Thread chinh de nhap gia dau gia
            Scanner scanner = new Scanner(System.in);
            System.out.println("=== PHIEN DAU GIA ===");
            System.out.println("Nhap gia dau gia cua ban (so nguyen)");

            while (true) {
                System.out.print("Nhap gia dau gia (hoac 'exit' de thoat): ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Thoat khoi phien dau gia...");
                    break;
                }

                // Gửi giá đấu giá lên server
                dos.writeUTF(input);
                dos.flush();

                // Cho mot chut de nhan response
                Thread.sleep(100);
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error in communication!");
            e.printStackTrace();
        } finally {
            try {
                soc.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
