package Bai3;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;

public class CaroClient extends JFrame implements MouseListener, Runnable {
	public static void main(String[] args) {
		new CaroClient();
	}

	int n = 15;
	int s = 25;
	int off = 50;
	Vector<Point> dadanh = new Vector<Point>();
	Socket soc;

	public CaroClient() {
		this.setTitle("Game Caro");
		this.setSize(off * 2 + n * s, off * 2 + n * s);
		this.setDefaultCloseOperation(3);
		this.addMouseListener(this);

		try {
			soc = new Socket("localhost", 5000);
		} catch (Exception e) {
			System.exit(1);
		}
		Thread t = new Thread(this);
		t.start();
		this.setVisible(true);
	}

	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		g.setColor(Color.BLACK);
		for (int i = 0; i <= n; i++) {
			g.drawLine(off, off + i * s, off + s * n, off + i * s);
			g.drawLine(off + i * s, off, off + i * s, off + s * n);
		}
		g.setFont(new Font("arial", Font.BOLD, s));
		for (int i = 0; i < dadanh.size(); i++) {
			int ix = dadanh.get(i).x;
			int iy = dadanh.get(i).y;

			int x = off + ix * s + s - s / 2 - s / 4;
			int y = off + iy * s + s - s / 2 + s / 4;
			String str = "o";
			Color c = Color.RED;
			if (i % 2 == 1) {
				str = "x";
				c = Color.BLUE;
			}
			g.setColor(c);
			g.drawString(str, x, y);
		}
	}

	public void run() {
		while (true) {
			try {
				DataInputStream dis = new DataInputStream(soc.getInputStream());
				int ix = Integer.parseInt(dis.readUTF());
				int iy = Integer.parseInt(dis.readUTF());
				dadanh.add(new Point(ix,iy));
				this.repaint();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (x < off || x >= off + s * n)
			return;
		if (y < off || y >= off + s * n)
			return;
		int ix = (x - off) / s;
		int iy = (y - off) / s;
		try {
			DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
			dos.writeUTF("" + ix);
			dos.writeUTF("" + iy);
		} catch (Exception e1) {

		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
