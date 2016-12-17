package ca.jing.game.entities;

import ca.jing.game.InputHandler;
import ca.jing.game.gfx.Colours;
import ca.jing.game.gfx.Font;
import ca.jing.game.gfx.Screen;
import ca.jing.game.level.Level;

public class Player extends Mob{
	private InputHandler input;
	private int colour=Colours.get(-1, 111, 145, 543);
	private int scale=1;
	protected boolean isSwimming = false;
	
	private int tickCount=0;
	
	private String username;
	
	public Player(Level level, int x, int y, InputHandler input, String username) {
		super(level, "player", x, y, 1);
		this.input=input;
		this.username=username;
	}


	@Override
	public void tick() {
		int xa=0;
		int ya=0;
		
		
		if (input.up.isPressed()) ya-=1;
		
		if (input.down.isPressed())	ya+=1;
	
		if (input.left.isPressed()){
			xa-=1;
		}
		if (input.right.isPressed()){
			xa+=1;
		}
		
		if(xa!=0||ya!=0){
			move(xa,ya);
			isMoving = true;
			
		}else{
			isMoving =false;
		}
		
		if (level.getTile(this.x>>3, this.y>>3).getId()==3){
			isSwimming=true;
		}
		if (isSwimming && level.getTile(this.x >>3, this.y>>3).getId() !=3){
			isSwimming =false;
		}
		tickCount++;
		this.scale=1;
		
	}

	@Override
	public void render(Screen screen) {
		int xTile=0;
		int yTile=28;
		int walkingSpeed=3;
		int flipTop=(numSteps >> walkingSpeed)&1;
		int flipBottom=(numSteps >> walkingSpeed)&1;
		
		if (movingDir ==1){
			xTile +=2;
			
		}else if(movingDir>1){
			xTile +=4 + ((numSteps >> walkingSpeed)&1)*2;
			flipTop= (movingDir-1)%2;
			
		}
		
		int modifier=8*scale;
		int xOffset=x-modifier/2;
		int yOffset=y-modifier/2-4;
		
		if (isSwimming){
			int waterColour=0;
			yOffset+=4;
			
			if (tickCount % 60 <15){
				waterColour = Colours.get(-1, -1, 255, -1);
				
			}else if (15 <= tickCount%60 && tickCount %60 < 30){
				waterColour = Colours.get(-1, 255, 115, -1);
			}else if (30 <= tickCount%60 && tickCount %60 < 45){
				waterColour = Colours.get(-1, 115, -1, 225);
			}else{
				waterColour = Colours.get(-1, 225, 115, -1);
				
		   }
			
			screen.render(xOffset, yOffset+3, 0+27*32, waterColour, 0x00,1);
			screen.render(xOffset + 8, yOffset+3, 0 +27*32, waterColour, 0x01,1);
		}
		
		screen.render(xOffset+(modifier*flipTop), yOffset, xTile+yTile*32,colour, flipTop, scale);
		screen.render(xOffset+modifier-(modifier*flipTop), yOffset, (xTile+1)+yTile*32,colour, flipTop, scale);
		if(!isSwimming){
		
		screen.render(xOffset+(modifier*flipBottom), yOffset+modifier, xTile+(yTile+1)*32,colour, flipBottom, scale);
		screen.render(xOffset+modifier-(modifier*flipBottom), yOffset+modifier, (xTile+1)+(yTile+1)*32,colour, flipBottom,scale);
		}
		if(username !=null){
			Font.render(username, screen, xOffset-((username.length()-1)/2*8)+4, yOffset-10,Colours.get(-1, -1, -1, 555) ,1);
		}
	}
	
	public boolean hasCollided(int xa, int ya){
		int xMin=0;
		int xMax=7;
		int yMin =3;
		int yMax=7;
		for (int x=xMin; x<xMax; x++){
			if(isSolidTile(xa, ya,x, yMin)){
				return true;
			}
		}
		for (int x=xMin; x<xMax; x++){
			if(isSolidTile(xa,ya,x,yMax)){
				return true;
			}
			
		}
		for (int y=yMin; y<yMax;y++){
			if(isSolidTile(xa, ya, xMin, y)){
				return true;
			}
		}
		for (int y=yMin; y<yMax; y++){
			if(isSolidTile(xa, ya, xMax, y)){
				return true;
			}
		}
		return false;
	}

}