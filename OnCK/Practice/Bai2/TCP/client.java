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

            // TODO: Tạo JSON data cho sinh viên
            String jsonData = buildJSON("SV001", "Nguyen Van A", new double[] { 8.5, 9.0, 7.5, 8.0, 9.5 });

            System.out.println("Client sent: " + jsonData);

            // Gửi JSON data đến server
            dos.writeUTF(jsonData);
            dos.flush();

            // Nhận response từ server
            String response = dis.readUTF();
            System.out.println("Client received: " + response);

            // Hiển thị kết quả
            displayResult(response);

            // Đóng kết nối
            dis.close();
            dos.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error in client communication!");
            e.printStackTrace();
        }
    }

    private String buildJSON(String studentId, String name, double[] scores) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"studentId\":\"").append(studentId).append("\",");
        sb.append("\"name\":\"").append(name).append("\",");
        sb.append("\"scores\":[");

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
        System.out.println("\n=== KẾT QUẢ THỐNG KÊ ===");

        if (jsonResponse.contains("\"error\"")) {
            int errorStart = jsonResponse.indexOf("\"error\":\"") + 9;
            int errorEnd = jsonResponse.indexOf("\"", errorStart);
            String error = jsonResponse.substring(errorStart, errorEnd);
            System.out.println("ERROR: " + error);
        } else {
            // TODO: Parse và hiển thị các trường
            try {
                String studentId = extractValue(jsonResponse, "studentId");
                String name = extractValue(jsonResponse, "name");
                String average = extractValue(jsonResponse, "average");
                String max = extractValue(jsonResponse, "max");
                String min = extractValue(jsonResponse, "min");
                String grade = extractValue(jsonResponse, "grade");
                String totalScores = extractValue(jsonResponse, "totalScores");

                System.out.println("Mã SV: " + studentId);
                System.out.println("Tên: " + name);
                System.out.println("Điểm TB: " + average);
                System.out.println("Điểm cao nhất: " + max);
                System.out.println("Điểm thấp nhất: " + min);
                System.out.println("Xếp loại: " + grade);
                System.out.println("Tổng số điểm: " + totalScores);
            } catch (Exception e) {
                System.out.println("Error parsing response!");
            }
        }
    }

    private String extractValue(String json, String key) {
        String searchPattern = "\"" + key + "\":";
        int start = json.indexOf(searchPattern);
        if (start == -1)
            return "";

        start += searchPattern.length();

        // Skip quote if it's a string value
        if (json.charAt(start) == '"') {
            start++;
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } else {
            // It's a number
            int end = start;
            while (end < json.length() &&
                    (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.' || json.charAt(end) == '-')) {
                end++;
            }
            return json.substring(start, end);
        }
    }
}
