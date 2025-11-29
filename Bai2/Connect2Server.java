package Bai2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connect2Server {

	public static void main(String[] args) {
		try {
			//wifi LAN:WebTuan
			//pass:machinelearning0705
			Socket soc = new Socket("192.168.10.62",3000);
			DataInputStream dis = new DataInputStream(soc.getInputStream());
			System.out.println(dis.readUTF());
		}catch(Exception e) {
			System.out.println("Error!!");
		}

	}

}
