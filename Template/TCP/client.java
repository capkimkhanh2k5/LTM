package Template.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

            // TODO: Gửi dữ liệu đến server
            String message = "Hello Server";
            dos.writeUTF(message);
            dos.flush();
            System.out.println("Client sent: " + message);

            // TODO: Nhận dữ liệu từ server
            String response = dis.readUTF();
            System.out.println("Client received: " + response);

            // Đóng kết nối
            dis.close();
            dos.close();
        } catch (Exception e) {
            System.out.println("Error in communication!");
            e.printStackTrace();
        } finally {
            try {
                soc.close();
            } catch (IOException ex) {
                System.out.println("Error in closing socket!");
                ex.printStackTrace();
            }
        }
    }
}
