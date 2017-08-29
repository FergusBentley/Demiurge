package com.fergusbentley.asproj.entity.particle;

import java.util.ArrayList;
import java.util.List;

import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.util.Colour;
import com.fergusbentley.asproj.util.Util;
import com.fergusbentley.asproj.world.World;

import processing.core.PApplet;
import processing.core.PImage;

public class ParticleSystem extends Entity {

	private List<Particle> particles;
	private int maxParticleCount;
	private PImage sprite;
	private int size;
	
	public ParticleSystem(World w, int x, int y, PImage t, int s) {
		super(w, x, y, new int[s * s], s, s);
		this.size = s;
		this.y += this.size / 2;
		this.updateCorners();
		this.sprite = t;
		particles = new ArrayList<Particle>();
		this.maxParticleCount = 10;
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				this.texture[j * this.size + i] = Colour.TRANSPARENT;
			}
		}
	}

	@Override
	public void tick() {
		//if (System.nanoTime() % 2 == 0) {
			if (this.particles.size() < this.maxParticleCount) {
				this.particles.add(new Particle(0, 0, Util.randInt(5, 20)));
			}
			
			for (Particle p : this.particles) {
				int px = (this.size / 2) + p.x;
				int py = (this.size / 2) + p.y;
				if (px >= 0 && px < this.size && py >= 0 && py < this.size) {
					this.texture[py * this.size + px] = Colour.TRANSPARENT;
				}
				p.tick();
				px = (this.size / 2) + p.x;
				py = (this.size / 2) + p.y;
				if (px >= 0 && px < this.size && py >= 0 && py < this.size && !p.dead) {
					this.texture[py * this.size + px] = Colour.MAGENTA;
				} else {
					p.reset();
				}
			}
		//}
	}


	
	
}
