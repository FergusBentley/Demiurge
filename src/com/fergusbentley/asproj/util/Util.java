package com.fergusbentley.asproj.util;

import java.util.concurrent.ThreadLocalRandom;

import com.fergusbentley.asproj.Config;
import com.fergusbentley.asproj.GameState;

public class Util {
	
	public static float lerp(float a, float b, float f) {		
		return a + f * (b - a);		
	}
	
	public static float logistic(float x) {
		return (float) (1 / (1 + Math.pow(Math.E, -x)));
	}
	
	public static float sigmoid(float x, float s) {
		return logistic((float) (s * (x - 0.5)));
	}
	
	// Transform a y value of y = x, where 0 < x < 1,
	// to a cubic y value around (0.5, 0.5), with a plateau narrowness defined by n
	public static float cubic (float x, float n) {
	  return (float) (1 - (Math.pow((n * x) - (0.5 * n), 3) + 0.5));
	}
	
	
	public static int randInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}
	
	public static int randRangeSigned(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max) * randSign();
	}
	
	public static int randSign() {
		int[] signs = {-1, 1};
		int rnd = (int)Math.round(ThreadLocalRandom.current().nextFloat());
		return signs[rnd];
	}
	
	// x-coordinate of a tile -> position on screen
	public static float gridToScreenX(GameState game, int gx) {
		return (float) (((Config.SCREEN_WIDTH - game.getScale() * Config.WORLD_SIZE) / 2f) + (game.getScale() * (gx + game.getPan().x)));
	}

	// y-coordinate of a tile -> position on screen
	public static float gridToScreenY(GameState game, int gy) {
		return (float) (((Config.SCREEN_HEIGHT - game.getScale() * Config.WORLD_SIZE) / 2f) + (game.getScale() * (gy + game.getPan().y)));
	}

	// position on screen -> x-coordinate of nearest tile
	public static int screenToGridX(GameState game, float sx) {
		return (int) ((((sx - ((Config.SCREEN_WIDTH - game.getScale() * Config.WORLD_SIZE)) / 2f)) / game.getScale()) - game.getPan().x);
	}

	// position on screen -> y-coordinate of nearest tile
	public static int screenToGridY(GameState game, float sy) {
		return (int) ((((sy - ((Config.SCREEN_HEIGHT - game.getScale() * Config.WORLD_SIZE)) / 2f)) / game.getScale()) - game.getPan().y);
	}
	
}
