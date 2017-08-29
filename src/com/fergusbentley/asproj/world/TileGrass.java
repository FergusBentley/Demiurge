package com.fergusbentley.asproj.world;

import com.fergusbentley.asproj.util.Colour;
import com.fergusbentley.asproj.util.Util;

public class TileGrass extends Tile {
	
	private int colour;
	
	public TileGrass(int x, int y, World w) {
		super(x, y, w);
		this.colour = generateColour(x, y);
	}

	@Override
	public int colour(int x, int y) {
		return colour;
	}

	@Override
	public boolean isPassable() {
		return true;
	}
	
	private int generateColour(int x, int y) {
		
		float rr = Util.cubic((float) Math.random(), 1.5f);
		float rg = Util.cubic((float) Math.random(), 1.5f);
		float rb = Util.cubic((float) Math.random(), 1.5f);
		
		
		int r = (int) Util.lerp(25, 50, 0.5f + (rr / 2f));
		int g = (int) Util.lerp(80, 104, 0.5f + (rg / 2f));
		int b = (int) Util.lerp(42, 45, 0.5f + (rb / 2f));
		
		return Colour.colour(r, g, b);
	}

}
