package OnCK.ChatBoxTCP;

import java.io.*;
import java.net.*;
import java.util.*;
public class server {
    private ServerSocket serverSocket;
    private static Vector<ServerHandler> clientHandler = new Vector<>();


    public static void main(String[] args) { 
        new server(); 
    }

    public server() {
        try {
            serverSocket = new ServerSocket(5432);
            System.out.println("Server running on 5432");
            while (true) {
                Socket soc = serverSocket.accept();

                DataInputStream dis = new DataInputStream(soc.getInputStream());

                String txt_client = dis.readUTF();

                ServerHandler handel = new ServerHandler(soc);
                handel.start();

                clientHandler.add(handel);

                System.out.println("New client: " + txt_client);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ServerHandler extends Thread {
        private Socket socket;
        private DataInputStream dis;
        private DataOutputStream dos;

        public ServerHandler( Socket socket){
            this.socket = socket;
        }

        private void sendAllClient(String msg){
            try {
                for(ServerHandler handel : clientHandler){
                    dos = new DataOutputStream(handel.socket.getOutputStream());
                    dos.writeUTF(msg);
                    dos.flush();
                }
            } catch (Exception e) {
                System.out.println("Client is disconnected!");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());

                while(true){
                    String msg = dis.readUTF();
                    System.out.println(msg);
                    sendAllClient(msg);
                }
            } catch (Exception e) {
                System.out.println("Client is disconnected!");
            }
        }
    }
}
