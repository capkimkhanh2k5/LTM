package OnCK.Practice.Bai4.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class client {
    private DatagramSocket socket;
    private int port = 5000;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        try {
            while (true) {
                socket = new DatagramSocket();

                Scanner sc = new Scanner(System.in);
                System.out.println("Header: CALC, FACTOR, PRIME, HISTORY, EXIT");

                //Gửi dữ liệu tới server
                String input = sc.nextLine();
                byte[] sendBuffer = input.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getLocalHost(), port);
                socket.send(sendPacket);
                System.out.println("Client sent: " + input);

                //Nhận dữ liệu từ server
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                if(input.equalsIgnoreCase("EXIT")){
                    System.out.println("Client exit!");
                    break;
                }

                String response = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                System.out.println("Client received: " + response);
            }
        } catch (Exception e) {
            System.out.println("Error in client!");
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                System.out.println("Error in client!");
                e.printStackTrace();
            }
        }
    }
}
