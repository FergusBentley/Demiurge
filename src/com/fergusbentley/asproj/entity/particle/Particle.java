package com.fergusbentley.asproj.entity.particle;

import com.fergusbentley.asproj.util.Util;

import processing.core.PVector;

public class Particle {
	
	public int y;
	public int x;
	private PVector vel;
	
	private int age;
	private int maxAge;
	public boolean dead;

	public Particle(int x, int y, int mA) {
		this.x = x;
		this.y = y;
		this.vel = new PVector(Util.randInt(-1, 2), Util.randInt(-2, 1));
		
		this.maxAge = mA;
		this.age = 0;
		
		this.dead = false;
	}
	
	public void tick() {
		float g = 0.2f;
		this.vel = new PVector(this.vel.x, this.vel.y + g);
		this.x += this.vel.x;
		this.y += this.vel.y;
	
		this.age += 1;
		this.dead = this.age >= this.maxAge;
		
	}

	public void reset() {
		this.x = 0;
		this.y = 0;
		this.vel = new PVector(Util.randInt(-1, 2), Util.randInt(-2, 1));
		this.age = 0;
		this.dead = false;
	}
	
}
