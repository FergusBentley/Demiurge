package com.fergusbentley.asproj.gui;

import com.fergusbentley.asproj.util.Colour;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class GUIStyle {

	PApplet app;
	
	String path;
	JSONObject json;

	public GUIStyle(PApplet a, String path) {
		this.app = a;
		this.path = path;
		this.json = app.loadJSONObject(path);
	}

	public Object get(StyleAttribute attr) {
		if (attr.type == "string")
			return json.getString(attr.name().toLowerCase());
		if (attr.type == "int")
			return json.getInt(attr.name().toLowerCase());
		if (attr.type == "boolean")
			return json.getBoolean(attr.name().toLowerCase());
		if (attr.type == "colour") {
			JSONArray array = json.getJSONArray(attr.name().toLowerCase());
			int r = array.getInt(0);
			int g = array.getInt(1);
			int b = array.getInt(2);
			int alpha = array.getInt(3);
			return new Colour(r, g, b, alpha);
		}
		return null;
	}

	public boolean isSet(StyleAttribute attr) {
		return !json.isNull(attr.name().toLowerCase());
	}

}