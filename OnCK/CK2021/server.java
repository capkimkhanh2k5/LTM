package OnCK.CK2021;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        while (true) {
            try {
                DataInputStream dis = new DataInputStream(soc.getInputStream());
                DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

                String txt = dis.readUTF();
                System.out.println("Server receive: " + txt);

                String message = messageDate(txt, 7);
                System.out.println("Server send: " + message);

                dos.writeUTF(message);
                dos.flush();
            } catch (Exception e) {
                System.out.println("Error DIS or DOS in ServerHandle!");
                e.printStackTrace();
                break;
            }
        }
    }

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