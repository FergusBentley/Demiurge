package com.fergusbentley.asproj.gui;

import java.util.ArrayList;

import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.Main;
import com.fergusbentley.asproj.util.Colour;

import processing.core.PConstants;

public class GUIList extends GUIElement implements PConstants {

	private ArrayList<String> lines;
	
	public GUIList(Main a, int x, int y, int w, int h) {
		super(a, x, y, w, h);
		this.lines = new ArrayList<String>();
	}
	
	public GUIList(Main a, int x, int y, int w, int h, GUIStyle s) {
		super(a, x, y, w, h, s);
		this.lines = new ArrayList<String>();
	}

	@Override
	void show(GameState game) {
		if (this.visible) {
			
			// Default style
			app.fill(255);
			app.textSize(16);
			
			// If style is set, overide defaults
			if (this.style != null) {
				if (this.style.isSet(StyleAttribute.BORDER_WIDTH)) {
					Colour border = new Colour(0, 0, 0, 255);
					if (this.style.isSet(StyleAttribute.BORDER_COLOUR)){
						border = (Colour) this.style.get(StyleAttribute.BORDER_COLOUR);					
					}
					int weight = (int) this.style.get(StyleAttribute.BORDER_WIDTH);
					app.strokeWeight(weight);
					app.stroke(border.r, border.g, border.b, border.a);
				}

				if (this.style.isSet(StyleAttribute.FILL_COLOUR)) {
					Colour c = (Colour) this.style.get(StyleAttribute.FILL_COLOUR);
					app.fill(c.r, c.g, c.b, c.a);
				} else app.noFill();
				
				if (this.style.isSet(StyleAttribute.BORDER_WIDTH) || this.style.isSet(StyleAttribute.FILL_COLOUR))
					app.rect(sx, sy, sw, sh);
								
				if (this.style.isSet(StyleAttribute.FONT_COLOUR)) {
					Colour c = (Colour) this.style.get(StyleAttribute.FONT_COLOUR);
					app.fill(c.r, c.g, c.b, c.a);
				}
				
				if (this.style.isSet(StyleAttribute.FONT_SIZE)) {
					int s = (int) this.style.get(StyleAttribute.FONT_SIZE);
					app.textSize(s);
				}
			}
			
			app.textAlign(LEFT, CENTER); // Align text centrally
			
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				app.text(line, 3 + sx, 3 + sy + (parent.cellHeight / 2f) + (parent.cellHeight * i));
			}
		}
	}

	@Override
	void click() {}
	
	public void addLine(String s) {
		this.lines.add(s);
	}
	
	public void clear() {
		this.lines.clear();
	}

}
