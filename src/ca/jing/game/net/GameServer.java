package ca.jing.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import ca.jing.game.Game;
import ca.jing.game.entities.Player;
import ca.jing.game.entities.PlayerMP;
import ca.jing.game.net.packets.Packet;
import ca.jing.game.net.packets.Packet.PacketTypes;
import ca.jing.game.net.packets.Packet00Login;

public class GameServer extends Thread{
	
	
	private DatagramSocket socket;
	private Game game;
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
	public GameServer(Game game){
		this.game=game;
		try {
			this.socket=new DatagramSocket(1331);
			
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
			
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//			String message=new String(packet.getData());
//			System.out.println("ClIENT[" + packet.getAddress().getHostAddress()+ ":" + packet.getPort() +"]>" + message);
//			if (message.trim().equalsIgnoreCase("ping")){
//				
//					System.out.println("Returing pang");
//					sendData("pang".getBytes(), packet.getAddress(),packet.getPort());
//			}
		}
			
	}
	
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type=Packet.lookupPacket(message.substring(0,2));	
		switch(type){
		case INVALID:
			break;
			
		case LOGIN:
			Packet00Login packet= new Packet00Login(data);
			System.out.println("[" + address.getHostAddress()+":"+ port+"]"+ packet.getUsername() + "has connected ...");
			PlayerMP player=null;
			if (address.getHostAddress().equalsIgnoreCase("127.0.0.1")){
				player=new PlayerMP(game.level, 100, 100, game.input, packet.getUsername(),address,port);
			}else{
				player=new PlayerMP(game.level, 100, 100, game.input, packet.getUsername(),address,port);
			}
			if (player!=null){
			this.connectedPlayers.add(player);
			game.level.addEntity(player);
			game.player=player;
			}
			break;
		}
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port){
		DatagramPacket packet= new DatagramPacket(data, data.length, ipAddress, port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (PlayerMP p : connectedPlayers){
			sendData(data, p.ipAddress, p.port);
		}
		
	}

}
