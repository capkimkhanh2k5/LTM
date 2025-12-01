package OnCK.Practice.Bai3.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class client implements Runnable {
    private Socket socket;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        try {
            socket = new Socket("localhost", 7001);
            System.out.println("Client connected to server: " + socket.getInetAddress() + ":" + socket.getPort());
        } catch (Exception e) {
            System.out.println("Error connecting to server!");
            e.printStackTrace();
            return;
        }

        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // TODO: Gửi số nguyên tố p đến server
            String message = "23";
            dos.writeUTF(message);
            dos.flush();
            System.out.println("Client sent: " + message);

            // TODO: Nhận kết quả từ server
            String response = dis.readUTF();
            System.out.println("Client received: " + response);

            // TODO: Hiển thị kết quả (parse response)

            // Đóng kết nối
            dis.close();
            dos.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error in communication!");
            e.printStackTrace();
        }
    }
}
