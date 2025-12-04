package THICK;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Vector;

import javax.imageio.ImageIO;

public class server {
	private static Vector<String> blackList = new Vector<>(); // BlackList save IP client
	public static final Object lock = new Object();

	public static void main(String[] args) {
		new server();
	}

	public server() {
		Screen s = new Screen();

		s.start();
		try {
			ServerSocket server = new ServerSocket(2345);
			while (true) {
				Socket soc = server.accept();
				String clientIP = soc.getInetAddress().getHostAddress();

				//Test band
				//blackList.add(clientIP);

				if (blackList.contains(clientIP)) {
					System.out.println("Client ID: " + soc.getInetAddress().getHostAddress() + " is Band!");

					soc.close();
					continue;
				}

				ScreenProcessing sp = new ScreenProcessing(soc);
				sp.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class Screen extends Thread {
	static byte[] tmp;

	public void run() {
		Robot r = null;
		Rectangle capture = null;

		try {
			r = new Robot();
			capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				BufferedImage img = r.createScreenCapture(capture);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();

				ImageIO.write(img, "png", bos);
				bos.flush();

				synchronized (server.lock) {
					tmp = bos.toByteArray();
				}

				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class ScreenProcessing extends Thread {
	Socket soc;

	public ScreenProcessing(Socket soc) {
		this.soc = soc;
	}

	public void run() {
		DataInputStream dis = null;
		DataOutputStream dos = null;
		try {
			dis = new DataInputStream(soc.getInputStream());
			dos = new DataOutputStream(soc.getOutputStream());

			String msg = dis.readUTF();
			System.out.println("Server received: " + msg);

			if (msg.equalsIgnoreCase("Please livestream")) {
				dos.writeUTF("OK");
				dos.flush();

				System.out.println("Server Send: OK");

				while (true) {
					String request = dis.readUTF();
					if (request.equalsIgnoreCase("Get Video")) {
						byte[] data = null;
						synchronized (server.lock) {
							data = Screen.tmp;
						}

						if (data != null) {
							dos.writeInt(data.length);
							dos.write(data);
							dos.flush();
						} else {
							// Send 0 if no data yet
							dos.writeInt(0);
							dos.flush();
						}
					} else {
						// Unknown command or disconnect
						break;
					}
				}
			}

		} catch (Exception e) {
			// Client disconnected
			System.out.println("Client is Disconnected");
		} finally {
			try {
				if (soc != null)
					soc.close();
			} catch (Exception ex) {
			}
		}
	}
}
