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
		} catch (Exception e) {
			System.out.println("Error in connection!");
		}

        Thread t = new Thread(this);
        t.start();
    }


    @Override
    public void run() {
        while(true){
            try {
                DataInputStream dis = new DataInputStream(soc.getInputStream());
                DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

                System.out.println("Client receive: ");
            } catch (Exception e) {
                System.out.println("Error in connection!");
            }
        }
    }

}
