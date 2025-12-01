package OnCK.Practice.Bai4.UDP;

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
    private List<String> history = new ArrayList<>();
    private List<String> header = List.of("CALC", "FACTOR", "PRIME", "HISTORY", "EXIT");

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

            // TODO: Xử lý dữ liệu
            String response = "Response from server";

            // Gửi response về client
            byte[] buffer = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                    buffer,
                    buffer.length,
                    packet.getAddress(),
                    packet.getPort());
            socket.send(sendPacket);

            System.out.println("Server sent: " + response);
        } catch (Exception e) {
            System.out.println("Error in serverHandle!");
            e.printStackTrace();
        }
    }

    private String handerData(String receivedData) {
        String content = contentData(receivedData).trim();
        System.out.println("Content Data: " + content);

        String commandUpper = receivedData.toUpperCase();

        if (commandUpper.startsWith("CALC")) {
            return Calc(content);
        } else if (commandUpper.startsWith("FACTOR")) {
            return Factor(content);
        } else if (commandUpper.startsWith("PRIME")) {
            try {
                int n = Integer.parseInt(content);

                if (isPrime(n))
                    return "OK: TRUE";

                return "OK: FALSE";
            } catch (Exception e) {
                return "ERROR: Invalid input!";
            }
        }
        return "ERROR: Invalid input!";
    }

    // Cacl
    public String Calc(String expression) {
        try {
            String expr = expression.replace(":", "/");
            double result = evaluate(expr);
            // Format result: remove .0 if integer
            if (result == (long) result) {
                return "OK: " + (long) result;
            }
            return "OK: " + result;
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: Invalid expression!";
        }
    }

    public double evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        java.util.Stack<Double> values = new java.util.Stack<>();
        java.util.Stack<Character> ops = new java.util.Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ')
                continue;

            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuilder sbuf = new StringBuilder();
                while (i < tokens.length && ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.')) {
                    sbuf.append(tokens[i++]);
                }
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            } else if (tokens[i] == '(') {
                ops.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/'
                    || tokens[i] == '^') {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.push(tokens[i]);
            }
        }
        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        return values.pop();
    }

    public boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if (op1 == '^' && op2 != '^')
            return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        return true;
    }

    public double applyOp(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            case '^':
                return Math.pow(a, b);
        }
        return 0;
    }

    // Factor
    public String Factor(String numberStr) {
        try {
            long n = Long.parseLong(numberStr.trim());
            if (n < 2)
                return numberStr;

            List<String> factors = new ArrayList<>();

            int count = 0;
            while (n % 2 == 0) {
                count++;
                n /= 2;
            }
            if (count > 0) {
                factors.add(count > 1 ? "2^" + count : "2");
            }

            for (long i = 3; i <= Math.sqrt(n); i += 2) {
                if (n % i == 0) {
                    count = 0;
                    while (n % i == 0) {
                        count++;
                        n /= i;
                    }
                    factors.add(count > 1 ? i + "^" + count : String.valueOf(i));
                }
            }

            if (n > 1) {
                factors.add(String.valueOf(n));
            }

            return "OK: " + String.join("*", factors);

        } catch (Exception e) {
            return "ERROR: Invalid input!";
        }
    }

    // Kiểm tra Prime
    private boolean isPrime(int n) {
        if (n < 2)
            return false;
        for (int i = 2; i < Math.sqrt(n); i++) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    // Lấy nội dung Data
    private String contentData(String data) {
        String[] tmp = data.split(":", 2);
        return tmp[1];
    }

    // Kiểm tra header hợp lệ
    private boolean checkHeader(String receivedData) {
        for (String hd : header) {
            if (receivedData.startsWith(hd))
                return true;
        }

        return false;
    }
}