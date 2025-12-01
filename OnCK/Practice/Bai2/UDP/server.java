package OnCK.Practice.Bai2.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
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
                byte[] buffer = new byte[2048];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
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
            String receivedData = new String(packet.getData(), 0, packet.getLength()).trim();
            System.out.println("Server received: " + receivedData);

            // TODO: Parse JSON từ client
            StudentData student = parseJSON(receivedData);

            // TODO: Validate dữ liệu
            String validationError = validateStudent(student);

            String response;
            if (validationError != null) {
                // Gửi error response
                response = buildErrorResponse(validationError);
            } else {
                // Tính toán thống kê
                response = calculateAndBuildResponse(student);
            }

            System.out.println("Server sent: " + response);

            // Gửi response về client
            byte[] buffer = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                    buffer,
                    buffer.length,
                    packet.getAddress(),
                    packet.getPort());
            socket.send(sendPacket);

        } catch (Exception e) {
            System.out.println("Error in serverHandle!");
            e.printStackTrace();
        }
    }

    // Class để lưu dữ liệu sinh viên
    private static class StudentData {
        String studentId;
        String name;
        List<Double> scores;

        StudentData() {
            scores = new ArrayList<>();
        }
    }

    private StudentData parseJSON(String json) {
        StudentData student = new StudentData();

        try {
            // TODO: Parse studentId
            // Gợi ý: Tìm vị trí của "studentId":" và lấy giá trị đến dấu " tiếp theo
            int idStart = json.indexOf("\"studentId\":\"") + 13;
            int idEnd = json.indexOf("\"", idStart);
            student.studentId = json.substring(idStart, idEnd);

            // TODO: Parse name
            int nameStart = json.indexOf("\"name\":\"") + 8;
            int nameEnd = json.indexOf("\"", nameStart);
            student.name = json.substring(nameStart, nameEnd);

            // TODO: Parse scores array
            int scoresStart = json.indexOf("\"scores\":[") + 10;
            int scoresEnd = json.indexOf("]", scoresStart);
            String scoresStr = json.substring(scoresStart, scoresEnd);

            if (!scoresStr.isEmpty()) {
                String[] scoreTokens = scoresStr.split(",");
                for (String token : scoreTokens) {
                    student.scores.add(Double.parseDouble(token.trim()));
                }
            }

        } catch (Exception e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
        }

        return student;
    }

    private String validateStudent(StudentData student) {
        List<String> errors = new ArrayList<>();

        // TODO: Validate studentId (không rỗng, độ dài 4-10)
        if (student.studentId == null || student.studentId.isEmpty()) {
            errors.add("studentId is empty");
        } else if (student.studentId.length() < 4 || student.studentId.length() > 10) {
            errors.add("studentId length must be 4-10 characters");
        }

        // TODO: Validate name (không rỗng)
        if (student.name == null || student.name.isEmpty()) {
            errors.add("name is empty");
        }

        // TODO: Validate scores (1-10 phần tử, mỗi điểm trong [0,10])
        if (student.scores.isEmpty() || student.scores.size() > 10) {
            errors.add("scores must have 1-10 elements");
        } else {
            for (double score : student.scores) {
                if (score < 0 || score > 10) {
                    errors.add("scores contain invalid values (must be 0-10)");
                    break;
                }
            }
        }

        if (errors.isEmpty()) {
            return null;
        }

        return String.join(", ", errors);
    }

    private String buildErrorResponse(String error) {
        return "{\"error\":\"" + error + "\"}";
    }

    private String calculateAndBuildResponse(StudentData student) {
        // TODO: Tính average, max, min
        double sum = 0;
        double max = student.scores.get(0);
        double min = student.scores.get(0);

        for (double score : student.scores) {
            sum += score;
            if (score > max)
                max = score;
            if (score < min)
                min = score;
        }

        double average = sum / student.scores.size();

        // TODO: Xác định grade
        String grade = determineGrade(average);

        // TODO: Build JSON response
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"studentId\":\"").append(student.studentId).append("\",");
        sb.append("\"name\":\"").append(student.name).append("\",");
        sb.append("\"average\":").append(String.format("%.2f", average)).append(",");
        sb.append("\"max\":").append(max).append(",");
        sb.append("\"min\":").append(min).append(",");
        sb.append("\"grade\":\"").append(grade).append("\",");
        sb.append("\"totalScores\":").append(student.scores.size());
        sb.append("}");

        return sb.toString();
    }

    private String determineGrade(double average) {
        // TODO: Xác định xếp loại dựa vào average
        if (average >= 9.0)
            return "Xuất sắc";
        if (average >= 8.0)
            return "Giỏi";
        if (average >= 7.0)
            return "Khá";
        if (average >= 5.0)
            return "Trung bình";
        return "Yếu";
    }
}
