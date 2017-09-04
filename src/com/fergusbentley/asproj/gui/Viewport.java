package com.fergusbentley.asproj.gui;

import java.util.ArrayList;
import java.util.List;

import com.fergusbentley.asproj.Config;
import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.util.Colour;
import com.fergusbentley.asproj.world.Tile;
import com.fergusbentley.asproj.world.World;

import processing.core.PApplet;

public class Viewport {

	private PApplet app;
	private int w, h;

	private int scale = Config.TILE_SIZE;
	private float zoom = 1;
	private float size; // apparent unit distance

	private float panX; // Offset of viewport from centre
	private float panY; //

	public int minX; // The range of tiles that are being displayed
	public int minY; // (minX, minY) -> (maxX, maxY)
	public int maxX;
	public int maxY;
	
	private int GW = Config.WORLD_SIZE;
	private int GH = Config.WORLD_SIZE;

	public Viewport(PApplet a) {	
		this.app = a;
		this.w = app.width;
		this.h = app.height;
		this.size = scale * zoom;
		updateDrawRegion();
	}

	// Draw an appropriately scaled and positioned square
	public void drawPixel(int gx, int gy, int c) {
		app.fill(c);
		app.noStroke();
		float sx = gridToScreenX(gx);
		float sy = gridToScreenY(gy);
		if (w - sx > 0 && h - sy > 0)
			app.rect(sx, sy, PApplet.constrain(size, 0, w - sx), PApplet.constrain(size, 0, w - sy));
	}
	
	// Allow transparency
	public void drawPixel(int gx, int gy, Colour c) {
		app.fill(c.r, c.g, c.b, c.a);
		app.noStroke();
		float sx = gridToScreenX(gx);
		float sy = gridToScreenY(gy);
		if (w - sx > 0 && h - sy > 0)
			app.rect(sx, sy, PApplet.constrain(size, 0, w - sx), PApplet.constrain(size, 0, w - sy));
	}
	
	// Draw a rectangle around some tiles
	public void highlight(int gx, int gy, int gw, int gh, int c, int w) {
		app.noFill();
		app.stroke(c);
		app.strokeWeight(w);
		float sx1 = gridToScreenX(gx);
		float sy1 = gridToScreenY(gy);
		float sx2 = gridToScreenX(gx + gw);
		float sy2 = gridToScreenY(gy + gh);
		app.rect(sx1, sy1, sx2 - sx1, sy2 - sy1);
	}

	public void highlight(int gx, int gy, int gw, int gh, int c) {
		this.highlight(gx, gy, gw, gh, c, 3);
	}
	
	// x-coordinate of a tile -> position on screen
	public float gridToScreenX(int gx) {
		return (float) (((w - size * GW) / 2) + (size * (gx + panX)));
	}

	// y-coordinate of a tile -> position on screen
	public float gridToScreenY(int gy) {
		return (float) (((h - size * GH) / 2) + (size * (gy + panY)));
	}

	// position on screen -> x-coordinate of nearest tile
	public int screenToGridX(float sx) {
		return (int) (((sx - ((w - (size * GW)) / 2)) / size) - panX);
	}

	// position on screen -> y-coordinate of nearest tile
	public int screenToGridY(float sy) {
		return (int) (((sy - ((h - (size * GH)) / 2)) / size) - panY);
	}

	// Update variables when camera moves to limit calculations per draw
	void updateDrawRegion() {
		minX = PApplet.constrain((int) Math.floor(screenToGridX(0)), 0, GW);
		minY = PApplet.constrain((int) Math.floor(screenToGridY(0)), 0, GH);
		maxX = PApplet.constrain((int) Math.ceil(screenToGridX(w)), 0, GW);
		maxY = PApplet.constrain((int) Math.ceil(screenToGridY(h)), 0, GH);
	}

	// Is a grid coordinate on screen? (i.e. should things here be drawn?)
	boolean onScreen(int x, int y) {
		return x >= minX && y >= minY && x <= maxX && y <= maxY;
	}

	// Is an entity even partially on screen?
	boolean onScreen(Entity e) {
		return e.x + e.w >= minX && e.y + e.h >= minY && e.x - e.w < maxX && e.y - e.h < maxY;
	}

	public List<Entity> onScreen(List<Entity> entities) {
		List<Entity> os = new ArrayList<Entity>();
		
		for (Entity e : entities) {
			if (onScreen(e)) os.add(e);
		}
		
		return os;
	}

	public void draw(GameState game, World world) {

		app.background(Colour.BLUE);
		
		this.zoom = game.getZoom();
		this.panX = game.getPan().x;
		this.panY = game.getPan().y;
		
		updateDrawRegion();
		
		// Draw tiles within viewport bounds
		for (int x = minX; x < maxX + 1; x++) {
			for (int y = minY; y < maxY + 1; y++) {
				Tile t = world.getTile(x, y);
				if (t != null) {
					int c = t.colour(x, y);
					drawPixel(x, y, c);
				}
			}
		}
		
		for (Entity e : onScreen(world.entities)) {
			if (e.shown) {
				int[] t = e.texture;
				int x = e.tx;
				int y = e.ty;
				for (int p = 0; p < t.length; p++) {
					int c = p % e.w;
					int r = p / e.w;
					int col = t[p];
					drawPixel(x + c, y + r, col);
				}
			}
		}
		
		if (Config.debug) {
			app.fill(Colour.colour(app.millis() % 255, (app.millis() % 127) * 2, (app.millis() % 85) * 3));
			app.noStroke();
			app.rect(0, 0, 8, 8);
		}
	}

}