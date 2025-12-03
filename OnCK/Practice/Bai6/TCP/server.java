package OnCK.Practice.Bai6.TCP;

import java.util.List;
import java.util.Vector;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    private static List<String> chatHistory = new ArrayList<>();
    public static Vector<serverHandle> clientHandler = new Vector<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5001);
            ExecutorService executorService = Executors.newCachedThreadPool();
            System.out.println("Server is running on port 5001...");

            while (true) {
                Socket soc = serverSocket.accept();
                System.out.println("Client connected: " + soc.getInetAddress().getHostAddress() + ":" + soc.getPort());

                serverHandle handle = new serverHandle(soc);

                clientHandler.add(handle);

                executorService.execute(handle);
            }
        } catch (Exception e) {
            System.out.println("Error in server!");
            e.printStackTrace();
        }
    }

    public static List<String> getList() {
        return chatHistory;
    }

    public static void addList(String str) {
        chatHistory.add(str);
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

            // Gửi toàn bộ lịch sử chat cũ
            List<String> history = server.getList();

            dos.writeInt(history.size());
            if (history.size() != 0) {
                for (String str : history) {
                    dos.writeUTF(str);
                }
            }

            while (true) {
                String msg = "[" + soc.getInetAddress().getHostAddress() + "]:" + dis.readUTF();
                System.out.println("Server received: " + msg);
                server.addList(msg);

                sendAllClient(msg);
            }

        } catch (EOFException | SocketException e) {
            System.out.println("Client disconnected.");
        } catch (Exception e) {
            System.out.println("Error in serverHandle!");
            e.printStackTrace();
        } finally {
            try {
                server.clientHandler.remove(this);
                soc.close();
            } catch (IOException e) {
                System.out.println("Error in closing socket!");
                e.printStackTrace();
            }
        }
    }

    private void sendAllClient(String msg) {
        for (serverHandle handle : server.clientHandler) {
            try {
                DataOutputStream dos = new DataOutputStream(handle.soc.getOutputStream());

                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e) {
                System.out.println("Error in sending message to client!");
            }
        }
    }
}