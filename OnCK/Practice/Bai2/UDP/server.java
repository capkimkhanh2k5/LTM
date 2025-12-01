package OnCK.Practice.Bai2.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    public static void main(String[] args) {
        new server();
    }

    public server() {
        try {
            System.out.println("Server is running on port 6000...");
            DatagramSocket socket = new DatagramSocket(6000);

            ExecutorService executorService = Executors.newCachedThreadPool();

            while (true) {
                //Nhận info
                byte[] buffer = new byte[2048];
                DatagramPacket packet = new  DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                serverHandle handle = new serverHandle(socket, packet);
                executorService.submit(handle);
            }

        } catch (Exception e) {
            System.out.println("Error in server!");
            e.printStackTrace();
        }
    }
}

class serverHandle extends Thread {
    private DatagramSocket socket;
    private DatagramPacket packet;

    public serverHandle(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        try {
            String msg = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Server received message: " + msg);

            String rs = caculatorStudent(msg);
            
            packet = new DatagramPacket(rs.getBytes(), rs.getBytes().length, packet.getAddress(), packet.getPort());
            socket.send(packet);

        } catch (Exception e) {
            System.out.println("Error in serverHandle!");
            e.printStackTrace();
        }
    }

    private String caculatorStudent(String msg) {
        //Xử lý chuỗi đầu vào
        String arr[] = msg.split("\\|");

        String id = arr[0];
        if(id.length() < 4 || id.length() > 10){
            return "ID is Invalid!";
        }

        String name = arr[1];
        if(name.isEmpty()){
            return "Name is Invalid!";
        }

        double[] cores = new double[arr.length - 2];
        try{
            if(cores.length > 10) throw new Exception();
            
            for (int i = 2; i < arr.length; i++) {
                double value = Double.parseDouble(arr[i]);

                if (value < 0.0 || value > 10.0) { 
                    throw new Exception();
                }
                
                cores[i - 2] = value;
            }
        }catch(Exception e){
            return "Cores is Invalid!";
        }

        String average = String.valueOf(averageCores(cores));
        String max = String.valueOf(maxCores(cores));
        String min = String.valueOf(minCores(cores));
        String grade = Grade(averageCores(cores));
        String totalCores = String.valueOf(cores.length);

        return id + "|" + name + "|" + average + "|" + max + "|" + min + "|" + grade + "|" + totalCores;
    }

    private double averageCores(double[] cores) {
        double sum = 0;
        for (double c : cores) {
            if(c <= 0 || c >= 10.0){
                
            }
            sum += c;
        }
        return sum / cores.length;
    }

    private double maxCores(double[] cores){
        double max = cores[0];
        for (double c : cores) {
            if (c > max)
                max = c;
        }
        return max;
    } 

    private double minCores(double[] cores){
        double min = cores[0];
        for (double c : cores) {
            if (c < min)
                min = c;
        }
        return min;
    }

    private String Grade(double average) {
        if (average >= 9.0)
            return "Excellent";
        if (average >= 8.0)
            return "Very Good";
        if (average >= 7.0)
            return "Good";
        if (average >= 5.0)
            return "Medium";
        return "Weak";
    }
}
