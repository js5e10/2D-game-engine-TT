package ca.jing.game.level.tiles;

import ca.jing.game.gfx.Colours;
import ca.jing.game.gfx.Screen;
import ca.jing.game.level.Level;

public abstract class Tile {

	public static final Tile[] tiles = new Tile[256];
	public static final Tile VOID = new BasicTile(0,0,0, Colours.get(000, -1, -1, -1));
	public static final Tile STONE = new BasicTile(1,1,0, Colours.get(-1, 333,-1,1));
	public static final Tile GRASS = new BasicTile(2,2,0, Colours.get(-1,131,141,-1));
	
	protected byte id;
	protected boolean solid;
	protected boolean emitter;
	
	public Tile(int id, boolean isSolid, boolean isEmitter){
		this.id=(byte) id;
		if (tiles[id]!=null) throw new RuntimeException("Duplicated id on" + id);
		this.solid=isSolid;
		tiles[id]=this;
	}
	
	public byte getId(){
		return id;
		
	}
	
	public boolean isSolid(){
		return solid;
		
	}
	public boolean isEmitter(){
		
		return emitter;
	}
	
	public abstract void render(Screen screen, Level level, int x, int y);

}
