package com.fergusbentley.asproj.entity.living;

import com.fergusbentley.asproj.ai.Behaviour;
import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.world.World;

import processing.core.PImage;

public abstract class Actor extends Entity {
	
	public Behaviour behaviour;
	
	public Actor(World w, int x, int y, PImage t) {
		super(w, x, y, t.pixels, t.pixelWidth, t.pixelHeight, 1, 1);
	}
	
	public Actor(World w, int x, int y, int[] t, int pw, int ph) {
		super(w, x, y, t, pw, ph, 1, 1);
	}
	
	public Actor(World w, int x, int y, PImage t, int cW, int cH) {
		super(w, x, y, t.pixels, t.pixelWidth, t.pixelHeight, cW, cH);
	}
	
	public Actor(World w, int x, int y, int[] t, int pw, int ph, int cW, int cH) {
		super(w, x, y, t, pw, ph, cW, cH);
	}

	@Override
	public void tick() {
		behaviour.tick();
	}
	
	public void move(int dx, int dy) {
		if (this.world.isPassable(this.cx + dx, this.cy + dy, this.colliderWidth, this.colliderHeight, this)) {
			this.x += dx;
			this.y += dy;
			updateCorners();
		}
	}

	public boolean moveTo(int tx, int ty) {
		if (this.world.isPassable(tx, ty, this.colliderWidth, this.colliderHeight, this)) {
			this.x = tx;
			this.y = ty;
			updateCorners();
			return true;
		}
		return false;
	}

}
