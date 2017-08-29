package com.fergusbentley.asproj;

public class Config {

	// Number of tiles per axis. Total number of tiles == WORLD_SIZE ^ 2
	public static final int WORLD_SIZE = 200;
	// Base unit length in pixels, when zoom == 1
	public static final int TILE_SIZE = 8;
	
	// Good old descriptive variable names :)
	public static final float DEFAULT_ZOOM = 1;
	public static final float MAX_ZOOM = 4;
	public static final float MIN_ZOOM = 0.5f;
	

	// Maximum number of entities, cap prevents over population, causing lag
	public static final int ENTITY_LIMIT = 500;
	public static final int MAX_ROCKS = 50;
	public static final int MAX_TREES = 50;
	public static final int MAX_SHEEP = 50;
	public static final int MAX_DEER = 50;
	
	public static boolean debug = false;
	
	// GUI settings
	public static final int GRID_COLS = 50;
	public static final int GRID_ROWS = 32;
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 512;
	
}
