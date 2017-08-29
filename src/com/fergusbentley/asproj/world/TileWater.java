package com.fergusbentley.asproj.world;

import com.fergusbentley.asproj.util.Colour;

public class TileWater extends Tile {

	public TileWater(int x, int y, World w) {
		super(x, y, w);
	}

	@Override
	public int colour(int x, int y) {
		return Colour.BLUE;
	}

	@Override
	public boolean isPassable() {
		return false;
	}

}
