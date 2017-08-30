package com.fergusbentley.asproj.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fergusbentley.asproj.Config;
import com.fergusbentley.asproj.Main;
import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.entity.EntityMarker;
import com.fergusbentley.asproj.entity.EntityRock;
import com.fergusbentley.asproj.entity.EntityTree;
import com.fergusbentley.asproj.entity.living.Actor;
import com.fergusbentley.asproj.entity.living.ActorDeer;
import com.fergusbentley.asproj.entity.living.ActorSheep;
import com.fergusbentley.asproj.util.Region;

import processing.core.PApplet;
import processing.core.PVector;

public class World {

	private Main app;
	
	public Tile[][] terrain; // 2D array of tiles that make up the world
	public Integer[][] regions = new Integer[Config.WORLD_SIZE][Config.WORLD_SIZE]; 
	public List<Entity> entities = new ArrayList<Entity>(); // All entities in the world (trees, animals, structures, etc.)
	public List<Entity> spawnList = new ArrayList<Entity>();

	public Weather weather;
	public int time;
	
	private long seed; // For seeding the noise engine

	// 0 = default (voronoi), 1 = quick (noise)
	private int mode = 0;
	
	public World(Main a, long s, int m) {
		
		this.app = a;
		
		this.seed = s;
		this.mode = m;
		
		this.terrain = new Tile[Config.WORLD_SIZE][Config.WORLD_SIZE];

		this.populate();
		
		this.weather = Weather.MILD;
		
		this.calculateRegions();
	}
	
	// COPY CONSTRUCTOR
	public World(World another) {
		this.seed = another.seed;
		this.mode = another.mode;
		
		this.terrain = another.terrain;

		this.entities = new ArrayList<Entity>();
		this.entities.addAll(another.entities);
		
		this.weather = another.weather;
	}
	
	public Tile getTile(int x, int y) {
		if (x >= terrain.length || y >= terrain.length || x < 0 || y < 0)
			return null;
		return terrain[x][y];
	}

	// Generate terrain
	public void populate() {
		
		if (this.mode == 0)
			terrain = smoothTerrain(generateVoronoi(10000));
		else
			terrain = generateNoise();

		int i = 0;
		while (i < Config.MAX_ROCKS) {
			int x = (int) Math.floor(app.random(terrain.length));
			int y = (int) Math.floor(app.random(terrain.length));
			for (int xx = x - 5; xx < x + 5; xx++) {
				for (int yy = y - 5; yy < y + 5; yy++) {
					if (xx > 0 && yy > 0 && xx < terrain.length && yy < terrain.length) {
						if (app.noise(xx, yy) < 0.3 && isPassable(xx - 1, yy - 1, 3, 2)) {
							addEntity(new EntityRock(this, xx, yy));
							i++;
						}
					}
				}
			}
		}

		i = 0;
		int j = 0;
		while (i < Config.MAX_TREES) {
			int x = (int) Math.floor(app.random(terrain.length));
			int y = (int) Math.floor(app.random(terrain.length));
			for (int xx = x - (int) (app.random(5)) + 5; xx < x + (int) (app.random(5)) + 5; xx++) {
				for (int yy = y - (int) (app.random(5)) + 5; yy < y + (int) (app.random(5)) + 5; yy++) {
					if (xx > 0 && yy > 0 && xx < terrain.length && yy < terrain.length) {
						if (app.noise(xx, yy) < 0.3 && isPassable(xx, yy)) {
							addEntity(new EntityTree(this, xx, yy, (int) (app.random(3)) + 1));
							i++;
						}
					}
				}
			}
			if (j < Config.MAX_DEER) {
				int xd = (int) (x + app.random(3, 5) * app.random(-1, 1));
				int yd = (int) (y + app.random(3, 5) * app.random(-1, 1));
				int m = (int) Math.ceil(app.random(6) + 2);
				for (int n = 0; n < m; n++) {
					int xx = (int)(xd + app.random(-5, 5));
					int yy = (int)(yd + app.random(-5, 5));
					if (isPassable(xx - 1, yy, 2, 1)) {
						addEntity(new ActorDeer(this, xx, yy));
						j++;
					}
				}
			}
		}
		
		i = 0;
		while (i < Config.MAX_SHEEP) {
			int x = (int) Math.floor(app.random(terrain.length));
			int y = (int) Math.floor(app.random(terrain.length));
			int m = (int) Math.ceil(app.random(6) + 2);
			for (int n = 0; n < m; n++) {
				int xx = (int)(x + app.random(-5, 5));
				int yy = (int)(y + app.random(-5, 5));
				if (isPassable(xx - 1, yy, 2, 1)) {
					addEntity(new ActorSheep(this, xx, yy));
					i++;
				}
			}
		}

		sortEntities();

	}
	
