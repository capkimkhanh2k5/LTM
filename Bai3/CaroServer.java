package Bai3;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Vector;

public class CaroServer {
	static Vector<xuly> clients = new Vector<xuly>();
	static Vector<Point> dadanh = new Vector<Point>();
	static int n = 15;
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(5000);
		while (true) {
			try {
				Socket soc = server.accept();
				xuly x = new xuly(soc);
				clients.add(x);
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
			DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
			for (int i=0;i<CaroServer.dadanh.size();i++) {
				dos.writeUTF(CaroServer.dadanh.get(i).x+"");
				dos.writeUTF(CaroServer.dadanh.get(i).y+"");
			}
		}catch(Exception e) {
			
		}
		
		while (true) {
			try {
				DataInputStream dis = new DataInputStream(soc.getInputStream());
				int ix = Integer.parseInt(dis.readUTF());
				int iy = Integer.parseInt(dis.readUTF());
				//Kiem tra tinh hop le
				//1. kiem tra co phai la 2 client dau hay khong
				if (this!=CaroServer.clients.get(0) && this!=CaroServer.clients.get(1))
					continue;
				//2. kiem tra luot choi co hop le khong
				if (this==CaroServer.clients.get(0) && CaroServer.dadanh.size()%2!=0)
					continue;
				if (this==CaroServer.clients.get(1) && CaroServer.dadanh.size()%2!=1)
					continue;
				//3. kiem tra o do trung hay chua
				boolean trung = false;
				for (Point p:CaroServer.dadanh) {
					if (p.x == ix && p.y == iy) {
						trung = true;
					}
				}
				if (trung) continue;
				CaroServer.dadanh.add(new Point(ix,iy));
				for (xuly c: CaroServer.clients) {
					try {
						DataOutputStream dos = new DataOutputStream(c.soc.getOutputStream());
						dos.writeUTF(""+ix);
						dos.writeUTF(""+iy);
					} catch(Exception e2) {
						
					}
				}
				
			} catch (Exception e1) {
			}
		}
	}
}
