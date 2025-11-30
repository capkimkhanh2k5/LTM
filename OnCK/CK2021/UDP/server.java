package OnCK.CK2021.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    public static void main(String[] args) {
        new server();
    }

    public server() {
        try {
            System.out.println("Server is running in port 5000...");
            DatagramSocket socket = new DatagramSocket(5000);

            ExecutorService executorService = Executors.newCachedThreadPool();

            while (true) {
                byte[] buffer = new byte[1024];

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                serverHandle handle = new serverHandle(socket, packet);
                
                executorService.execute(handle);
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
            String date_receive = new String(packet.getData(), 0, packet.getLength()).trim();

            System.out.println("Server received: " + date_receive);

            String date_send = messageDate(date_receive, 7);

            byte[] buffer = date_send.getBytes();
            DatagramPacket packet_send = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
            socket.send(packet_send);

            System.out.println("Server sent: " + date_send);
        } catch (Exception e) {
            System.out.println("Error in send DatagramPacket!");
            e.printStackTrace();
        }
    }

    //Phần xử lý ngày tháng năm

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

    private boolean checkDate(String txt) {
        try {
            formatter.parse(txt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean namNhuan(int nam) {
        return nam % 4 == 0 && nam % 100 != 0 || nam % 400 == 0;
    }

    private int soNgayTrongThang(int thang, int nam) {
        int[] ngayTrongThang = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        if (thang == 2 && namNhuan(nam)) {
            return 29;
        }
        return ngayTrongThang[thang - 1];
    }

    private String messageDate(String txt, int i) {
        if (!checkDate(txt)) {
            return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now());
        }

        String[] parts = txt.split("/");
        int ngay = Integer.parseInt(parts[0]) + i;
        int thang = Integer.parseInt(parts[1]);
        int nam = Integer.parseInt(parts[2]);

        if (ngay > soNgayTrongThang(thang, nam)) {
            ngay -= soNgayTrongThang(thang, nam);
            thang++;
        }

        if (thang > 12) {
            thang = 1;
            nam++;
        }

        return String.format("%02d/%02d/%04d", ngay, thang, nam);
    }
}