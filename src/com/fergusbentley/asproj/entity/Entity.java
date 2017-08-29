package com.fergusbentley.asproj.entity;

import com.fergusbentley.asproj.util.Colour;
import com.fergusbentley.asproj.world.World;

import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public abstract class Entity implements PConstants, Comparable<Entity> {
	
	public int x, y;
	public int w, h;

	public int colliderWidth, colliderHeight;
	public int cx, cy;
	
	public int tx, ty;
	
	public int[] texture;
	
	public World world;

	public boolean shown = true;
	
	public Entity(World w, int x, int y, PImage t) {
		this(w, x, y, t.pixels, t.pixelWidth, t.pixelHeight, 1, 1);
	}
	
	public Entity(World w, int x, int y, int[] t, int pw, int ph) {
		this(w, x, y, t, pw, ph, 1, 1);
	}
	
	public Entity(World w, int x, int y, PImage t, int cW, int cH) {
		this(w, x, y, t.pixels, t.pixelWidth, t.pixelHeight, cW, cH);
	}
	
	public Entity(World w, int x, int y, int[] t, int pw, int ph, int cW, int cH) {
		this.x = x;
		this.y = y;
		this.texture = t;
		this.w = pw;
		this.h = ph;
		this.colliderWidth = cW;
		this.colliderHeight = cH;
		this.world = w;
		
		updateCorners();
		
	}
	
	public abstract void tick();

	public void updateCorners() {
		this.tx = this.x - (int) Math.floor(this.w / 2f);
		this.cx = this.x - (int) Math.floor(this.colliderWidth / 2f);
		
		this.ty = (this.y - this.h) + 1;
		this.cy = (this.y - this.colliderHeight) + 1;
	}
	
	public PVector pos() {
		return new PVector(x, y);
	}

	public boolean colliding(int x1, int y1) {
		return cx <= x1 && cy <= y1 && cx + colliderWidth > x1 && cy + colliderHeight > y1; 
	}
	
	public boolean colliding(Entity ent) {
		// TODO Auto-generated method stub.
		return false;
	}
	
	public int compareTo(Entity e) {
		return (int) Math.signum(this.y - e.y);
	}

	public boolean hovered(int gx, int gy) {
		if (tx <= gx && ty <= gy && tx + w > gx && ty + h > gy) {
			int dx = gx - tx;
			int dy = gy - ty;
			int p = (dy * w) + dx;
			int col = texture[p];
			return Colour.alpha(col) == 255;
		}
		return false;
	}
	
}
