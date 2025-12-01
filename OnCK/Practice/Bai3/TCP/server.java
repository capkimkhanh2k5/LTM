package OnCK.Practice.Bai3.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(7001);
            ExecutorService executorService = Executors.newCachedThreadPool();
            System.out.println("Server is running on port 7001...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println(
                        "Client connected: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

                serverHandle handle = new serverHandle(socket);
                executorService.execute(handle);
            }
        } catch (Exception e) {
            System.out.println("Error in server!");
            e.printStackTrace();
        }
    }
}

class serverHandle extends Thread {
    private Socket socket;

    public serverHandle(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // TODO: Nhận số p từ client
            String receivedData = dis.readUTF();
            System.out.println("Server received: " + receivedData);

            // TODO: Parse p, kiểm tra số nguyên tố
            // TODO: Tìm primitive roots
            // TODO: Build response string

            String response = "Response from server";
            dos.writeUTF(response);
            dos.flush();
            System.out.println("Server sent: " + response);

            // Đóng kết nối
            dis.close();
            dos.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error in serverHandle!");
            e.printStackTrace();
        }
    }
}
