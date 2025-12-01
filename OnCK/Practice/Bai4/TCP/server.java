package OnCK.Practice.Bai4.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5001);
            ExecutorService executorService = Executors.newCachedThreadPool();
            System.out.println("Server is running on port 5001...");

            while (true) {
                Socket soc = serverSocket.accept();
                System.out.println("Client connected: " + soc.getInetAddress().getHostAddress() + ":" + soc.getPort());

                serverHandle handle = new serverHandle(soc);
                executorService.execute(handle);
            }
        } catch (Exception e) {
            System.out.println("Error in server!");
            e.printStackTrace();
        }
    }
}

class serverHandle extends Thread {
    private Socket soc;
    private List<String> history = new ArrayList<>();
    private List<String> header = List.of("CALC", "FACTOR", "PRIME", "HISTORY", "EXIT");

    public serverHandle(Socket soc) {
        this.soc = soc;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

            while (true) {
                // Nhận dữ liệu từ client
                String receivedData = dis.readUTF();
                String response = "Response from server";
                String command = receivedData.toUpperCase().trim();

                System.out.println("Server received: " + receivedData);

                // Xử lý dữ liệu
                if (!checkHeader(command)) {
                    response = "ERROR: Invalid header!";
                }

                if (command.startsWith("EXIT")) {

                    dos.writeUTF("EXIT");
                    dos.flush();

                    System.out.println("End connection with client: " + soc.getInetAddress() + ":" + soc.getPort());
                    break;
                } else if (command.startsWith("HISTORY")) {
                    if (history.size() > 0)
                        response = history.toString().replace("[", "").replace("]", "");
                    else
                        response = "ERROR: History is empty!";
                } else {
                    response = handerData(receivedData);
                }

                // Gửi response về client
                dos.writeUTF(response);
                dos.flush();
                System.out.println("Server sent: " + response);

                // Lưu vào lịch sử
                history.add("\nClient: " + receivedData + "\nServer: " + response + "\n\n");
            }
        } catch (IOException e) {
            System.out.println("Error in serverHandle!");
            e.printStackTrace();
        } finally {
            try {
                soc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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