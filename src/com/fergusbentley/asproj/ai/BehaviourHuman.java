package com.fergusbentley.asproj.ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fergusbentley.asproj.crafting.Material;
import com.fergusbentley.asproj.crafting.MaterialStack;
import com.fergusbentley.asproj.crafting.Recipe;
import com.fergusbentley.asproj.crafting.TechTree;
import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.entity.Harvestable;
import com.fergusbentley.asproj.entity.Interactable;
import com.fergusbentley.asproj.entity.living.Actor;
import com.fergusbentley.asproj.entity.living.ActorHuman;
import com.fergusbentley.asproj.entity.structure.StructAbode;
import com.fergusbentley.asproj.util.Region;
import com.fergusbentley.asproj.util.Util;

import processing.core.PApplet;
import processing.core.PVector;

public class BehaviourHuman extends Behaviour {

	public Map<String, Float> stats = new HashMap<String, Float>();
	
	public Boolean travelling; // Whether the actor is following a path
	public Boolean pathfinding; // Whether the actor is searching for a path
	
	public TaskQueue queue;
	
	private TechTree techTree;
	private Map<Material, Integer> materials;
	private Recipe building;
	
	Entity target;
	PVector targetPos;
	
	public ActorMap map; // The actor's "memory" of the world
	
	private List<PVector> route; // The current route being traversed
	private boolean lastFailed = false;

	private int movementCooldown; // How long until next movement
	private int counter; // Used to repeat actions
	
	public Boolean indoors; // Whether the actor is inside a building
	public StructAbode abode;
	
	public BehaviourHuman(Actor a) {
		super(a);
	}

	@Override
	public void spawn() {
		// Initialise stats
		this.stats.put("health", 100f);
		this.stats.put("exposure", 70f);
		this.stats.put("hunger", 0f);
		this.stats.put("thirst", 0f);
		
		// Initialise status flags
		this.indoors = false;
		this.travelling = false;
		this.pathfinding = false;
		
		// Initialise the actor's "memory" of the world
		this.map = new ActorMap(this.actor.world, this.actor.x, this.actor.y, 25);
		
		this.target = null;
		
		this.queue = new TaskQueue(ActorTask.done);
		this.techTree = new TechTree();
		this.materials = new HashMap<Material, Integer>();
	}

	@Override
	public void die(Entity cause) {}

	@Override
	public void attack(Entity target) {}

	@Override
	public void hit(Entity cause, int damage) {}

	@Override
	public void updateStats() {
		if (!this.indoors) {
			// increase exposure if outdoors
			this.affect("exposure", this.actor.world.weather.exposureModifier * 0.01f);
			this.actor.shown = true;
		} else {
			this.affect("exposure", -0.1f);
			this.actor.shown = false;
		}
		this.movementCooldown -= 1;
		if (this.movementCooldown < 0) this.movementCooldown = 0;
	}

