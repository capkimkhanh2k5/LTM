package OnCK.Practice.Bai6.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class client extends JFrame implements Runnable {
    private Socket soc;

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
            soc = new Socket("localhost", 5001);
            System.out.println("Client connected: " + soc.getInetAddress() + ":" + soc.getPort());

            lb = new JLabel("Chat Box LTM");
            txt_area = new JTextArea();
            txt_area.setEditable(false);
            lb_text = new JLabel("ID: " + soc.getInetAddress());
            txt_text = new JTextField();
            btn_text = new JButton("Send");
            btn_text.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

                        String msg = txt_text.getText();
                        dos.writeUTF(msg);
                        dos.flush();

                        txt_text.setText("");
                    } catch (IOException e1) {
                        System.out.println("Error in sending message!");
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

        } catch (Exception e) {
            System.out.println("Error in connection!");
            e.printStackTrace();
            return;
        }

        Thread t = new Thread(this);
        t.start();

        setVisible(true);
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

            List<String> chatHistory = new ArrayList<>();

            int capacity = dis.readInt();
            if (capacity != 0) {
                for (int i = 0; i < capacity; i++) {
                    String historyMsg = dis.readUTF();
                    chatHistory.add(historyMsg);
                    txt_area.append(historyMsg + "\n");
                }
            }

            while (true) {
                String msg = dis.readUTF();
                System.out.println("Client received: " + msg);
                txt_area.append(msg + "\n");
            }

        } catch (Exception e) {
            System.out.println("Error in communication!");
            e.printStackTrace();
        } finally {
            try {
                soc.close();
            } catch (IOException ex) {
                System.out.println("Error in closing socket!");
                ex.printStackTrace();
            }
        }

    }
}
