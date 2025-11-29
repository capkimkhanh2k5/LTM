package ExchangeMoney;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ExchangeRateTable extends JFrame{
	public static void main(String[] args) {
		new ExchangeRateTable();
	}
	public ExchangeRateTable() {
		this.setTitle("ExchangeRateTable");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(3);
		this.setLayout(new GridLayout(2,2));
		
		ExchangeRate e1 = new ExchangeRate("ExchangeRateUSDtoJPY");
		ExchangeRate e2 = new ExchangeRate("ExchangeRateJPYtoVND");
		ExchangeRate e3 = new ExchangeRate("ExchangeRateJPYtoVND");
		ExchangeRate e4 = new ExchangeRate("ExchangeRateJPYtoVND");
		this.add(e1);
		this.add(e2);
		this.add(e3);
		this.add(e4);
		
		this.setVisible(true);
		new Thread(e1).start();
		new Thread(e2).start();
		new Thread(e3).start();
		new Thread(e4).start();
	}
}

class ExchangeRate extends JPanel implements Runnable {
	String cmd= "ExchangeRateUSDtoJPY";

	public ExchangeRate(String cmd) {
		this.cmd = cmd;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLUE);
		g.drawRect(1, 1, this.getWidth()-2, this.getHeight()-2);
		int last = 0;
		for (int i=1;i<_tg.size();i++) {
			int x1 = (i-1)*5;
			double y1 = _tg.get(i-1);
			int x2 = i*5;
			double y2 = _tg.get(i);
			g.drawLine(x1, this.getHeight()-(int)y1, x2, this.getHeight()-(int)y2);
			last = (int)y2;
		}
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString(cmd+":"+last, 50, 20);
	}
	Vector<Double> _tg = new Vector<Double>();
	public void run() {
		try {
			DatagramSocket TT = new DatagramSocket();
			while (true) {
				DatagramPacket lt = new DatagramPacket(cmd.getBytes(), cmd.length(),
						InetAddress.getLocalHost(),6000);
				TT.send(lt);
				
				DatagramPacket ltres = new DatagramPacket(new byte[100], 100);
				TT.receive(ltres);
				String tg = new String(ltres.getData()).substring(0,ltres.getLength());
				_tg.add(Double.parseDouble(tg));
				while(_tg.size()*5>=this.getWidth()) _tg.remove(0);
				this.repaint();
				Thread.sleep(100);
			}
		} catch (Exception e) {

		}
	}
}
