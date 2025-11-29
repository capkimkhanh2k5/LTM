package ShareScreenUDP.src;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.imageio.ImageIO;

public class ScreenServer {
	public static void main(String[] args) {
		new ScreenServer();
	}

	public ScreenServer() {
		Screen s = new Screen();
		s.start();
		try {
			DatagramSocket TT = new DatagramSocket(5000);
			while(true) {
				DatagramPacket lt = new DatagramPacket(new byte[10], 10);
				TT.receive(lt);
				System.out.println("Server receive");
				
				
				byte[] buf = Screen.tmp;
				int lensplt = 10000;
				int sl = (Screen.tmp.length+lensplt-1)/lensplt;
				String header = Screen.tmp.length+","+sl+","+lensplt;
				
				
				DatagramPacket ltres = new DatagramPacket(header.getBytes(), header.length(),lt.getAddress(),lt.getPort());
				TT.send(ltres);
				for (int i=0;i<sl;i++) {
					byte bufi[] = new byte[lensplt];
					for (int j=0; j<lensplt && i*lensplt+j<buf.length;j++)
						bufi[j] = buf[i*lensplt+j];
					DatagramPacket lti = new DatagramPacket(bufi, bufi.length,lt.getAddress(),lt.getPort());
					TT.send(lti);
				}
				
				System.out.println("Server send");
				
				
				
			}
			
		} catch (Exception e) {

		}
	}
	
}

class Screen extends Thread{
	static byte[] tmp;
	static int count = 0;
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_3BYTE_BGR);
	    Graphics g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    return dimg;
	}  
	public void run() {
		Robot r = null;
		Rectangle capture = null;
		try {
			r = new Robot();
			capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		}catch(Exception e) {
			
		}
		while (true) {
			try {
				BufferedImage img = r.createScreenCapture(capture);
				//img = resize(img, 200, 120);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(img, "jpg", bos);
				bos.flush();
				tmp = bos.toByteArray();
				count++;
				if (count%100==0)
					System.out.println(count+":"+tmp.length);
			}catch(Exception e) {
				
			}
		}
	}
}


