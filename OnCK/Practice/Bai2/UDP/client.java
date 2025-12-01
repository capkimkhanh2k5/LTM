package OnCK.Practice.Bai2.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class client {
    private DatagramSocket socket;
    private int port = 6000;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        try {
            socket = new DatagramSocket();

            student std = new student("100230192", "Cap Kim Khanh", new double[]{4,5,6,7,8,9,10,10,10,10,10});
            String msg = std.toString();

            byte[] buffer = new byte[1024];
            buffer = msg.getBytes();

            //Gửi info
            InetAddress address = InetAddress.getLocalHost();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet);

            //Nhận result
            buffer = new byte[1024];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String tmp = new String(packet.getData(), 0, packet.getLength());
            
            String[] rs = tmp.split("\\|");
            
            if(rs.length == 1){
                System.out.println(rs[0]);
                socket.close();
                return;
            }

            System.out.println("ID: " + rs[0]);
            System.out.println("Name: " + rs[1]);
            System.out.println("Average: " + rs[2]);
            System.out.println("Max: " + rs[3]);
            System.out.println("Min: " + rs[4]);
            System.out.println("Grade: " + rs[5]);
            System.out.println("Total cores: " + rs[6]);

            socket.close();
        } catch (Exception e) {
            System.out.println("Error in client!");
            e.printStackTrace();
        }
    }

}

class student {
    private String id;
    private String name;
    private double[] cores;

    public student(String id, String name, double[] cores) {
        this.id = id;
        this.name = name;
        this.cores = cores;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double[] getCores() {
        return cores;   
    }

    public String toString() {
        String s = this.id + "|" + this.name;
        for (double c : this.cores) {
            s += "|" + c;
        }
        return s;
    }
}