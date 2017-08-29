package com.fergusbentley.asproj.world;

import com.fergusbentley.asproj.util.Colour;

public class TileDebug extends Tile {

	public TileDebug(int x, int y, World w) {
		super(x, y, w);
	}

	@Override
	public int colour(int x, int y) {
		return Colour.MAGENTA;
	}

	@Override
	public boolean isPassable() {
		return true;
	}

}
