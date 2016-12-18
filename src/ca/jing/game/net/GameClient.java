package ca.jing.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ca.jing.game.Game;
import ca.jing.game.entities.PlayerMP;
import ca.jing.game.net.packets.Packet;
import ca.jing.game.net.packets.Packet00Login;
import ca.jing.game.net.packets.Packet.PacketTypes;

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
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			//String message=new String(packet.getData());
			//System.out.println("SERVER>" + message);
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
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type=Packet.lookupPacket(message.substring(0,2));
		Packet packet=null;
		switch(type){
		case INVALID:
			break;
			
		case LOGIN:
			//Packet00Login packet= new Packet00Login(data);
			packet= new Packet00Login(data);
			
			System.out.println("[" + address.getHostAddress()+":"+ port+"]"+ ((Packet00Login)packet).getUsername() + "has joined game ...");
			PlayerMP player=new PlayerMP(game.level, 100, 100, ((Packet00Login)packet).getUsername(),address,port);;
			//if (address.getHostAddress().equalsIgnoreCase("127.0.0.1")){
			//	player=new PlayerMP(game.level, 100, 100, game.input, packet.getUsername(),address,port);
			//}else{
				
			//}
			game.level.addEntity(player);
			//if (player!=null){
			//this.connectedPlayers.add(player);
			//game.level.addEntity(player);
			//game.player=player;
			//}
			break;
		case DISCONNECT:
			break;
		}
	}
}
