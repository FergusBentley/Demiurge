package com.fergusbentley.asproj.entity;

import com.fergusbentley.asproj.Config;
import com.fergusbentley.asproj.Resources;
import com.fergusbentley.asproj.entity.structure.Material;
import com.fergusbentley.asproj.entity.structure.MaterialStack;
import com.fergusbentley.asproj.util.Util;
import com.fergusbentley.asproj.world.World;

import processing.core.PConstants;
import processing.core.PImage;

public class EntityTree extends Entity implements PConstants, Harvestable {

	private int age;
	private int growthCooldown;
	
	public EntityTree(World w, int x, int y, int a) {
		super(w, x, y, Resources.get("sprite_tree/tree0" + a), 1, 1);
		this.age = a;
		this.growthCooldown = (int) (Math.random() * 3000) + 1000;
	}

	@Override
	public void tick() {
		if (growthCooldown < 1) grow();
		growthCooldown--;
	}
	
	private void grow() {
		if (age < 3) {
			this.age++;
			updateSprite();
		}
		else if (this.world.entities.size() < Config.ENTITY_LIMIT) {
			spawnChild();
		}
		this.growthCooldown = (int) (Math.random() * 3000) + 1000;
	}
	
	private void updateSprite() {
		PImage ns = Resources.get("sprite_tree/tree0" + this.age);
		this.w = ns.pixelWidth;
		this.h = ns.pixelHeight;
		this.texture = ns.pixels;
		updateCorners();
	}
	
	private void spawnChild() {
		
		int nx = this.x + Util.randRangeSigned(2, 4);
		int ny = this.y + Util.randRangeSigned(2, 4);
		
		if (this.world.isPassable(nx - 1, ny - 1, 3, 3)) {
			this.world.spawnList.add(new EntityTree(this.world, nx, ny, 1));
		}
		
	}

	@Override
	public MaterialStack harvest() {
		this.age = 1;
		return new MaterialStack(Material.WOOD, this.age - 1 * 10);
	}
}
