package com.fergusbentley.asproj.entity.structure;

import com.fergusbentley.asproj.entity.EntityRock;
import com.fergusbentley.asproj.entity.EntityTree;
import com.fergusbentley.asproj.entity.Harvestable;
import com.fergusbentley.asproj.util.Colour;

public enum Material {

	WOOD(Colour.colour(130, 90, 70), EntityTree.class),
	ROCK(Colour.grey(150), EntityRock.class);
	
	public int colour;
	public Class<? extends Harvestable> resource;
	
	private Material (int col, Class<? extends Harvestable> res) {
		this.colour = col;
		this.resource = res;
	}
	
}
