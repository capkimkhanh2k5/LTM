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

            // TODO: Tạo JSON string cho dữ liệu sinh viên
            // Format: {"studentId":"SV001","name":"Nguyen Van A","scores":[8.5,9.0,7.5,8.0,9.5]}
            String jsonData = buildJSON("SV001", "Nguyen Van A", new double[]{8.5, 9.0, 7.5, 8.0, 9.5});
            
            System.out.println("Client sent: " + jsonData);

            // TODO: Gửi JSON data đến server
            byte[] sendBuffer = jsonData.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                sendBuffer, 
                sendBuffer.length, 
                InetAddress.getLocalHost(), 
                port
            );
            socket.send(sendPacket);

            // TODO: Nhận kết quả từ server
            byte[] receiveBuffer = new byte[2048];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
            System.out.println("Client received: " + response);

            // TODO: Parse và hiển thị kết quả
            displayResult(response);

            socket.close();
        } catch (Exception e) {
            System.out.println("Error in client!");
            e.printStackTrace();
        }
    }

    // Hàm hỗ trợ build JSON (không dùng thư viện)
    private String buildJSON(String studentId, String name, double[] scores) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"studentId\":\"").append(studentId).append("\",");
        sb.append("\"name\":\"").append(name).append("\",");
        sb.append("\"scores\":[");
        
        // TODO: Thêm các điểm vào mảng scores
        for (int i = 0; i < scores.length; i++) {
            sb.append(scores[i]);
            if (i < scores.length - 1) {
                sb.append(",");
            }
        }
        
        sb.append("]}");
        return sb.toString();
    }

    private void displayResult(String jsonResponse) {
        // TODO: Parse JSON response và hiển thị kết quả
        // Gợi ý: Sử dụng String methods như indexOf, substring để parse
        System.out.println("\n=== KẾT QUẢ THỐNG KÊ ===");
        
        if (jsonResponse.contains("\"error\"")) {
            // Xử lý lỗi
            int errorStart = jsonResponse.indexOf("\"error\":\"") + 9;
            int errorEnd = jsonResponse.indexOf("\"", errorStart);
            String error = jsonResponse.substring(errorStart, errorEnd);
            System.out.println("ERROR: " + error);
        } else {
            // TODO: Parse và hiển thị các trường: average, max, min, grade, totalScores
            System.out.println("Response: " + jsonResponse);
        }
    }
}
