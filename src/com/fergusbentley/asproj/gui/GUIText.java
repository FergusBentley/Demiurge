package com.fergusbentley.asproj.gui;

import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.Main;
import com.fergusbentley.asproj.util.Colour;

public class GUIText extends GUIElement {

	public String text; // The text to be written

	public GUIText(Main a, String t, int x, int y, int w, int h) {
		this(a, t, x, y, w, h, null);
	}

	public GUIText(Main a, String t, int x, int y, int w, int h, GUIStyle s) {
		super(a, x, y, w, h, s);
		this.text = t;
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
			
			app.textAlign(3, 3); // Align text centrally
			app.text(text, sx + (sw / 2f), sy + (sh / 2f));

		}
	}

	@Override
	void click() { return; } // PASS

}
