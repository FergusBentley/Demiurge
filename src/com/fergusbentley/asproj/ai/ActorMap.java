package com.fergusbentley.asproj.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fergusbentley.asproj.Config;
import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.util.Region;
import com.fergusbentley.asproj.world.World;

import processing.core.PVector;

public class ActorMap {

	private World world;
	
	private Boolean[][] map;
	
	public Region region;
	
	public Map<Entity, PVector> entities;
	
	public ActorMap(World wo, int x, int y, int w, int h) {
		this.world = wo;
		this.map = new Boolean[Config.WORLD_SIZE][Config.WORLD_SIZE];
		this.region = new Region(x, y, w, h);
		this.entities = new HashMap<Entity, PVector>();
		this.discover(this.region);
	}
	
	public ActorMap(World wo, int x, int y, int r) {
		this.world = wo;
		this.map = new Boolean[Config.WORLD_SIZE][Config.WORLD_SIZE];
		this.region = new Region(x, y, r);
		this.entities = new HashMap<Entity, PVector>();
		this.discover(this.region);
	}
	
	// Copy Constructor
	public ActorMap(ActorMap other) {
		this.world = other.world;
		this.map = other.map;
		this.region = other.region;
	}
	
	public void discover(Region r) {
		for (PVector p : r.iterate()) {
			int x = (int)p.x;
			int y = (int)p.y;
			if (world.inBounds(x, y)) {
				map[x][y] = world.isPassable(x, y);
			}
		}
		for (Entity e : world.entitiesInBounds(r.top(), r.left(), r.bottom(), r.right())) {
			if (this.entities.containsKey(e)) {
				this.entities.replace(e, e.pos());
			} else {
				this.entities.put(e, e.pos());
			}
		}
		updateRegion();
	}
	
	public void updateRegion() {
		for (int x = 0; x < Config.WORLD_SIZE; x++) {
			for (int y = 0; y < Config.WORLD_SIZE; y++) {
				if (map[x][y] != null) {
					if (x < region.left()) region.x = x;
					else if (x > region.right()) region.w = x - region.x;
					if (y < region.top()) region.y = y;
					else if (y > region.bottom()) region.h = y - region.y;			
				}
			}
		}
	}
	
	public void refreshRegion() {
		for (PVector p : region.iterate()) {
			int x = (int)p.x;
			int y = (int)p.y;
			if (isDiscovered(x, y)) {
				map[x][y] = world.isPassable(x, y);
			}
		}
		for (Entity e : world.entitiesInBounds(region.top(), region.left(), region.bottom(), region.right())) {
			if (isDiscovered(e.pos())) {
				if (this.entities.containsKey(e)) {
					this.entities.replace(e, e.pos());
				} else {
					this.entities.put(e, e.pos());
				}
			}
		}
	}

	public boolean isPassable(int x, int y) {
		return map[x][y];
	}
	
	public boolean isPassable(PVector p) {
		return map[(int)p.x][(int)p.y];
	}

	public boolean isDiscovered(int x, int y) {
		if (inBounds(x, y))	return map[x][y] != null;
		return false;
	}
	
	public boolean isDiscovered(PVector p) {
		if (inBounds((int)p.x, (int)p.y))
			return map[(int)p.x][(int)p.y] != null;
		return false;
	}

	private boolean inBounds(int x, int y) {
		return x >= 0 && y >= 0 && x < Config.WORLD_SIZE && y < Config.WORLD_SIZE;
	}

	public ArrayList<PVector> getPassableNeighbours(PVector current, boolean treatUndiscoveredAsPassable) {
		return getPassableNeighbours(current, treatUndiscoveredAsPassable, null);
	}

	public ArrayList<PVector> getPassableNeighbours(PVector current) {
		return getPassableNeighbours(current, false, null);
	}
	


	public ArrayList<PVector> getPassableNeighbours(PVector current, boolean treatUndiscoveredAsPassable, Entity entity) {
		ArrayList<PVector> neighbours = new ArrayList<PVector>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int nx = (int) (current.x + i - 1);
				int ny = (int) (current.y + j - 1);
				PVector np = new PVector(nx, ny);
				if (!np.equals(current)) {
					if (this.isDiscovered(np)) {
						boolean passable = entity == null ? this.isPassable(np) : (this.isPassable(np) || entity.colliding(nx, ny));
						if (passable) {
							neighbours.add(np);
						}
					} else if (treatUndiscoveredAsPassable) {
						neighbours.add(np);
					}
				}
			}
		}
		return neighbours;
	}

	public Map<Entity, PVector> search(Class<? extends Entity> t) {
		Map<Entity, PVector> found = new HashMap<Entity, PVector>();
		for (Entity e : this.entities.keySet()) {
			if (e.getClass().equals(t)) {
				found.put(e, this.entities.get(e));
			}
		}
		return found;
	}
	
}
