package OnCK.Practice.Bai6.TCP;

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
}

class serverHandle extends Thread {
    private Socket soc;

    public serverHandle(Socket soc) {
        this.soc = soc;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

            // TODO: Nhận dữ liệu từ client
            String receivedData = dis.readUTF();
            System.out.println("Server received: " + receivedData);

            // TODO: Xử lý dữ liệu và gửi response
            String response = "Response from server";
            dos.writeUTF(response);
            dos.flush();
            System.out.println("Server sent: " + response);

            // Đóng kết nối
            dis.close();
            dos.close();
            soc.close();
        } catch (IOException e) {
            System.out.println("Error in serverHandle!");
            e.printStackTrace();
        }
    }
}