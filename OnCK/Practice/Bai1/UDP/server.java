package OnCK.Practice.Bai1.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

            // Tạo ExecutorService để xử lý đa luồng
            ExecutorService executorService = Executors.newCachedThreadPool();

            while (true) {
                byte[] buffer = new byte[1024];

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // Tạo serverHandle và submit vào ExecutorService
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
            // Nhận dữ liệu từ client
            String receivedData = new String(packet.getData(), 0, packet.getLength()).trim();

            System.out.println("Server received: " + receivedData);

            // Tách chuỗi thành hai ngày (split bởi "|")
            String[] dates = receivedData.split("\\|");
            byte[] buffer = new byte[1024];

            //Kiểm tra ngày hợp lệ và đủ ngày start và end
            if(dates.length != 2){
                buffer = new byte[1024];
                buffer = String.valueOf(-1).getBytes();
                packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                socket.send(packet);
                return;
            }

            // Tính số ngày giữa hai ngày
            long daysBetween = calculateDaysBetween(dates[0], dates[1]);

            // Gửi kết quả về client
            buffer = String.valueOf(daysBetween).getBytes();
            packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
            socket.send(packet);

            System.out.println("Server sent: " + daysBetween);
        } catch (Exception e) {
            System.out.println("Error in send DatagramPacket!");
            e.printStackTrace();
        }
    }

    // Phần xử lý ngày tháng năm
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

    private boolean checkFormatDate(String txt) {
        try {
            LocalDate.parse(txt, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private long calculateDaysBetween(String date1Str, String date2Str) {
        try {
            if(!checkFormatDate(date1Str) || !checkFormatDate(date2Str)){
                System.out.println("Error Format Date!");
                return -1;
            }

            // Parse hai ngày
            LocalDate date1 = LocalDate.parse(date1Str, formatter);
            LocalDate date2 = LocalDate.parse(date2Str, formatter);

            // Kiểm tra date1 <= date2
            if (date1.isAfter(date2)) {
                System.out.println("Error Date! Start > End");
                return -1;
            }

            // Tính số ngày giữa hai ngày (bao gồm cả ngày bắt đầu và kết thúc)
            long daysBetween = ChronoUnit.DAYS.between(date1, date2) + 1;

            return daysBetween;
        } catch (Exception e) {
            return -1;
        }
    }
}
