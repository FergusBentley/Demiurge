package com.fergusbentley.asproj.gui;

import java.util.concurrent.Callable;

import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.Main;
import com.fergusbentley.asproj.util.Colour;

import processing.core.PImage;

public class GUIButton extends GUIElement {

	private PImage image; // Icon for button
	private String text; // Text to write inside button

	private Callable<Boolean> callback; // Called when the button is clicked
	
	// Icon button
	public GUIButton(Main a, int x, int y, int w, int h, PImage i) {
		super(a, x, y, w, h);
		this.image = i;
	}
	
	// Text button
	public GUIButton(Main a, int x, int y, int w, int h, String t) {
		super(a, x, y, w, h);
		this.text = t;
	}
	
	// Styled variants...
	public GUIButton(Main a, int x, int y, int w, int h, PImage i, GUIStyle s) {
		super(a, x, y, w, h, s);
		this.image = i;
	}
	
	public GUIButton(Main a, int x, int y, int w, int h, String t, GUIStyle s) {
		super(a, x, y, w, h, s);
		this.text = t;
	}

	@Override
	void show(GameState game) {
		
		if (this.visible) {
			
			if (this.style != null) {
				
				// Button background...
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
				
				// Draw image if applicable
				if (this.image != null) {
					app.image(this.image, sx, sy, sw, sh);
				}
				// Else write & style text
				else if (this.text != null){
					
					float am = 1;
					if (this.hovered()) {
						am = 3;
					}
					
					if (this.style.isSet(StyleAttribute.FONT_COLOUR)) {
						Colour c = (Colour) this.style.get(StyleAttribute.FONT_COLOUR);
						app.fill(c.r, c.g, c.b, c.a * am);
					} else {
						app.fill(255, 100 * am);
					}
					
					if (this.style.isSet(StyleAttribute.FONT_SIZE)) {
						app.textSize((int) this.style.get(StyleAttribute.FONT_SIZE));
					} else {
						app.textSize(16);
					}
					
					app.textAlign(3, 3);
					app.text(this.text, sx + (sw / 2f), sy + (sh / 2f));
				}
				
			} else {
				// Default button style...
				app.fill(Colour.REGAL_BLUE);
				app.stroke(Colour.grey(80));
				app.strokeWeight(2);
				app.rect(sx, sy, sw, sh);
				
				if (this.hovered()) {
					app.fill(255, 150);
					app.stroke(Colour.YELLOW);
					app.strokeWeight(2);
					app.rect(sx, sy, sw, sh);
				}
				
				if (this.image != null) {
					app.image(this.image, sx, sy, sw, sh);
				}
			}
			
		}

	}

	// Assign an anonymous function to the click event
	public GUIButton assign(Callable<Boolean> bc) {
		this.callback = bc;
		return this;
	}
	
	@Override
	public void click() {
		// Execute the assigned function, if exists.
		if (this.callback != null) {
			try {
				this.callback.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
