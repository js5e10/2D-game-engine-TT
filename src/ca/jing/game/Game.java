package ca.jing.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;

import java.awt.image.DataBufferInt;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ca.jing.game.entities.Player;
import ca.jing.game.entities.PlayerMP;
import ca.jing.game.gfx.Colours;
import ca.jing.game.gfx.Font;
import ca.jing.game.gfx.Screen;
import ca.jing.game.gfx.SpriteSheet;
import ca.jing.game.level.Level;
import ca.jing.game.net.GameClient;
import ca.jing.game.net.GameServer;
import ca.jing.game.net.packets.Packet00Login;

public class Game extends Canvas implements Runnable{
	public static final int WIDTH=160;
	public static final int HEIGHT=WIDTH/12*9;
	public static final int SCALE=3;
	
	public static final String NAME="Game";
	
	private JFrame frame;
	public boolean running=false;
	public int tickCount=0;
	
	private BufferedImage image=new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels=((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	/*private SpriteSheet spriteSheet=new SpriteSheet("/spreadsheet.png");*/
	private int[]colours=new int[6*6*6];
	
	
	private Screen screen;
	
	public InputHandler input;
	public Level level;
	public Player player;
	
	private GameClient socketClient;
	private GameServer socketServer;
	
	
	
	public Game(){
		setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		
		frame=new JFrame(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	public void init(){
		int index = 0;
		for(int r=0;r<6;r++){
			for(int g=0;g<6;g++){
				for(int b=0;b<6;b++){
					int rr=(r*255/5);
					int gg=(g*255/5);
					int bb=(b*255/5);
					
					colours[index++]=rr<<16|gg<<8|bb;
					
				}
			}
		}
		
		Colours.get(555,505,055,555);
		screen=new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input=new InputHandler(this);
		level=new Level("/levels/water_test_level.png");
		player=new PlayerMP(level, 100 ,100, input, JOptionPane.showInputDialog(this, "please enter a username"), null, -1);
		level.addEntity(player);
		Packet00Login loginPacket = new Packet00Login(player.getUsername());
	
		if (socketServer!=null){
			socketServer.addConnection((PlayerMP)player, loginPacket);
		}
		
		//socketClient.sendData("ping".getBytes());
		//Packet00Login loginPacket = new Packet00Login(JOptionPane.showInputDialog(this, "please enter a username"));
		loginPacket.writeData(socketClient);
	}
	public synchronized void start() {
		// TODO Auto-generated method stub
		running=true;
		new Thread(this).start();
		
		if(JOptionPane.showConfirmDialog(this, "Do you want to run the server")==0){
			socketServer = new GameServer(this);
			socketServer.start();
			
		}
		socketClient=new GameClient(this, "localhost");
		socketClient.start();
	}
	public synchronized void stop(){
		running=false;
	}
	public void run(){
		
		long lastTime=System.nanoTime();
		double nsPerTick=1000000000D/60D;
		
		int frames=0;
		int ticks=0;
		
		long lastTimer=System.currentTimeMillis();
		double delta=0;
		init();
		while (running){
			long now=System.nanoTime();
			delta+=(now -lastTime)/nsPerTick;
		    lastTime=now;
		    boolean shouldRender=true;
		    while(delta>=1){
		    	ticks++;
		    	tick();
		    	delta-=1;
		        shouldRender=true;
		    }
		    try{Thread.sleep(2);}catch(Exception e){
		    	e.printStackTrace();
		    	
		    }
           if(shouldRender==true){
        	   frames++;
        	   render();
           }
		    
	       if(System.currentTimeMillis()-lastTimer>=1000){
	    	   lastTimer+=1000;
	    	   /*System.out.println(ticks+ "," + frames);*/
	    	   frame.setTitle(ticks+ " ticks, " + frames + "frames");
	    	   frames=0;
	    	   ticks=0;
	    
	       }
	       
	    
		}
	}
	//private int x=0, y=0;
	
	public void tick(){
		tickCount++;
		
		//if (input.up.isPressed())y-=1;
		
		//if (input.down.isPressed())	y+=1;
	
		//if (input.left.isPressed()){
		//	x-=1;
		//}
		//if (input.right.isPressed()){
		//	x+=1;
		//}
		
		//for (int i =0 ; i<pixels.length; i++){
		//	pixels[i]=i+tickCount;
		//}
		level.tick();
	}
	public void render(){
		BufferStrategy bs=getBufferStrategy();
		if(bs==null){
			createBufferStrategy(3);
			return;
		}
		
		//int xOffset=screen.xOffset;
		//int yOffset=screen.yOffset;
		
		int xOffset=player.x-(screen.width/2);
		int yOffset=player.y-(screen.height/2);
		
		level.renderTiles(screen, xOffset, yOffset);
		//screen.render(pixels,0, WIDTH);
		//for(int y=0; y<32;y++){
		//	for(int x=0;x<32;x++){
			//	boolean flipX=x%2==1;
			//	boolean flipY=y%2==1;
				
			//	screen.render(x<<3, y<<3, 0, Colours.get(555, 505, 055, 550),flipX, flipY );
				
		//	}
	//	}
	//	String msg="This is our game!";
		
		//Font.render(msg, screen, screen.xOffset+screen.width/2-(msg.length()*8/2), screen.yOffset+screen.height/2, Colours.get(-1, -1, -1, 000));
		for (int x = 0; x < level.width; x++) {
            int colour = Colours.get(-1, -1, -1, 000);
            if (x % 10 == 0 && x != 0) {
                    colour = Colours.get(-1, -1, -1, 500);
            }
            Font.render((x % 10) + "", screen, 0 + (x * 8), 0, colour, 1);
    }
		
		
		level.renderEntities(screen);
		
		
		for(int y=0; y<screen.height;y++){
			for(int x=0;x<screen.width;x++){
				int colourCode=screen.pixels[x+y*screen.width];
				if(colourCode<255)pixels[x+y*WIDTH]=colours[colourCode];
				}
			} 
		
		
		Graphics g=bs.getDrawGraphics();
		
		g.drawImage(image,0,0,getWidth(),getHeight(),null);
		g.dispose();/* free memory*/
		bs.show();
	
		
	}
	
	
	
	public static void main(String[] args){
		new Game().start();
		
		
	}


}