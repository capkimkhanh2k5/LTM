package Bai1;

import java.net.InetAddress;

public class getAllMyIP2 {

	public static void main(String[] args) {
		try {
			InetAddress myips[] = InetAddress.getAllByName("google.com");
			for (InetAddress myip : myips) {
				System.out.println(myip.getHostName());
				System.out.println(myip.getHostAddress());
			}
		} catch (Exception e) {
		}
	}
}
