package OnCK.Practice.Bai2.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6001);
            ExecutorService executorService = Executors.newCachedThreadPool();
            System.out.println("Server is running on port 6001...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress() + ":" + socket.getPort());

                serverHandle handle = new serverHandle(socket);
                executorService.execute(handle);
            }
        } catch (Exception e) {
            System.out.println("Error in server!");
            e.printStackTrace();
        }
    }
}

class serverHandle extends Thread {
    private Socket socket;

    public serverHandle(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // Nhận JSON data từ client
            String receivedData = dis.readUTF();
            System.out.println("Server received: " + receivedData);

            // Parse JSON
            StudentData student = parseJSON(receivedData);

            // Validate
            String validationError = validateStudent(student);

            String response;
            if (validationError != null) {
                response = buildErrorResponse(validationError);
            } else {
                response = calculateAndBuildResponse(student);
            }

            System.out.println("Server sent: " + response);

            // Gửi response
            dos.writeUTF(response);
            dos.flush();

            // Đóng kết nối
            dis.close();
            dos.close();
            socket.close();

        } catch (Exception e) {
            System.out.println("Error in serverHandle!");
            e.printStackTrace();
        }
    }

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
            // Parse studentId
            int idStart = json.indexOf("\"studentId\":\"") + 13;
            int idEnd = json.indexOf("\"", idStart);
            student.studentId = json.substring(idStart, idEnd);

            // Parse name
            int nameStart = json.indexOf("\"name\":\"") + 8;
            int nameEnd = json.indexOf("\"", nameStart);
            student.name = json.substring(nameStart, nameEnd);

            // Parse scores
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

        // Validate studentId
        if (student.studentId == null || student.studentId.isEmpty()) {
            errors.add("studentId is empty");
        } else if (student.studentId.length() < 4 || student.studentId.length() > 10) {
            errors.add("studentId length must be 4-10 characters");
        }

        // Validate name
        if (student.name == null || student.name.isEmpty()) {
            errors.add("name is empty");
        }

        // Validate scores
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
        // Tính average, max, min
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

        // Xác định grade
        String grade = determineGrade(average);

        // Build JSON response
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
