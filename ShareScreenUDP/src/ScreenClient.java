package ShareScreenUDP.src;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ScreenClient extends JFrame {
	DatagramSocket TT;
	public static void main(String[] args) {
		new ScreenClient();
	}
	int off = 50;
	public ScreenClient() {
		this.setTitle("Share Screen");
		this.setSize(500, 400);
		this.setDefaultCloseOperation(3);
		try {
			TT = new DatagramSocket();
		} catch(Exception e) {
			System.exit(1);
		}

		this.setVisible(true);
	}

	public void paint(Graphics g) {
		try {
			DatagramPacket lt = new DatagramPacket(".".getBytes(), 1, InetAddress.getLocalHost(),5000);
			TT.send(lt);
			System.out.println("Client send");
			byte buf[] = new byte[200000];
			DatagramPacket ltres = new DatagramPacket(buf, 200000);
			TT.receive(ltres);
			System.out.println("Client receive");
			
			byte tmp[] = new byte[ltres.getLength()];
			for (int i=0;i<tmp.length;i++)
				tmp[i]=buf[i];
			
			
			ByteArrayInputStream bis1 = new ByteArrayInputStream(tmp);
			BufferedImage img1 = ImageIO.read(bis1);
			int w = this.getWidth()-2*off;
			int h = this.getHeight()-2*off;
			Image img2 = img1.getScaledInstance(w,h, Image.SCALE_SMOOTH);
			
			g.drawImage(img2, off, off, this.getWidth()-off, this.getHeight()-off, 
					0, 0, w,h, null);
			
		} catch (Exception e) {
		}
		this.repaint();
	}

}

