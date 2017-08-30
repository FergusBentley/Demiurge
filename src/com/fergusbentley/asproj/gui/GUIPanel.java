package com.fergusbentley.asproj.gui;

import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.Main;
import com.fergusbentley.asproj.util.Colour;

public class GUIPanel extends GUIElement {

	public GUIPanel(Main a,int x, int y, int w, int h) {
		super(a, x, y, w, h);
	}

	public GUIPanel(Main a, int x, int y, int w, int h, GUIStyle s) {
		super(a, x, y, w, h, s);
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

			} else {
				app.fill(Colour.REGAL_BLUE);
				app.stroke(Colour.grey(80));
				app.strokeWeight(2);
				app.rect(sx, sy, sw, sh);
			}

		}

	}

	@Override
	void click() { return; }

}