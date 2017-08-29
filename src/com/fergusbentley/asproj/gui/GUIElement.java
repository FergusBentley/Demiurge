package com.fergusbentley.asproj.gui;

import java.util.Map.Entry;

import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.Main;

public abstract class GUIElement implements Comparable<GUIElement> {

	Main app;

	GUIGrid parent; // Grid containing this element

	public float x, y; // Cell of parent that aligns with my top-left
	public float w, h; // How many of my parent's cells wide and tall I am

	public float sx, sy; // True coordinates, relative to the screen
	public float sw, sh; // True dimensions, relative to the screen
	
	public int z = 0; // Used to sort elements for layering
	
	public GUIStyle style; // Define visual aspects of the Element

	public boolean visible; // Should I be drawn?

	public GUIElement(Main a, int x, int y, int w, int h) {
		this(a, x, y, w, h, null);
	}

	public GUIElement(Main a, int x, int y, int w, int h, GUIStyle s) {
		this.app = a;
		this.parent = null;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.style = s;
		this.visible = true;
		
		this.sx = x;
		this.sy = y;
		this.sw = w;
		this.sh = h;
	}
	
	public void setParent(GUIGrid p) {
		this.parent = p;
		if (parent != null) {
			updateTrueDimensions();
		}
	}
	
	protected void updateTrueDimensions() {
		
		this.sx = parent.sx + (x * parent.cellWidth);
		this.sy = parent.sy + (y * parent.cellHeight);
		this.sw = parent.cellWidth * w;
		this.sh = parent.cellHeight * h;
		
		if (this instanceof GUIGrid) {
			
			GUIGrid me = (GUIGrid) this;
			
			me.cellWidth = (float)me.sw / (float)me.cols;
			me.cellHeight = (float)me.sh / (float)me.rows;
			
			for (Entry<String, GUIElement> ent : me.elements.entrySet()) {
				GUIElement ele = ent.getValue();
				ele.updateTrueDimensions();
			}
		}
	}
	
	public GUIElement getChild(String id) {
		try {
			return ((GUIGrid)this).getChild(id);
		} catch (Exception e) {
			System.out.println("Cannot get child of non-container element.");
			return null;
		}
	}
	
	public GUIGrid asGrid() {
		if (this instanceof GUIGrid) return (GUIGrid) this;
		else return null;
	}
	
	public GUIText asText() {
		if (this instanceof GUIText) return (GUIText) this;
		else return null;
	}
	
	public GUIButton asButton() {
		if (this instanceof GUIButton) return (GUIButton) this;
		else return null;
	}
	
	public GUIEntityRender asEntityRender() {
		if (this instanceof GUIEntityRender) return (GUIEntityRender) this;
		else return null;
	}
	
	public GUIList asList() {
		if (this instanceof GUIList) return (GUIList) this;
		else return null;
	}
	
	public GUIElement hidden() {
		this.visible = false;
		return this;
	}
	
	public GUIElement visible() {
		this.visible = true;
		return this;
	}
	
	public int compareTo(GUIElement e) {
		return (int) Math.signum(this.z - e.z);
	}
	
	public boolean hovered() {
		return 		app.mouseX > this.sx 
				&& 	app.mouseY > this.sy 
				&& 	app.mouseX < this.sx + this.sw 
				&& 	app.mouseY < this.sy + this.sh;
	}

	abstract void show(GameState game);
	
	abstract void click();

}