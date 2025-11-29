package OnCK.CK2023.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(5001);
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println("Server is running...");

        while (true) {
            try {
                Socket soc = socket.accept();
                serverHandle handle = new serverHandle(soc);
                executorService.execute(handle);

                System.out.println("Client connected: " + soc.getInetAddress().getHostAddress() + ":" + soc.getPort());
            } catch (Exception e) {
                System.out.println("Error in connection!");
            }
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
        int count = 0;

        try {
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

            int n = dis.readInt();

            System.out.println("Server receive: " + n);

            int m = 2;

            while (count < 10) {
                count++;

                int check = dis.readInt();

                if (check == 1) {
                    System.out.println("Server COMPLETED!");

                    dos.close();
                    dis.close();

                    soc.close();

                    break;
                } else {
                    dos.writeInt(m++);
                }

                dos.flush();
            }

            dos.close();
            dis.close();

            soc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in connection!");
        }
    }
}