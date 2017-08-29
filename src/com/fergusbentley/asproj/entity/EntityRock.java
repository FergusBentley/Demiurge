package com.fergusbentley.asproj.entity;

import com.fergusbentley.asproj.Resources;
import com.fergusbentley.asproj.entity.structure.Material;
import com.fergusbentley.asproj.entity.structure.MaterialStack;
import com.fergusbentley.asproj.world.World;

import processing.core.PConstants;

public class EntityRock extends Entity implements PConstants, Harvestable {

	int type = 0;
	
	public EntityRock(World w, int x, int y) {
		super(w, x, y, Resources.getRandom("sprite_rock"), 3, 2);
	}
	
	//public EntityRock(SaveData sd) {
	//	super()
	//}

	@Override
	public void tick() {}

	@Override
	public MaterialStack harvest() {
		return new MaterialStack(Material.ROCK, (int)(Math.random() * 15 + 5));
	}

}
