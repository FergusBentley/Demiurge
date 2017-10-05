package com.fergusbentley.asproj;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;

public class Resources {

	private static Map<String, PImage> resources = new HashMap<String, PImage>();

	private static PApplet app;
	
	public static void load(PApplet a) {
		app = a;
		loadDirectory("img");
	}
	
	private static void loadDirectory(String path) {
		File folder = new File("data/" + path);
		File[] list = folder.listFiles();

		for (int i = 0; i < list.length; i++) {
			if (list[i].isFile()) {
				String name = list[i].getName();
				PImage image = app.loadImage(path + "/" + name);
				if (image != null) {
					String pn = path + "/" + name;
					resources.put(pn.substring(4, pn.length() - 4), image);
				} else {
					PApplet.println("Error: Identified image named '"+ name +"', but failed to load it.");
				}
				// PApplet.println(pn.substring(4, pn.length() - 4));
			}
			else if (list[i].isDirectory()) {
				String name = list[i].getName();
				loadDirectory(path + "/" + name);
			}
		}
	}
	
	public static PImage get(String name) {
		if (resources.containsKey(name)) return resources.get(name);
		PApplet.println("Error: No such resource named '"+ name +"'");
		return null;
	}
	
	public static PImage getRandom(String folder) {
		List<String> names = new ArrayList<String>();
		for (String k : resources.keySet()) {
			if (k.startsWith(folder)) names.add(k);
		}
		if (names.size() > 0) {
			int i = (int) (app.random(names.size()));
			String n = names.get(i);
			return get(n);
		}
		return null;
	}
	
	public static int getFromMap(String name, int i) {
		PImage map = get(name);
		if (map != null) {
			if (i < map.pixels.length && i > -1) return map.pixels[i];
			return 0;
		}
		return 0;
	}

}
