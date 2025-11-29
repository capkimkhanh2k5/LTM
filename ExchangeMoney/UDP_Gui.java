package ExchangeMoney;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramSocket;

public class UDP_Gui {

	public static void main(String[] args) throws Exception {
		DatagramSocket TT = new DatagramSocket();
		String str = "Em quen anh roi sao!!";
		DatagramPacket lt = new DatagramPacket(str.getBytes(), str.length(),
					InetAddress.getLocalHost(),5555);
		TT.send(lt);
	}
}
