package OnCK.PhanTichUDP;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class client extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private JLabel lb_input;
    private JLabel lb_upcase;
    private JLabel lb_downcase;
    private JLabel lb_reverse;
    private JLabel lb_number;

    private JTextField txt_input;
    private JTextField txt_upcase;
    private JTextField txt_downcase;
    private JTextField txt_space;
    private JTextField txt_number;

    private JButton btn_reset;
    private JButton btn_send;

    private DatagramSocket socket;

    public static void main(String[] args) {
        new client();
    }

    public client(){
        setTitle("Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,600);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(6,2, 5, 5));

        lb_input = new JLabel("Input");
        lb_upcase = new JLabel("Upcase");
        lb_downcase = new JLabel("Downcase");
        lb_reverse = new JLabel("Reverse");
        lb_number = new JLabel("Number");   

        txt_input = new JTextField();
        txt_upcase = new JTextField();
        txt_downcase = new JTextField();
        txt_space = new JTextField();
        txt_number = new JTextField();

        txt_upcase.setEditable(false);
        txt_downcase.setEditable(false);
        txt_space.setEditable(false);
        txt_number.setEditable(false);

        btn_reset = new JButton("Reset");
        btn_send = new JButton("Send");

        btn_reset.addActionListener(this);
        btn_send.addActionListener(this);

        add(lb_input);
        add(txt_input);
        add(lb_upcase);
        add(txt_upcase);
        add(lb_downcase);
        add(txt_downcase);
        add(lb_reverse);
        add(txt_space);
        add(lb_number);
        add(txt_number);
        add(btn_reset);
        add(btn_send);

        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            System.out.println("Error in create DatagramSocket!");
            e.printStackTrace();
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btn_reset){
            txt_input.setText("");
            txt_upcase.setText("");
            txt_downcase.setText("");
            txt_space.setText("");
            txt_number.setText("");
            txt_input.requestFocus();
            txt_input.selectAll();
        }
        else if(e.getSource() == btn_send){
            String input = txt_input.getText();
            if(input.length() > 0)
            {
                try {
                    DatagramPacket tt = new DatagramPacket(input.getBytes(), input.length(), InetAddress.getLocalHost(), 5000);
                    socket.send(tt);

                    byte[] buffer_receive = new byte[1024];
                    DatagramPacket packet_receive = new DatagramPacket(buffer_receive, buffer_receive.length);
                    socket.receive(packet_receive);

                    String rs = new String(packet_receive.getData(), 0, packet_receive.getLength()).trim();
                    String[] arr = rs.split(",");

                    if(arr.length != 4){
                        System.out.println("Packet loss!");
                        throw new IOException();
                    }

                    txt_upcase.setText(arr[0]);
                    txt_downcase.setText(arr[1]);
                    txt_space.setText(arr[2]);
                    txt_number.setText(arr[3]);

                } catch (IOException e1) {
                    System.out.println("Error in send DatagramPacket!");
                    e1.printStackTrace();
                } 
            }
            else{
                System.out.println("Input is empty!");

                txt_downcase.setText("Error!");
                txt_space.setText("Error!");
                txt_number.setText("Error!");
                txt_upcase.setText("Error!");
                
                txt_input.requestFocus();
                txt_input.selectAll();
            }
        }  
    }
}
