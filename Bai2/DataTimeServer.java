package Bai2;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DataTimeServer {
	public static void main(String[] args) throws Exception{
		ServerSocket server = new ServerSocket(5000);
		while (true) {
			try {
				Socket soc = server.accept();
				xuly x = new xuly(soc);
				x.start();
			} catch (Exception e1) {
			}
		}
	}
}
class xuly extends Thread {
	Socket soc;
	public xuly(Socket soc) {
		this.soc = soc;
	}
	public void run() {
		try {
			System.out.println(soc.getInetAddress().getHostAddress());
			DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
			dos.writeUTF("Bay gio tren may thay Tuan la:" + new Date().toString());
		} catch (Exception e1) {
		}
	}
}
