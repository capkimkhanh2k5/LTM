package OnCK.Practice.Bai5.TCP;

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

            while(true){
                String message = "Client " + soc.getInetAddress() + " tham gia dau gia!";
                dos.writeUTF(message);
                dos.flush();
                System.out.println("Client sent: " + message);

                String response = dis.readUTF();
                System.out.println("Client received: " + response);
            }
        } catch (Exception e) {
            System.out.println("Error in communication!");
            e.printStackTrace();
        } finally{
            try{
                soc.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
