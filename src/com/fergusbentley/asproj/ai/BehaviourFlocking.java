package com.fergusbentley.asproj.ai;

import java.util.ArrayList;
import java.util.List;
import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.entity.living.Actor;
import com.fergusbentley.asproj.entity.living.ActorSheep;

import processing.core.PVector;

public class BehaviourFlocking extends Behaviour {

	public String flockID;
	
	private int movementCooldown;
	private int health;
	
	public BehaviourFlocking(Actor a, String fid) {
		super(a);
		this.flockID = fid;
		this.movementCooldown = 50 + (int)(Math.random() * 100);
	}
	
	@Override
	public void spawn() {}
	
	@Override
	public void die(Entity cause) {}
	
	@Override
	public void attack(Entity target) {}
	
	@Override
	public void hit(Entity cause, int damage) {
		this.health -= damage;
		if (this.health < 0) die(cause);
	}

	@Override
	public void updateStats() {
		movementCooldown -= 1;
	}

	@Override
	public void act() {
		
		if (movementCooldown == 0) {

			List<Actor> flock = new ArrayList<Actor>();
			
			int x = this.actor.x;
			int y = this.actor.y;
			
			PVector cohesionVector = new PVector();
			PVector seperationVector = new PVector();
			
			flock.clear();
			for (Entity e : this.actor.world.entitiesInBounds(x - 10, y - 10, x + 10, y + 10)) {
				if (e instanceof Actor && !e.equals(this.actor)) {
					Actor a = (Actor) e;
					if (a.behaviour instanceof BehaviourFlocking) {
						BehaviourFlocking b = (BehaviourFlocking) a.behaviour;
						String fid = b.flockID;
						if (fid == this.flockID) {
							flock.add(a);
							cohesionVector.x += a.x;
							cohesionVector.y += a.y;
							seperationVector.x += a.x - x;
							seperationVector.y += a.y - y;
						}
					}
				}
			}
			
			cohesionVector.x /= flock.size();
			cohesionVector.y /= flock.size();
			seperationVector.x /= -flock.size();
			seperationVector.y /= -flock.size();
			
			cohesionVector.normalize();
			seperationVector.normalize().mult(0.4f);
			
			PVector resultant = cohesionVector.add(seperationVector);
			//int dx = (int)Math.round(resultant.x);
			//int dy = (int)Math.round(resultant.y);
			//if ((dx == 0 && dy == 0) || !this.actor.world.isPassable(this.actor.cx + dx, this.actor.cy + dy, this.actor.colliderWidth, this.actor.colliderHeight, this.actor)) {
				int dx = (int) Math.round((Math.random() * 2) - 1);
				int dy = (int) Math.round((Math.random() * 2) - 1);
			//}
			
			this.actor.move(dx, dy);
			
			if (this.actor instanceof ActorSheep) {
				if (dx != 0) ((ActorSheep) actor).facing(dx);
			}
			
			movementCooldown = 50 + (int)(Math.random() * 100);
		}
		
	}

	@Override
	public void notifyOfThreadComplete(Thread thread) {
		// pass
	}

}
