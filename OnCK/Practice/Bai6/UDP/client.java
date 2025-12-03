package OnCK.Practice.Bai6.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class client extends JFrame {
    private DatagramSocket socket;
    private int port = 5000;

    private JLabel lb;
    private JTextArea txt_area;
    private JLabel lb_text;
    private JTextField txt_text;
    private JButton btn_text;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        setTitle("Client ChatBox");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");

            lb = new JLabel("Chat Box LTM");
            txt_area = new JTextArea();
            txt_area.setEditable(false);
            lb_text = new JLabel("ID: " + InetAddress.getLocalHost().getHostAddress());
            txt_text = new JTextField();
            btn_text = new JButton("Send");
            btn_text.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String msg = txt_text.getText();
                    if (msg.trim().isEmpty()) {
                        return;
                    }
                    byte[] buffer = msg.getBytes();

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, port);
                    try {
                        socket.send(packet);
                        txt_text.setText("");
                    } catch (Exception e1) {
                        System.out.println("Error send packet!");
                        e1.printStackTrace();   
                    }
                }
            });

            JPanel pn = new JPanel(new GridLayout(1, 3, 5, 5));
            pn.add(lb_text);
            pn.add(txt_text);
            pn.add(btn_text);

            add(lb, BorderLayout.NORTH);
            add(txt_area, BorderLayout.CENTER);
            add(pn, BorderLayout.SOUTH);

            setVisible(true);

            List<String> history = new ArrayList<>();
            if(history.size() != 0){
                for (String str : history) {
                    txt_area.append(str + "\n");
                }
            }

            while(true){
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String receivedData = new String(packet.getData(), 0, packet.getLength()).trim();
                txt_area.append(receivedData + "\n");
            }
        } catch (Exception e) {
            System.out.println("Error in client!");
            e.printStackTrace();
        } finally{
            try{
                socket.close();
            } catch (Exception ex) {
                System.out.println("Error close socket!");
                ex.printStackTrace();
            }
        }
    }
}
