package OnCK.CK2021.TCP;

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
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class client extends JFrame implements ActionListener, Runnable {
    private static final long serialVersionUID = 1L;

    private Socket soc;

    private JLabel lb_date;
    private JLabel lb_rs;
    private JLabel lb_exam;

    private JTextField txt_date;
    private JTextField txt_rs;
    private JButton btn;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        this.setTitle("Client");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(3);

        this.setLayout(new BorderLayout());

        lb_date = new JLabel("Date: ");
        lb_rs = new JLabel("Result: ");
        lb_exam = new JLabel("Example: " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now()));

        txt_date = new JTextField();
        txt_rs = new JTextField();
        txt_rs.setEditable(false);
        btn = new JButton("Send");
        btn.addActionListener(this);

        JPanel pn = new JPanel(new GridLayout(2, 2));

        pn.add(lb_date);
        pn.add(txt_date);
        pn.add(lb_rs);
        pn.add(txt_rs);

        this.add(lb_exam, BorderLayout.NORTH);
        this.add(pn, BorderLayout.CENTER);
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
        while (true) {
            try {
                DataInputStream dis = new DataInputStream(soc.getInputStream());

                String rs = dis.readUTF();
                txt_rs.setText(rs);
            } catch (Exception e) {
                System.out.println("Error in connection!");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

            String date = txt_date.getText();

            dos.writeUTF(date);
            dos.flush();

            txt_rs.setText("");
        } catch (Exception e2) {
            System.out.println("Error in connection!");
        }
    }
}
