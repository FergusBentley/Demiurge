package com.fergusbentley.asproj;

import java.util.List;
import java.util.concurrent.Callable;

import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.entity.living.ActorHuman;
import com.fergusbentley.asproj.entity.structure.StructAbode;
import com.fergusbentley.asproj.gui.GUIButton;
import com.fergusbentley.asproj.gui.GUIElement;
import com.fergusbentley.asproj.gui.MainGUI;
import com.fergusbentley.asproj.gui.Viewport;
import com.fergusbentley.asproj.world.World;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.event.MouseEvent;

public class Main extends PApplet implements PConstants {

	private MainGUI gui;
	private World world;
	private Viewport viewport;
	
	private PFont font;
	
	private int loadStart = millis();
	
	private long seed = 1234L;
	
	private GameState game;
	
	// Start Processing thread
	public static void main(String[] args) {
		PApplet.main("com.fergusbentley.asproj.Main");
	}

	
	// Set processing app parameters
	public void settings() {
		size(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
		noSmooth();
	}

	
	// Initialisation, called before first call to draw()
	public void setup() {
		
		println("[LOG] " + dataPath(""));
		
		font = loadFont("Pixellari-16.vlw");
		textFont(font);
		
		//  GUIStyle mainGridStyle = new GUIStyle(this, "styles/grid.json");
		//  GUIStyle debugStyle = new GUIStyle(this, "styles/debug.json");
		
		game = new GameState();
		
		Resources.load(this); // Load all images under /data/img/
		
		InputHandler.addBind(UP, new Callable<Boolean>() {
			public Boolean call() {
				game.pan(0, 1);
				return true;
			}
		});
		
		InputHandler.addBind(DOWN, new Callable<Boolean>() {
			public Boolean call() {
				game.pan(0, -1);
				return true;
			}
		});
		
		InputHandler.addBind(LEFT, new Callable<Boolean>() {
			public Boolean call() {
				game.pan(1, 0);
				return true;
			}
		});
		
		InputHandler.addBind(RIGHT, new Callable<Boolean>() {
			public Boolean call() {
				game.pan(-1, 0);
				return true;
			}
		});
		
		InputHandler.addBind(SHIFT, new Callable<Boolean>() {
			public Boolean call() {
				Config.debug = !Config.debug;
				return true;
			}
		});
		
		gui = new MainGUI(this);
		gui.construct(game);
		gui.show(game);
		
		viewport = new Viewport(this);
		
	}

	
	// Main loop, called once per frame, target 60fps
	public void draw() {
		
		game.setTime(millis());

		if (game.getState() == GameState.MAIN_MENU) {
			
		}
		
		if (game.getState() == GameState.START_GENERATION) {

			randomSeed(seed); // seed the generators
			noiseSeed(seed);
			
			gui.getChild("mainMenu").visible = false;
			
			game.setState(GameState.GENERATION_SCREEN); // Move on to the next phase
			
			loadStart = millis(); // 
			
			thread("loadWorld");
		}
		
		else if (game.getState() == GameState.START_LOAD) {

			// TBA
			
		}
		
		else if (game.getState() == GameState.GENERATION_SCREEN || game.getState() == GameState.LOAD_SCREEN) {}
		
		else if (game.getState() == GameState.IN_GAME) {
			
			if (!game.isPaused()) {
			
				// Update entities
				for (Entity e : world.entities) {
					e.tick();
				}
				
				for (Entity e : world.spawnList) {
					if (world.entities.size() < Config.ENTITY_LIMIT) world.addEntity(e);
				}
				world.spawnList.clear();
				
				world.sortEntities();
			}
			
			viewport.update(game);
			viewport.draw(world);
			
			//println(world.entities.size());
		}		
		
		// Draw GUI
		gui.update(game);
		gui.show(game);
		
		// TEMP
		//  println(frameRate);
	}


	public void loadWorld() {
		if (world == null) {
			world = new World(this, seed, 1);
			int loadTime = millis() - loadStart;
			float loadSecs = loadTime / 1000f;
			println("Done loading world in " + loadSecs + "s");
			int gx = Config.WORLD_SIZE / 2;
			int gy = Config.WORLD_SIZE / 2;
			world.spawnList.add(new StructAbode(world, gx + 10, gy, 2));
			world.spawnList.add(new ActorHuman(world, gx, gy));
		}
		game.setState(GameState.IN_GAME);
	}
	
	// Detect key presses, pan if arrow keys pressed
	public void keyPressed() {
		InputHandler.handleKeyPress(keyCode);
	}
	
	
	// Zoom with mouse wheel
	public void mouseWheel(MouseEvent event) {
	  float e = event.getCount();
	  game.zoom((float) (e * 0.1));
	}
	
	public void mouseClicked(MouseEvent event) {
		
		if (event.getButton() == LEFT) {
			List<GUIElement> elems = gui.getHoveredChildren();
			if (elems.size() > 0) {
				GUIElement e = elems.get(elems.size() - 1);
				PApplet.println(e);
				if (e instanceof GUIButton) {
					((GUIButton)e).click();
				}
				return;
			}
			

			if (game.getState() == GameState.IN_GAME) {
				List<Entity> ents = world.getEntitiesUnderMouse(viewport.screenToGridX(mouseX), viewport.screenToGridY(mouseY));
				if (ents.size() > 0) {
					Entity ent = ents.get(0);
					game.setSelectedEntity(ent);
					return;
				}
			}

		} else {
			if (game.getState() == GameState.IN_GAME) {
				int gx = viewport.screenToGridX(mouseX);
				int gy = viewport.screenToGridY(mouseY);
				int id = gy * Config.WORLD_SIZE + gx;
				println(gx, gy, id);
			}
		}
		
	}
	
	public void mouseMoved() {
		List<GUIElement> elems = gui.getHoveredChildren();
		if (elems.size() > 0) {
			GUIElement e = elems.get(elems.size() - 1);
			game.setHoveredElement(e);
			return;
		}
		game.setHoveredElement(null);
	}

}
