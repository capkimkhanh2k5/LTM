package OnCK.Practice.Bai3.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

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

            // Gửi dữ liệu đến server
            int n = 98;
            dos.writeInt(n);
            dos.flush();
            System.out.println("Client sent: " + n);

            // Nhận dữ liệu từ server
            String response = dis.readUTF();
            System.out.println("Client received: " + response);

            // Đóng kết nối
            dis.close();
            dos.close();
            soc.close();
        } catch (Exception e) {
            System.out.println("Error in communication!");
            e.printStackTrace();
        }
    }
}
