package ca.jing.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ca.jing.game.Game;

public class GameClient extends Thread {
	
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Game game;
	
	public GameClient(Game game, String ipAddress){
		this.game=game;
		try {
			this.socket=new DatagramSocket();
			this.ipAddress=InetAddress.getByName(ipAddress);
			} 
		catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void run(){
		while (true){
			byte[] data=new byte[1024];
			DatagramPacket packet=new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String message=new String(packet.getData());
			System.out.println("SERVER>" + message);
		}
	}
	
	
	public void sendData(byte[] data){
		DatagramPacket packet= new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
