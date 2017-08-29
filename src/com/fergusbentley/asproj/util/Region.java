package com.fergusbentley.asproj.util;

import java.util.ArrayList;

import com.fergusbentley.asproj.Config;

import processing.core.PVector;

public class Region {

	public static Region world = new Region(0, 0, Config.WORLD_SIZE, Config.WORLD_SIZE);
	
	public int x, y, w, h;
	
	public Region(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public Region(int x, int y, int r) {
		this.x = x - r;
		this.y = y - r;
		this.w = r * 2 + 1;
		this.h = r * 2 + 1;
	}
	
	public int top() {
		return y;
	}
	
	public int bottom() {
		return y + h - 1;
	}
	
	public int left() {
		return x;
	}
	
	public int right() {
		return x + w - 1;
	}
	
	public boolean overlapping(Region r) {
		for (int i = left(); i <= right(); i++) {
			for (int j = top(); j <= bottom(); j++) {
				if (r.contains(i, j)) return true;
			}
		}
		return false;
	}
	
	public ArrayList<PVector> iterate() {
		ArrayList<PVector> points = new ArrayList<PVector>();
		for (int i = left(); i <= right(); i++) {
			for (int j = top(); j <= bottom(); j++) {
				points.add(new PVector(i, j));
			}
		}
		return points;
	}
	
	public boolean contains(int i, int j) {
		return top() <= j && j <= bottom() && left() <= i && i <= right();
	}
	
}
