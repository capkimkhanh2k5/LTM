package Bai1;

import java.net.InetAddress;

public class getMyIP {

	public static void main(String[] args) {
		try {
			InetAddress myip = InetAddress.getLocalHost();
			System.out.println(myip.getHostName());
			System.out.println(myip.getHostAddress());
		} catch(Exception e) {
			
		}

	}

}
