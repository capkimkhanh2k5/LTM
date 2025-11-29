package OnCK.UpperCaseTCP;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class client extends JFrame implements ActionListener, Runnable{
    private static final long serialVersionUID = 1L;

    private Socket soc;

    private JButton btn;
    private JLabel lb;
    private JTextField txt;
    private JTextField txt_rs;

    public static void main(String[] args) {
        new client();
    }

    public client(){
        this.setTitle("Client");
        this.setSize(500,500);
        this.setDefaultCloseOperation(3);

        this.setLayout(new BorderLayout());

    lb = new JLabel("Input: ");
    txt = new JTextField();
    btn = new JButton("Send");
    txt_rs = new JTextField();
    txt_rs.setEditable(false);

    btn.addActionListener(this);

    JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));

    formPanel.add(lb);
    formPanel.add(txt);
    formPanel.add(new JLabel("Result:"));
    formPanel.add(txt_rs);


    this.add(formPanel, BorderLayout.CENTER);
    this.add(btn, BorderLayout.SOUTH);

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
                String txt = dis.readUTF();

                System.out.println("Client receive: " + txt);
                
                this.txt_rs.setText(txt);
            } catch (Exception e) {
                System.out.println("Error in connection!");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btn){
            System.out.println(txt.getText());
            try {
                DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
                dos.writeUTF(txt.getText());
                dos.flush();
            } catch (IOException e1) {
                System.out.println("Error DOS!");
                e1.printStackTrace();
            }
        }
    }
    
}
