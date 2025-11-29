package OnCK.PhanTichUDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class server {
    public static void main(String[] args) {
        new server();
    }

    public server(){
        try {
            System.out.println("Server is running in port 5000...");
            DatagramSocket socket = new DatagramSocket(5000);

            while(true){
                byte[] buffer = new byte[1024];

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                serverHandle handle = new serverHandle(socket, packet);
                handle.start();
            }

        } catch (Exception e) { 
            System.out.println("Error in server!");
            e.printStackTrace();
        }
    }
}

class serverHandle extends Thread{
    private DatagramSocket socket;
    private DatagramPacket packet;


    public serverHandle(DatagramSocket socket, DatagramPacket packet){
        this.socket = socket;
        this.packet = packet;
    }


    @Override
    public void run() {
        String input = new String(packet.getData(), 0, packet.getLength()).trim();

        System.out.println("Server received: " + input);

        int upCase = 0;
        int downCase = 0;
        int number = 0;
        int space = 0;


        for(char c : input.toCharArray()){
            if(Character.isUpperCase(c))
                upCase++;
            else if(Character.isLowerCase(c))
                downCase++;
            else if(Character.isDigit(c))
                number++;
            else 
                space ++;
        }

        String rs = upCase + "," + downCase + "," + space + "," + number;
        byte[] buffer = rs.getBytes();

        packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
        try {
            socket.send(packet);
        } catch (Exception e) {
            System.out.println("Error in send DatagramPacket!");
            e.printStackTrace();
        }
    }
}