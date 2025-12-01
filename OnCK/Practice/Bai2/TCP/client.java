package OnCK.Practice.Bai2.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class client implements Runnable {
    private Socket socket;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        try {
            socket = new Socket("localhost", 6001);
            System.out.println("Client connected to server: " + socket.getInetAddress() + ":" + socket.getPort());

            Thread t = new Thread(this);
            t.start();
        } catch (Exception e) {
            System.out.println("Error connecting to server!");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            student std = new student("102230192", "Cap Kim Khanh", new double[]{4,5,6,7,8,9,10,10,10,10});

            String msg = std.toString();
            dos.writeUTF(msg);
            dos.flush();

            String tmp = dis.readUTF();
            System.out.println("Server response: " + tmp);

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

            //Đóng kết nối
            dis.close();
            dos.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error in client communication!");
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