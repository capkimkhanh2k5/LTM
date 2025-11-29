package OnCK.ChatBoxTCP;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class client extends JFrame implements ActionListener, Runnable{
    private static long serialVersionUID = 1;

    private Socket soc;

    private JTextArea txt_area;
    private JTextField txt_name;
    private JTextField txt_msg;
    private JButton btn_send;

    public static void main(String[] args) {
        for(int i = 1; i <= 3; i ++)
            new client("Client " + serialVersionUID ++);
    }

    public client(String txt){
        setTitle("Client");
        setSize(400,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        txt_area = new JTextArea();
        txt_area.setEditable(false);
        txt_name = new JTextField();
        txt_name.setText(txt);
        txt_name.setEditable(false);
        txt_msg = new JTextField();
        btn_send = new JButton("Send");
        btn_send.addActionListener(this);

        setLayout(new BorderLayout());

        add(txt_area, BorderLayout.CENTER);
        JPanel pn = new JPanel(new GridLayout(1,3));
        pn.add(txt_name);
        pn.add(txt_msg);
        pn.add(btn_send);
        add(pn, BorderLayout.SOUTH);

        try {
            soc = new Socket("localhost", 5432);

            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
            
            dos.writeUTF(txt_name.getText());
            dos.flush();
        } catch (Exception e) {
            System.out.println("Client Error in generate Socket!");
            e.printStackTrace();
        }

        Thread t = new Thread(this);
        t.start();

        setVisible(true);
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            
            while (true) {
                String msg = dis.readUTF();
                txt_area.append(msg + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

            String msg = txt_name.getText() + ": " + txt_msg.getText();

            dos.writeUTF(msg);
            dos.flush();

            txt_msg.setText("");
        }catch(Exception ex){
            System.out.println("Client Error in send Message to Server!");
            ex.printStackTrace();
        }
    }
}
