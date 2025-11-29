package ExchangeMoney;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ExchangeRateServer extends Thread{

	public static void main(String[] args) {
		new ExchangeRateServer();
	}
	Map<String, Double> tg = new HashMap<String, Double>();
	public void run() {
		Random rand = new Random();
		while(true) {
			for (String key:tg.keySet()) {
				double tg_now = tg.get(key);
				double tg_new = tg_now*(1+(rand.nextDouble()-0.5)*0.01);
				tg.put(key, tg_new);
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}
	public ExchangeRateServer() {
		tg.put("USD",26166.00);
		tg.put("EUR",30207.65);
		tg.put("JPY",168.41);
		tg.put("VND",1.0);
		this.start();
		try {
			DatagramSocket TT = new DatagramSocket(6000);
			while(true) {
				DatagramPacket lt = new DatagramPacket(new byte[100], 100);
				TT.receive(lt);
				
				String str = new String(lt.getData()).substring(0,lt.getLength());
				if (!"ExchangeRate".equals(str.substring(0, 12))) continue;
				String name1 = str.substring(12, 15);
				if (!"to".equals(str.substring(15, 17))) continue;
				String name2 = str.substring(17, 20);
				String _tg = tg.get(name1)/tg.get(name2)+"";
			
				DatagramPacket ltres = new DatagramPacket(_tg.getBytes(), _tg.length(),
									lt.getAddress(),lt.getPort());
				TT.send(ltres);
			}
		} catch (Exception e) {

		}
	}

}
