package com.fergusbentley.asproj.gui;

import processing.core.PImage;

import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.Main;
import com.fergusbentley.asproj.Resources;

public class GUIIcon extends GUIElement {

	private String path;
	private PImage file;

	public GUIIcon(Main a, int x, int y, int w, int h, String pa) {
		super(a, x, y, w, h);
		this.path = pa;
		this.file = Resources.get(path);
	}

	@Override
	void show(GameState game) {
		
		app.image(this.file, sx, sy, sw, sh);

	}

	@Override
	void click() { return; }

}
