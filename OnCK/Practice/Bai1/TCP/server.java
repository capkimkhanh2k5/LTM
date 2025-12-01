package OnCK.Practice.Bai1.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(5001);
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println("Server is running...");

        while (true) {
            try {
                Socket soc = socket.accept();
                serverHandle handle = new serverHandle(soc);
                executorService.execute(handle);

                System.out.println("Client connected: " + soc.getInetAddress().getHostAddress() + ":" + soc.getPort());
            } catch (Exception e) {
                System.out.println("Error in connection!");
            }
        }
    }
}

class serverHandle extends Thread {
    private Socket soc;

    public serverHandle(Socket soc) {
        this.soc = soc;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

            String dates = dis.readUTF();
            System.out.println("Server receive: " + dates);

            String[] tmp = dates.split("\\|");

            if(tmp.length != 2){
                dos.writeInt(-1);
                dos.flush();

                soc.close();
                dis.close();
                dos.close();

                return;
            }

            int days = (int) calculateDaysBetween(tmp[0], tmp[1]);

            dos.writeInt(days);
            dos.flush();

            dis.close();
            dos.close();
            soc.close();
        } catch (IOException e) {
            System.out.println("Error DIS or DOS in ServerHandle!");
            e.printStackTrace();
        }
    }

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

    private boolean checkFormatDate(String date){
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (Exception e) {
            System.out.println("Date from client is Invalid!");
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