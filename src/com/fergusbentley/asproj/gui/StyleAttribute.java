package com.fergusbentley.asproj.gui;

public enum StyleAttribute {

	BORDER_WIDTH("int"),
	BORDER_COLOUR("colour"),
	FILL_COLOUR("colour"),
	SHOW_GRID("boolean"),
	GRID_COLOUR("colour"),
	GRID_WIDTH("int"),
	FONT_COLOUR("colour"),
	FONT_SIZE("int"),
	;

	String type;

	private StyleAttribute(String t) {
		this.type = t;
	}

}