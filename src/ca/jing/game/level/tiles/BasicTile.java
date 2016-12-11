package ca.jing.game.level.tiles;

import ca.jing.game.gfx.Screen;
import ca.jing.game.level.Level;

public class BasicTile extends Tile{
	protected int tileId;
	protected int tileColour;

	public BasicTile(int id, int x, int y, int tileColour, int levelColour) {
		super(id, false, false, levelColour);
		this.tileId=x+y;
		this.tileColour=tileColour;
	}

	@Override
	public void render(Screen screen, Level level, int x, int y) {
		screen.render(x,y,tileId,tileColour,0x00,1);
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}
	

}
