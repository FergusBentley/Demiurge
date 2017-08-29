package com.fergusbentley.asproj.entity;

import com.fergusbentley.asproj.util.Colour;
import com.fergusbentley.asproj.world.World;

public class EntityMarker extends Entity {
	
	public static int[] sprite = {Colour.MAGENTA};
	
	public EntityMarker(World w, int x, int y) {
		super(w, x, y, EntityMarker.sprite, 1, 1, 3, 3);
	}

	@Override
	public void tick() {}
}