	@Override
	public void act() {
		
		ActorTask currentTask = this.queue.current();
		// PApplet.println(currentTask.id);
		if (currentTask.id == "done") {
			if (this.indoors) {
				if (this.stats.get("exposure") < 10) {
					this.queue.done();
					this.queue.add(ActorTask.exitAbode);
				}
			}
			else if (this.stats.get("exposure") > 80) {
				this.queue.done();
				this.queue.add(ActorTask.seekShelter);
			}
			else {
				if (this.movementCooldown <= 0) {
					step();
					this.movementCooldown = 100;
					this.queue.done();
				}
			}
		}
		else if (currentTask.id == "seekShelter") {
			Map<Entity, PVector> abodes = this.map.search(StructAbode.class);
			// PApplet.println("Found " + abodes.size() + " abodes");
			if (abodes.size() > 0) {
				Entity closest = null;
				float cd = Float.MAX_VALUE;
				PVector pos = this.actor.pos();
				for (Entity e : abodes.keySet()) {
					float d = pos.dist(e.pos());
					if (d < cd) {
						cd = d;
						closest = e;
					}
				}
				this.target = closest;
				this.targetPos = closest.pos().add(0, 1);
				this.queue.done();
			} else {
				//this.queue.pushIn(ActorTask.build);
				this.building = this.techTree.getRecipe("abode1");
			}
		}
		else if (currentTask.id == "exitAbode") {
			if (this.abode != null) {
				this.abode.exit((ActorHuman)this.actor);
			}
			this.queue.done();
		}
		else if (currentTask.id == "navigate") {
			if (this.target != null) {
				if (!this.travelling) {
					if (!this.pathfinding) {
						PVector standingTile = actor.pos();
						if (this.actor.world.getRegion(standingTile) == this.actor.world.getRegion(this.targetPos)) {
							if (!standingTile.equals(this.target)) {
								if (this.map.isDiscovered(this.target.pos()) && !lastFailed) {
									Pathfinder.startPathfinding(this, this.map, standingTile, this.targetPos, false);
									this.pathfinding = true;
								} else {
									Pathfinder.startPathfinding(this, this.map, standingTile, this.targetPos, true);
									this.pathfinding = true;
								}
							}
						} else {
							this.lastFailed = false;
						}
					}
				} else {
					if (this.movementCooldown == 0) {
						if (this.route.size() > 0) {
							PVector next = this.route.get(0);
							if (this.actor.moveTo((int)next.x, (int)next.y)) {
								this.route.remove(0);
								this.map.discover(new Region((int)next.x, (int)next.y, 25));
							} else {
								this.travelling = false;
							}
							this.movementCooldown = 15;
						} else {
							this.travelling = false;
							this.lastFailed = false;
							this.queue.done();
						}
					}
				}
			} else {
				this.queue.done();
			}
		}
		else if (currentTask.id == "interact") {
			if (this.target instanceof Interactable) {
				((Interactable) this.target).interact((ActorHuman)this.actor);
				this.queue.done();
			} else if (this.target instanceof Harvestable) {
				MaterialStack mats = ((Harvestable) this.target).harvest();
				if (mats != null) {
					if (this.materials.get(mats.material) != null) {
						this.materials.put(mats.material, this.materials.get(mats.material) + mats.amount);
					} else {
						this.materials.put(mats.material, mats.amount);
					}
					this.queue.done();
				} else {
					this.queue.pushIn(ActorTask.gatherResources);
				}
			}
		}
		else if (currentTask.id == "build") {
			if (this.building == null) {
				this.queue.cancel();
			}
			else {
				if (!hasMaterials(this.building.required)) {
					this.queue.pushIn(ActorTask.gatherResources);
				} else {
					
				}
			}
		}
		else if (currentTask.id == "gatherResources") {
			if (this.building != null) {
				Material needed = null;
				for (MaterialStack ms : this.building.required) {
					if (this.materials.get(ms.material) == null) {
						needed = ms.material;
						break;
					}
					if (ms.amount > this.materials.get(ms.material)) {
						needed = ms.material;
						break;
					}
				}
				if (needed == null) this.queue.done();
				else {
					Entity nearestNeeded = findNearest(needed.resource);
					if (nearestNeeded != null) {
						PApplet.println(nearestNeeded);
						this.target = nearestNeeded;
						this.targetPos = this.target.pos().add(0, 1);
						this.queue.pushIn(ActorTask.navigateInteract);
					} else {
						this.queue.pushIn(ActorTask.explore);
					}
				}
			} else {
				this.queue.done();
			}
		}
		else if (currentTask.id == "explore") {
			if (this.counter < 10) {
				this.targetPos = this.map.randomBorderPoint();
				this.queue.pushIn(ActorTask.navigate);
				this.counter++;
			} else {
				this.counter = 0;
				this.queue.done();
			}
		}

	}

	private Entity findNearest(Class<? extends Harvestable> type) {
		Entity closest = null;
		float cd = Float.MAX_VALUE;
		for (Entity e : this.map.entities.keySet()) {
			if (e instanceof Harvestable) {
				Harvestable h = (Harvestable) e;
				if (e.getClass() == type && h.isHarvestable()) {
					float d = PApplet.dist(this.actor.x, this.actor.y, this.map.entities.get(e).x, this.map.entities.get(e).y);
					if (d < cd) {
						closest = e;
						cd = d;
					}
				}
			}
		}
		return closest;
	}

	private boolean hasMaterials(List<MaterialStack> required) {
		for (MaterialStack ms : required) {
			Integer amount = this.materials.get(ms.material);
			if (amount == null) return false;
			if (amount < ms.amount) return false;
		}
		return true;
	}

	private void step() {
		int x = Util.randInt(-1, 2);
		int y = Util.randInt(-1, 2);
		this.actor.move(x, y);
		this.map.discover(new Region((int)this.actor.x, (int)this.actor.y, 25));
	}

	private void affect(String name, float d) {
		Float stat = this.stats.get(name);
		if (stat != null) {
			this.stats.replace(name, stat + d);
		} else {
			PApplet.print("Error - no stat with name '" + name + "'.");
		}
	}

	@Override
	public void notifyOfThreadComplete(Thread thread) {
		PathfindingThread pt = (PathfindingThread) thread;
		this.route = pt.result;
		if (this.route == null) {
			pathfinding = false;
			travelling = false;
			lastFailed = true;
		} else {
			pathfinding = false;
			travelling = true;
			lastFailed = false;
		}
	}

}
