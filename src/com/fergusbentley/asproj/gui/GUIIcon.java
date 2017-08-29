package com.fergusbentley.asproj.gui;

import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.Main;

import processing.core.PImage;

public class GUIIcon extends GUIElement {

	private String path;
	private PImage file;

	public GUIIcon(Main a, int x, int y, int w, int h, String pa) {
		super(a, x, y, w, h);
		this.path = pa;
		this.file = app.loadImage(path);
	}

	@Override
	void show(GameState game) {
		
		app.image(this.file, sx, sy, sw, sh);

	}

	@Override
	void click() { return; }

}
