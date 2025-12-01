package OnCK.Practice.Bai1.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class client implements Runnable{
    private static final long serialVersionUID = 1L;

    private Socket soc;

    public static void main(String[] args) {
        new client();
    }

    public client(){    

        try {
			soc = new Socket("localhost", 5001);
            System.out.println("Client connected: " + soc.getInetAddress().getHostAddress() + ":" + soc.getPort());
		} catch (Exception e) {
			System.out.println("Error in connection!");
		}

        Thread t = new Thread(this);
        t.start();
    }


    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

            String dates = "1/1/2025|10/10/2025";
            
            dos.writeUTF(dates);
            dos.flush();

            int rs = dis.readInt();
            
            if(rs == -1){   
                System.out.println("Format Dates is Invalid!");
            } else {
                System.out.println("Numbers of Dates is: " + rs);
            }

            dis.close();
            dos.close();
            soc.close();
        } catch (Exception e) {
            System.out.println("Error in connection!");
        }
    }

}
