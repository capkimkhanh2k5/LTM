package OnCK.Practice.Bai2.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import OnCK.Practice.Bai2.TCP.student;

public class server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6001);
            ExecutorService executorService = Executors.newCachedThreadPool();
            System.out.println("Server is running on port 6001...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress() + ":" + socket.getPort());

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


            dis.close();
            dos.close();
            socket.close();

        } catch (Exception e) {
            System.out.println("Error in serverHandle!");
            e.printStackTrace();
        }
    }

    

    private String determineGrade(double average) {
        if (average >= 9.0)
            return "Excellent";
        if (average >= 8.0)
            return "Very Good";
        if (average >= 7.0)
            return "Good";
        if (average >= 5.0)
            return "Medium";
        return "Weak";
    }
}
