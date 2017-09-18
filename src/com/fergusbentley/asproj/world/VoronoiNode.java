package com.fergusbentley.asproj.world;

public class VoronoiNode {

	public int x, y;
	public Class<? extends Tile> type;
	
	public VoronoiNode(int x, int y, Class<? extends Tile> t) {
		this.x = x;
		this.y = y;
		this.type = t;
	}
	
}
