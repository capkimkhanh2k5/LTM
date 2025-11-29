package Template.TCP;

import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class client extends JFrame implements ActionListener, Runnable{
    private static final long serialVersionUID = 1L;

    private Socket soc;

    public static void main(String[] args) {
        new client();
    }

    public client(){
        this.setTitle("Client");
        this.setSize(500,500);
        this.setDefaultCloseOperation(3);

        this.setLayout(new BorderLayout());

        try {
			soc = new Socket("localhost", 5001);
		} catch (Exception e) {
			System.out.println("Error in connection!");
		}

        Thread t = new Thread(this);
        t.start();

        this.setVisible(true);
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

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
