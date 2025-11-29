package ExchangeMoney;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramSocket;

public class UDP_Nhan {

	public static void main(String[] args) throws Exception {
		DatagramSocket TT = new DatagramSocket(5555);
		while (true) {
			DatagramPacket lt = new DatagramPacket(new byte[100], 100);
			TT.receive(lt);
			String str = new String(lt.getData()).substring(0, lt.getLength());
			System.out.println(str);
		}
	}
}
