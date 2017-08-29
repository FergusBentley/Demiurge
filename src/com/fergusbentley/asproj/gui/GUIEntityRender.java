package com.fergusbentley.asproj.gui;

import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.Main;
import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.util.Colour;
import com.fergusbentley.asproj.util.Util;

public class GUIEntityRender extends GUIElement {

	private Entity entity;
	
	public GUIEntityRender(Main a, int x, int y, int w, int h, Entity e) {
		super(a, x, y, w, h);
		this.setEntity(e);
	}

	public GUIEntityRender(Main a, int x, int y, int w, int h, Entity e, GUIStyle s) {
		super(a, x, y, w, h, s);
		this.setEntity(e);
	}

	public void show(GameState game) {

		if (this.visible) {

			if (this.style != null) {

				if (this.style.isSet(StyleAttribute.BORDER_WIDTH)) {
					Colour border = new Colour(0, 0, 0, 255);
					if (this.style.isSet(StyleAttribute.BORDER_COLOUR)){
						border = (Colour) this.style.get(StyleAttribute.BORDER_COLOUR);					
					}
					int weight = (int) this.style.get(StyleAttribute.BORDER_WIDTH);
					app.strokeWeight(weight);
					app.stroke(border.r, border.g, border.b, border.a);
				} else {
					app.noStroke();
				}

				if (this.style.isSet(StyleAttribute.FILL_COLOUR)) {
					Colour c = (Colour) this.style.get(StyleAttribute.FILL_COLOUR);
					app.fill(c.r, c.g, c.b, c.a);
				} else {
					app.noFill();
				}

				app.rect(sx, sy, sw, sh);

			}
			
			Entity e = this.getEntity();
			
			if (e != null) {
				
				int[] t = e.texture;
				for (int p = 0; p < t.length; p++) {
					int c = p % e.w;
					int r = p / e.w;
					int col = t[p];
					app.noStroke();
					app.fill(col);
					int s = 8;
					app.rect(sx + ((sw - e.w * s) / 2f) + (c * s), sy + ((sh - e.h * s) / 2f) + (r * s), s, s);
				}
				
				float sx = Util.gridToScreenX(game, e.tx);
				float sy = Util.gridToScreenY(game, e.ty);
				app.noFill();
				app.stroke(Colour.YELLOW);
				app.strokeWeight(1);
				app.rect(sx, sy, e.w * game.getScale(), e.h * game.getScale());
				
			} else {
				app.textAlign(3, 3);
				app.textSize(16);
				app.fill(80);
				app.stroke(255);
				app.text("No Entity", sx + (sw / 2f), sy + (sh / 2f) - 8);
				app.text("Selected.", sx + (sw / 2f), sy + (sh / 2f) + 8);
			}

		}

	}

	@Override
	void click() { return; }

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

}