	private Tile[][] smoothTerrain(Tile[][] tiles) {
		Tile[][] smoothed = new Tile[Config.WORLD_SIZE][Config.WORLD_SIZE];
		
		for (int x = 0; x < Config.WORLD_SIZE; x++) {
			for (int y = 0; y < Config.WORLD_SIZE; y++) {
				int neighbours = countSimilarNeighbours(tiles, x, y);
				if (neighbours < 3) {
					if (tiles[x][y].getClass() == TileGrass.class) smoothed[x][y] = new TileWater(x, y, this);
					if (tiles[x][y].getClass() == TileWater.class) smoothed[x][y] = new TileGrass(x, y, this);
				} else {
					smoothed[x][y] = tiles[x][y];
				}
			}
		}
		
		return smoothed;
	}

	private Tile[][] generateNoise() {
		Tile[][] map = new Tile[Config.WORLD_SIZE][Config.WORLD_SIZE];
		for (int x = 0; x < Config.WORLD_SIZE; x++) {
			for (int y = 0; y < Config.WORLD_SIZE; y++) {
				map[x][y] = app.noise(x * 0.05f, y * 0.05f) > 0.4 ? new TileGrass(x, y, this) : new TileWater(x, y, this);
			}
		}
		return map;
	}
	
	private Tile[][] generateVoronoi(int startpoints) {
		
		Tile[][] voronoi = new Tile[Config.WORLD_SIZE][Config.WORLD_SIZE];
		List<VoronoiNode> points = new ArrayList<VoronoiNode>();

		while (startpoints > 0) {
			int x = (int) app.random(Config.WORLD_SIZE) + 1;
			int y = (int) app.random(Config.WORLD_SIZE) + 1;
			points.add(new VoronoiNode(x, y, app.noise(x * 0.05f, y * 0.05f) > 0.4 ? TileGrass.class : TileWater.class));
			startpoints -= 1;
		}
		
		for (int x = 0; x < Config.WORLD_SIZE; x++) {
			if (x == 0 || x == Config.WORLD_SIZE - 1) {
				for (int y = 0; y < Config.WORLD_SIZE; y++) {
					points.add(new VoronoiNode(x, y, TileWater.class));
				}
			} else {
				points.add(new VoronoiNode(x, 0, TileWater.class));
				points.add(new VoronoiNode(x, Config.WORLD_SIZE - 1, TileWater.class));
			}
		}

		for (int i = 0; i < Config.WORLD_SIZE; i++) {
			for (int j = 0; j < Config.WORLD_SIZE; j++) {
				Tile n;
//				if (closestStartPointDistance(points, i, j) == 0) {
//					n = new TileDebug(i, j, this);
//				}
//				else {
//					Class<?> close = closestStartPoint(points, i, j);
//					n = close.equals(TileGrass.class) ? new TileGrass(i, j, this) : new TileWater(i, j, this);
//				}
				Class<?> close = closestStartPoint(points, i, j);
				n = close.equals(TileGrass.class) ? new TileGrass(i, j, this) : new TileWater(i, j, this);
				voronoi[i][j] = n;
			}
		}

		return voronoi;
	}

	private Class<?> closestStartPoint(List<VoronoiNode> points, int x, int y) {
		Class<?> cv = null;
		float cd = Config.WORLD_SIZE ^ 2;

		for (VoronoiNode node : points) {
			int i = node.x;
			int j = node.y;
			float d = PApplet.dist(i, j, x, y);
			if (d < cd) {
				cd = d;
				cv = node.type;
			}
		}

		return cv;
	}
	
