package OnCK.Practice.Bai6.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.GridLayout;

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
    private JLabel lb_client;
    private JComboBox<String> cbb_client;
    private JButton btn_client;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        setTitle("Client ChatBox");
        setSize(400,600);
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
            lb_client = new JLabel("Channel");
            cbb_client = new JComboBox<String>();
            addCommboBox("All");
            addCommboBox(soc.getInetAddress().getHostAddress());
            btn_client = new JButton("View");

            JPanel pn = new JPanel(new GridLayout(2, 3, 5, 5));
            pn.add(lb_text);
            pn.add(txt_text);
            pn.add(btn_text);
            pn.add(lb_client);
            pn.add(cbb_client);
            pn.add(btn_client);

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

            // TODO: Gửi dữ liệu đến server
            String message = "Hello Server";
            dos.writeUTF(message);
            dos.flush();
            System.out.println("Client sent: " + message);

            // TODO: Nhận dữ liệu từ server
            String response = dis.readUTF();
            System.out.println("Client received: " + response);

            // Đóng kết nối
            dis.close();
            dos.close();
            soc.close();
        } catch (Exception e) {
            System.out.println("Error in communication!");
            e.printStackTrace();
        }
    }

    private void addCommboBox(String id){
        cbb_client.addItem(id);
    }
}
