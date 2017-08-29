package com.fergusbentley.asproj.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fergusbentley.asproj.util.Region;

import processing.core.PVector;

public class PathfindingThread extends NotifyingThread {
	
	ActorMap map;
	PVector start;
	PVector goal;
	
	boolean allowWandering;
	
	public ArrayList<PVector> result;
	
	public PathfindingThread (ActorMap am, PVector s, PVector g, boolean w) {
		this.map = am;
		this.start = s;
		this.goal = g;
		this.allowWandering = w;
	}

	public ArrayList<PVector> findRoute() {		
		// The set of nodes discovered but not evaluated
		ArrayList<PVector> openSet = new ArrayList<PVector>();
		openSet.add(start);
		
		// The set of nodes already evaluated
		ArrayList<PVector> closedSet = new ArrayList<PVector>();
		
		Map<PVector, Float> fScores = new HashMap<PVector, Float>();
		fScores.put(start, heuristic(start, goal));
		for (PVector m : Region.world.iterate()) {
			fScores.putIfAbsent(m, Float.MAX_VALUE);
		}
		
		Map<PVector, Float> gScores = new HashMap<PVector, Float>();
		gScores.put(start, 0f);
		for (PVector m : Region.world.iterate()) {
			gScores.putIfAbsent(m, Float.MAX_VALUE);
		}
		
		Map<PVector, PVector> cameFrom = new HashMap<PVector, PVector>();
		
		PVector current;
		while (openSet.size() > 0) {
			current = lowestScore(openSet, fScores);
			if (current.equals(goal))
				return reconstructPath(cameFrom, current);
			
			openSet.remove(current);
			closedSet.add(current);
			
			ArrayList<PVector> passableNeighbours = map.getPassableNeighbours(current, allowWandering);
			for (PVector neighbour : passableNeighbours) {
				if (closedSet.contains(neighbour)) continue;
				if (!openSet.contains(neighbour)) openSet.add(neighbour);
				
				// A tentative g score
				float tgScore = gScores.get(current) + distBetween(current, neighbour);
				if (tgScore >= gScores.get(neighbour)) continue;
				
				cameFrom.put(neighbour, current);
				gScores.put(neighbour, tgScore);
				fScores.put(neighbour, tgScore + heuristic(neighbour, goal));
			}
		}
		
		return null;
	}
	
	private ArrayList<PVector> reconstructPath(Map<PVector, PVector> cameFrom, PVector current) {
		ArrayList<PVector> totalPath = new ArrayList<PVector>();
		totalPath.add(current);
		while (cameFrom.containsKey(current)) {
			current = cameFrom.get(current);
			totalPath.add(current);
		}
		Collections.reverse(totalPath);
		return totalPath;
	}

	private PVector lowestScore(ArrayList<PVector> set, Map<PVector, Float> scores) {
		PVector min = null;
		for (PVector t : set) {
			if (min == null) min = t;
			else if (scores.get(t) < scores.get(min)) min = t;
		}
		return min;
	}
	
	// Estimation of shortest path to goal
	private float heuristic(PVector a, PVector b) {
		return distBetween(a, b);
	}
	
	private float distBetween(PVector a, PVector b) {
		return a.dist(b);
	}

	@Override
	public void doRun() {
		this.result = findRoute();
	}
	
	
}