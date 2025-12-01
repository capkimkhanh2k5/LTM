package OnCK.Practice.Bai4.TCP;

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

            while (true) {
                Scanner sc = new Scanner(System.in);
                System.out.println("Header: CALC, FACTOR, PRIME, HISTORY, EXIT");

                // Gửi dữ liệu đến server
                String input = sc.nextLine();
                dos.writeUTF(input);
                dos.flush();
                System.out.println("Client sent: " + input);

                // Nhận dữ liệu từ server
                String response = dis.readUTF();
                System.out.println("Client received: " + response);

                if (response.startsWith("EXIT")) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error connect Server! " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                soc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
