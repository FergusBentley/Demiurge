package com.fergusbentley.asproj.ai;

import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.entity.living.Actor;

public abstract class Behaviour implements ThreadCompleteListener {
	
	protected Actor actor;
	
	public abstract void spawn();
	public abstract void die(Entity cause);
	public abstract void attack(Entity target);
	public abstract void hit(Entity cause, int damage);
	
	public abstract void updateStats();
	
	public abstract void act();
	
	public Behaviour(Actor a) {
		this.actor = a;
	}
	
	public void tick() {
		this.updateStats();
		this.act();
	}
	
}
