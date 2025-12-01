package OnCK.Practice.Bai2.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import OnCK.Practice.Bai2.TCP.student;

public class client implements Runnable {
    private Socket socket;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        try {
            socket = new Socket("localhost", 6001);
            System.out.println("Client connected to server: " + socket.getInetAddress() + ":" + socket.getPort());

            Thread t = new Thread(this);
            t.start();
        } catch (Exception e) {
            System.out.println("Error connecting to server!");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            dis.close();
            dos.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error in client communication!");
            e.printStackTrace();
        }
    }
}
