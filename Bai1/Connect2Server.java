package Bai1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connect2Server {

	public static void main(String[] args) {
		try {
			Socket soc = new Socket("vnexpress.vn",80);
			PrintWriter pw = new PrintWriter(soc.getOutputStream(),true);
			pw.println("GET / HTTP/1.1");
			pw.println("Host: vnexpress.vn");
			pw.println();
			BufferedReader br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			String s;
			while ((s=br.readLine())!=null)
				System.out.println(s);
			System.out.println("Done!!");
		}catch(Exception e) {
			System.out.println("Error!!");
		}

	}

}
