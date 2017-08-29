package com.fergusbentley.asproj.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.Main;
import com.fergusbentley.asproj.util.Colour;

public class GUIGrid extends GUIElement {

	public Map<String, GUIElement> elements = new HashMap<String, GUIElement>();

	// Dimensions of each cell within grid
	public float cellWidth, cellHeight;
	// Number of cells in grid
	public int rows, cols;

	// Sub grid, has x/y, w/h values to position in a parent grid
	public GUIGrid(Main a, int x, int y, int w, int h, int c, int r, GUIStyle s) {
		super(a, x, y, w, h, s);
		this.rows = r;
		this.cols = c;
		
		this.cellWidth = w / (float) c;  // Calculate cell size
		this.cellHeight = h / (float) r;
		
	}
	
	// Allow creation of grids with default style
	public GUIGrid(Main a, int x, int y, int w, int h, int c, int r) {
		this(a, x, y, w, h, c, r, null);
	}
	
	// Main grid, has no parent grid
	public GUIGrid(Main a, int c, int r) {
		this(a, 0, 0, a.width, a.height, c, r);
	}
	
	public GUIGrid(Main a, int c, int r, GUIStyle s) {
		this(a, 0, 0, a.width, a.height, c, r, s);
	}
	
	// Allow manual setting of z-index
	public GUIGrid(Main a, int x, int y, int w, int h, int c, int r, int z, GUIStyle s) {
		this(a, x, y, w, h, c, r, s);
		this.z = z;
	}

	// Render the grid and child elements
	public void show(GameState game) {

		if (this.visible) {
			
			// Draw background, grid lines, etc. if specified by the stylesheet
			// Default to none of this, just draw children
			if (this.style != null) {

				if (this.style.isSet(StyleAttribute.FILL_COLOUR)) {
					Colour c = (Colour) this.style.get(StyleAttribute.FILL_COLOUR);
					app.fill(c.r, c.g, c.b, c.a);
					app.noStroke();
					if (this.style.isSet(StyleAttribute.BORDER_COLOUR)) {
						Colour bc = (Colour) this.style.get(StyleAttribute.BORDER_COLOUR);
						app.stroke(bc.r, bc.g, bc.b, bc.a);
						app.strokeWeight(1);
						if (this.style.isSet(StyleAttribute.BORDER_WIDTH)) {
							int bw = (int) this.style.get(StyleAttribute.BORDER_WIDTH);
							app.strokeWeight(bw);
						}
					}
					app.rect(this.sx, this.sy, this.sw, this.sh);
				}
				
				if (this.style.isSet(StyleAttribute.SHOW_GRID)) {
					if ((boolean) this.style.get(StyleAttribute.SHOW_GRID)) {
						if (this.style.isSet(StyleAttribute.GRID_WIDTH))
							app.strokeWeight((int) this.style.get(StyleAttribute.GRID_WIDTH));
						else
							app.strokeWeight(1);

						if (this.style.isSet(StyleAttribute.GRID_COLOUR)) {
							Colour c = (Colour) this.style.get(StyleAttribute.GRID_COLOUR);
							app.stroke(c.r, c.g, c.b, c.a);
						} else {
							app.stroke(255, 50);
						}
						for (int i = 0; i < this.cols + 1; i++) {
							app.line(sx + i * cellWidth, sy, sx + i * cellWidth, sy + sh);
						}

						for (int j = 0; j < this.rows + 1; j++) {
							app.line(sx, sy + j * cellHeight, sx + sw, sy + j * cellHeight);
						}
					}
				}

			}

			List<GUIElement> children = new ArrayList<GUIElement>();
			children.addAll(elements.values());
			Collections.sort(children);
			for (GUIElement ele : children) {
				ele.show(game);
			}

		}
	}
	
	// Add a child element, and set self as parent
	public GUIGrid addChild(String n, GUIElement e) {
		e.setParent(this);
		e.z += this.z; // Update z-index of child, so elements are layerd correctly
		this.elements.put(n, e);
		return this;
	}
	
	// Get a child given its name
	public GUIElement getChild(String n) {
		return elements.get(n);
	}
	
	// Return a list of elements under the mouse pointer
	public List<GUIElement> getHoveredChildren() {
		List<GUIElement> childs = new ArrayList<GUIElement>();
		for (GUIElement child : elements.values()) {
			if (child.visible) {
				if (child instanceof GUIGrid) { // Get child elements of subgrids, too
					if (child.hovered()) {
						List<GUIElement> subchilds = child.asGrid().getHoveredChildren();
						childs.addAll(subchilds);
						childs.add(child);
					}
				} else {
					if (child.hovered()) {
						childs.add(child);
					}
				}
			}
		}
		Collections.sort(childs); // Sort by z-index
		return childs;
	}

	@Override
	void click() {
		return;	// PASS
	}

}