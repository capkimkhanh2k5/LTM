package THICK;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class client extends JFrame {
	Socket soc;
	BufferedImage currentImage = null;
	boolean isRunning = true;
	JPanel screenPanel;

	public static void main(String[] args) {
		new client();
	}

	public client() {
		this.setTitle("Share Screen");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		screenPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (currentImage != null) {
					g.drawImage(currentImage, 0, 0, this.getWidth(), this.getHeight(), null);
				}
			}
		};
		this.add(screenPanel);
		this.setVisible(true);

		new Thread(() -> {
			try {
				soc = new Socket("localhost", 2345);
				soc.setSoTimeout(10000); // 10s timeout

				DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
				DataInputStream dis = new DataInputStream(soc.getInputStream());

				dos.writeUTF("Please livestream");
				System.out.println("Client Send: PLEASE LIVESTREAM");
				dos.flush();

				String response = dis.readUTF();
				System.out.println("Client received: " + response);
				if ("OK".equalsIgnoreCase(response)) {
					while (isRunning) {
						try {
							dos.writeUTF("Get Video");
							System.out.println("Client Send: GET VIDEO");
							dos.flush();

							int len = dis.readInt();
							if (len > 0) {
								byte[] data = new byte[len];
								dis.readFully(data);

								ByteArrayInputStream bais = new ByteArrayInputStream(data);
								currentImage = ImageIO.read(bais);
								screenPanel.repaint();
							}

							Thread.sleep(50);
						} catch (Exception e) {
							System.out.println("Error during streaming: " + e.getMessage());
							break;
						}
					}
				} else {
					System.out.println("Server rejected request (BAND)!");
				}
			} catch (Exception e) {
				System.out.println("Connection error or timeout: " + e.getMessage());
			} finally {
				try {
					if (soc != null)
						soc.close();
				} catch (Exception ex) {
				}
				System.exit(0);
			}
		}).start();
	}

}
