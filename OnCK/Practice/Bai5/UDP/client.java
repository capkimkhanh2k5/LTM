package OnCK.Practice.Bai5.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class client {
    private static DatagramSocket socket;
    private static InetAddress serverIP;
    private static int serverPort = 5001;

    public static void main(String[] args) {
        try {
            socket = new DatagramSocket();
            serverIP = InetAddress.getByName("localhost");

            // Gui message JOIN de server biet client nay ton tai
            sendMessage("JOIN");

            // Thread lang nghe
            Thread listenThread = new Thread(() -> {
                try {
                    byte[] receiveData = new byte[1024];
                    while (true) {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        socket.receive(receivePacket);
                        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println("\n" + message);
                        System.out.print("Nhap gia dau gia: ");
                    }
                } catch (Exception e) {
                    System.out.println("\n[INFO] Ngat ket noi hoac loi nhan tin");
                }
            });
            listenThread.setDaemon(true);
            listenThread.start();

            // Thread chinh de nhap lieu
            Scanner scanner = new Scanner(System.in);
            System.out.println("=== PHIEN DAU GIA (UDP) ===");
            System.out.print("Nhap gia dau gia: ");

            while (true) {
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    sendMessage(input);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private static void sendMessage(String message) {
        try {
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
