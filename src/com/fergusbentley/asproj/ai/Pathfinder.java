package com.fergusbentley.asproj.ai;

import java.util.List;

import com.fergusbentley.asproj.world.Tile;

import processing.core.PVector;

public class Pathfinder {

	public static boolean done;
	public static List<Tile> result;
	
	public static void startPathfinding(Behaviour b, ActorMap map, PVector start, PVector goal, boolean aw) {
		PathfindingThread t = new PathfindingThread(new ActorMap(map), start, goal, aw);
		t.addListener(b);
		t.start();
	}
	
}
