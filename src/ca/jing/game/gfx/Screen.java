package ca.jing.game.gfx;

public class Screen {

	 public static final int MAP_WIDTH=64;
	 public static final int MAP_WIDTH_MASK=MAP_WIDTH-1;
	 
	 public static final byte BIT_MIRROR_X=0x01;
	 public static final byte BIT_MIRROR_Y=0x02;
	 
	 //public int[]tiles = new int[MAP_WIDTH*MAP_WIDTH];
	 //public int[]colours = new int[MAP_WIDTH*MAP_WIDTH*4];
	 public int[]pixels;
	 public int xOffset=0;
	 public int yOffset=0;
	 
	 public int width;
	 public int height;
	 
	 public SpriteSheet sheet;

	 public Screen(int width, int height, SpriteSheet sheet){
		 this.width=width;
		 this.height=height;
		 this.sheet=sheet;
		 pixels=new int[width*height];
		 //for(int i=0; i<MAP_WIDTH*MAP_WIDTH; i++){
			 
			// colours[i*4+0]=0xff00ff;
			 //colours[i*4+1]=0x00ffff;
			 //colours[i*4+2]=0xffff00;
			 //colours[i*4+3]=0xffffff;
		// }
	 }
	  
	 
	// public void render(int[]pixels, int offset, int row){
	//	 for(int yTile=yOffset>>3; yTile<=(yOffset + height)>>3;yTile++){
	//	   int yMin=yTile*8-yOffset;
//		   int yMax=yMin+8;
	//	   if(yMin<0)yMin=0;
	//	   if(yMax>height)yMax=height;
	//	   
	//	   for(int xTile=xOffset>>3; xTile<=(xOffset + width)>>3;xTile++){
	//		   int xMin=xTile*8-xOffset;
	//		   int xMax=xMin+8;
	//		   if(xMin<0)xMin=0;
	//		   if(xMax>width)xMax=width;
			   
			   
	//		   int tileIndex=(xTile & (MAP_WIDTH_MASK))+(yTile&(MAP_WIDTH_MASK))*MAP_WIDTH;
	//		   for(int y=yMin; y<yMax; y++){
	//			   int sheetPixel=((y+yOffset)&7)*sheet.width + ((xMin + xOffset)&7);
	//			   int tilePixel=offset+xMin+y*row;
	//			   for(int x=xMin; x<xMax;x++){
	//				   int colour=tileIndex*4+sheet.pixels[sheetPixel++];
	//				   pixels[tilePixel++]=colours[colour];
	//			   }
				   
	//		   }
	//	  }
	//	 }
		 
	 //}
	 
	 public void render(int xPos, int yPos, int tile, int colour){
		 render(xPos, yPos, tile, colour, 0x00);
		 
	 }
	 
	 public void render(int xPos, int yPos, int tile, int colour, int mirrorDir){
		 xPos-=xOffset;
		 yPos-=yOffset;
		 
		 boolean mirrorX =(mirrorDir & BIT_MIRROR_X)>0;
		 boolean mirrorY =(mirrorDir & BIT_MIRROR_Y)>0;
		 
		 int xTile=tile%32;
		 int yTile=tile/32;
		 int tileOffset=(xTile<<3)+(yTile<<3)*sheet.width;
		 for (int y=0;y<8;y++){
			 
			// int ySheet=y;
			// if(mirrorY) ySheet=7-y;
			// if(y+yPos<0||y+yPos>=height) continue;
			// for(int x=0; x<8; x++){
			//	 int xSheet=x;
			//	 if (mirrorX) xSheet=7-x;
			//	 if(x+xPos<0||x+xPos>=width) continue;
				 
			//	 int col=(colour>>(sheet.pixels[xSheet+ySheet*sheet.width+tileOffset]*8))&255;
			//	 if(col<255){
			//		 pixels[(x+xPos)+(y+yPos)*width]=col;
			//	 }
			// }
			 if (y + yPos < 0 || y + yPos >= height) {
                 continue;
			 				}
			 			int ySheet = y;
			 			if (mirrorY) {
			 			ySheet = 7 - y;
			 			}
			 			for (int x = 0; x < 8; x++) {
			 				if (x + xPos < 0 || x + xPos >= width) {
                         continue;
                 }
                 int xSheet = x;
                 if (mirrorX) {
                         xSheet = 7 - x;
                 }
                 int col = (colour >> (sheet.pixels[xSheet + ySheet
                                 * sheet.width + tileOffset] * 8)) & 255;
                 if (col < 255) {
                         pixels[(x + xPos) + (y + yPos) * width] = col;
                 }
          }
			 
		 }
		 
		 
	 }
	    public void setOffset(int xOffset, int yOffset) {
	        this.xOffset = xOffset;
	        this.yOffset = yOffset;
	    }
	 
}
