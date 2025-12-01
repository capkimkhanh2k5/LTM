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
            
            while(true){
                //Nhận dữ liệu từ client
                String receivedData = dis.readUTF();
                String response = "Response from server";

                System.out.println("Server received: " + receivedData);

                //Xử lý dữ liệu
                if(!checkHeader(receivedData)){
                    response = "ERROR: Invalid header!";
                }

                if(receivedData.startsWith("EXIT")){
                    soc.close();
                    System.out.println("End connection with client: " + soc.getInetAddress() + ":" + soc.getPort());
                    break;
                } else if(receivedData.startsWith("HISTORY")){
                    response = history.toString();
                } else {
                    response = handerData(receivedData);
                }
                
                //Gửi response về client
                dos.writeUTF(response);
                dos.flush();
                System.out.println("Server sent: " + response);

                //Lưu vào lịch sử
                String htr = null;
                history.add(receivedData + "\n" + response + "\n\n");
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
        if(receivedData.startsWith("CALC")){   
            String content = contentData(receivedData);
            
        } else if(receivedData.startsWith("FACTOR")){
            String content = contentData(receivedData);
            
        } else if(receivedData.startsWith("PRIME")){
            String content = contentData(receivedData);

            try{
                int n = Integer.parseInt(content);

                if(isPrime(n)) return "OK: TRUE";
                
                return "OK: FALSE";
            } catch(Exception e){
                return "ERROR: Invalid input!";
            }
        }

        return null;
    }

    //Cacl
    public String Calc(String expression) {
        try {
            String expr = expression.replace(":", "/");
            
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
            
            Object result = engine.eval(expr);
            return "OK: " + result.toString();
        } catch (Exception e) {
            return "ERROR: Invalid expression or main calculation!";
        }
    }

    //Factor
    public String Factor(String numberStr) {
        try {
            long n = Long.parseLong(numberStr.trim());
            if (n < 2) return numberStr;

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

    //Kiểm tra Prime
    private boolean isPrime(int n){
        if(n < 2) return false;
        for(int i = 2; i < Math.sqrt(n); i++){
            if(n % i == 0) return false;
        }
        return true;
    }

    //Lấy nội dung Data
    private String contentData(String data){
        String[] tmp = data.split(":",2);
        return tmp[1];
    }

    //Kiểm tra header hợp lệ
    private boolean checkHeader(String receivedData) {
        for(String hd : header){
            if(receivedData.startsWith(hd)) return true;
        }

        return false;
    }
}