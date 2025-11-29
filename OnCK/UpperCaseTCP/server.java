package OnCK.UpperCaseTCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(5001);
        System.out.println("Server is running...");

        while(true){
            try {
                Socket soc = socket.accept();
                serverHandle handle = new serverHandle(soc);
                handle.start();

                System.out.println("Client connected: " + soc.getInetAddress().getHostAddress() + ":" + soc.getPort());
            } catch (Exception e) {
                System.out.println("Error in connection!");
            }
        }
    }
}

class serverHandle extends Thread{
    private Socket soc;

    public serverHandle(Socket soc){
        this.soc = soc;
    }

    @Override
    public void run(){
        while(true){
            try {
                DataInputStream dis = new DataInputStream(soc.getInputStream());
                String txt = dis.readUTF();

                System.out.println("Server receive: " + txt);

                txt = txt.toUpperCase();

                DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
                dos.writeUTF(txt);
                dos.flush();
            } catch (IOException e) {
                System.out.println("Error DIS or DOS in ServerHandle!");
                e.printStackTrace();
            }
        }
    }
}