	private Tile[][] getNeighbours(Tile[][] tiles, int x, int y) {
		Tile[][] neighbours = new Tile[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (inBounds(x + i - 1, y + j - 1))
					neighbours[i][j] = tiles[x + i - 1][y + j - 1];
			}
		}
		return neighbours;
	}
	
	public int countSimilarNeighbours(Tile[][] tiles, int x, int y) {
		int count = -1;
		Tile[][] neighbours = getNeighbours(tiles, x, y);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (neighbours[i][j] != null) {
					if (tiles[x][y].getClass() == neighbours[i][j].getClass()) {
						count += 1;
					}
				}
			}
		}
		return count;
	}
	
	public int countSimilarNeighbours(int x, int y) {
		return countSimilarNeighbours(this.terrain, x, y);
	}

	public boolean isPassable(int x, int y) {
		if (x < 0 || y < 0 || x >= Config.WORLD_SIZE || y >= Config.WORLD_SIZE)
			return false;
		if (!terrain[x][y].isPassable())
			return false;
		for (Entity e : entities) {
			if (e.colliding(x, y) && !(e instanceof EntityMarker))
				return false;
		}
		return true;
	}
	
	public boolean isPassable(int x, int y, Entity except) {
		if (x < 0 || y < 0 || x >= Config.WORLD_SIZE || y >= Config.WORLD_SIZE)
			return false;
		if (!terrain[x][y].isPassable())
			return false;
		for (Entity e : entities) {
			if (e.colliding(x, y) && !e.equals(except) && !(e instanceof EntityMarker))
				return false;
		}
		return true;
	}

	public boolean isPassable(int x, int y, int w, int h) {
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (x + i < 0 || y + j < 0 || x + i >= Config.WORLD_SIZE - (w + 1)
						|| y + j >= Config.WORLD_SIZE - (h + 1))
					return false;
				if (!isPassable(x + i, y + j))
					return false;
			}
		}
		return true;
	}

	public boolean isPassable(int x, int y, int w, int h, Entity except) {
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (x + i < 0 || y + j < 0 || x + i >= Config.WORLD_SIZE - (w + 1)
						|| y + j >= Config.WORLD_SIZE - (h + 1))
					return false;
				if (!isPassable(x + i, y + j, except))
					return false;
			}
		}
		return true;
	}
	
	public boolean isReachable(int x, int y) {
		for (Tile n : getTile(x, y).getPassableNeighbours()) {
			if (n.isPassable()) return true; 
		}
		return false;
	}
	
	public List<Entity> entitiesInBounds(int x1, int y1, int x2, int y2) {
		List<Entity> inbounds = new ArrayList<Entity>();
		
		for (Entity e : entities) {
			if (e.x >= x1 && e.y >= y1 && e.x <= x2 && e.y <= y2) inbounds.add(e);
		}
		
		return inbounds;
	}

	
	public void sortEntities() {
		Collections.sort(entities);
	}
	
	public void addEntity(Entity e) {
		this.entities.add(e);
		if (e instanceof Actor) {
			((Actor) e).behaviour.spawn();
		}
	}
	
	public boolean inBounds(int x, int y) {
		return x >= 0 && y >= 0 && x < Config.WORLD_SIZE && y < Config.WORLD_SIZE;
	}

	public List<Entity> getEntitiesUnderMouse(int gx, int gy) {
		List<Entity> ents = new ArrayList<Entity>();
		for (Entity ent : entities) {
			if (ent.hovered(gx, gy) && ent.shown) ents.add(ent);
		}
		Collections.sort(ents);
		return ents;
	}
	
	public Entity entityAt(int x, int y) {
		for (Entity ent : entities) {
			if (ent.x == x && ent.y == y) return ent;
		}
		return null;
	}

	public List<Tile> terrainAsList() {
		List<Tile> tiles = new ArrayList<Tile>();
		for (int x = 0; x < Config.WORLD_SIZE; x++) {
			for (int y = 0; y < Config.WORLD_SIZE; y++) {
				tiles.add(getTile(x, y));
			}
		}
		return tiles;
	}
	
	public int getRegion(Tile t) {
		return regions[t.x][t.y];
	}
	
	public int getRegion(PVector t) {
		return regions[(int)t.x][(int)t.y];
	}
	
	private void calculateRegions() {
		ArrayList<PVector> unvisited = Region.world.iterate();
		int id = 1;
		while (!unvisited.isEmpty()) {
			PVector node = unvisited.get(0);
			ArrayList<PVector> visited = floodFillRegion(node, id);
			unvisited.removeAll(visited);
			id += 1;
		}
		
	}
	
	private ArrayList<PVector> floodFillRegion(PVector node, int id) {
		ArrayList<PVector> visited = new ArrayList<PVector>();
		regions[(int)node.x][(int)node.y] = id;
		ArrayList<PVector> queue = new ArrayList<PVector>();
		queue.add(node);
		visited.add(node);
		boolean passable = isPassable((int)node.x, (int)node.y);
		while (queue.size() > 0) {
			PVector n = queue.get(0);
			queue.remove(0);
			int x = (int)n.x;
			int y = (int)n.y;
			for (PVector neighbour : new Region(x, y, 1).iterate()) {
				int nx = (int)neighbour.x;
				int ny = (int)neighbour.y;
				if (inBounds(nx, ny)) {
					if (regions[nx][ny] == null) {
						if (terrain[nx][ny].isPassable() == passable) {
							regions[nx][ny] = id;
							queue.add(neighbour);
							visited.add(neighbour);
						}
					}
				}
			}
		}
		return visited;
	}
	
	public void save() {
		
	}
}
