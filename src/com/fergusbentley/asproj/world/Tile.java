package com.fergusbentley.asproj.world;

import java.util.ArrayList;

public abstract class Tile {

	public int x;
	public int y;
	
	protected World world;
	
	public Tile(int x, int y, World w) {
		this.x = x;
		this.y = y;		
		this.world = w;
	}
	
	public abstract int colour(int x, int y);
	
	public abstract boolean isPassable();

	public ArrayList<Tile> getPassableNeighbours() {
		ArrayList<Tile> neighbours = new ArrayList<Tile>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (world.isPassable(x + i - 1, y + j - 1))
					neighbours.add(world.getTile(x + i - 1, y + j - 1));
			}
		}
		return neighbours;
	}
	
}
