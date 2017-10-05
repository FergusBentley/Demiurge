package com.fergusbentley.asproj;

import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.entity.particle.ParticleSystem;
import com.fergusbentley.asproj.gui.GUIElement;

import processing.core.PApplet;
import processing.core.PVector;

public class GameState {

	private int gameState;
	
	private int time;
	private boolean paused;
	
	private float zoom;
	private float panX;
	private float panY;
	
	private GUIElement hoveredElement;
	private Entity selectedEntity;
	private ParticleSystem mouseEffect;
	
	public static final int MAIN_MENU = 0;
	public static final int START_LOAD = 1;
	public static final int LOAD_SCREEN = 2;
	public static final int START_GENERATION = 3;
	public static final int GENERATION_SCREEN = 4;
	public static final int IN_GAME = 5;
	
	public GameState() {
		this.setState(MAIN_MENU);
		this.setSelectedEntity(null);
		this.setZoom(Config.DEFAULT_ZOOM);
	}

	public Entity getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(Entity selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

	public int getState() {
		return gameState;
	}

	public void setState(int gameState) {
		this.gameState = gameState;
	}

	public float getZoom() {
		return this.zoom;
	}
	
	public void setZoom(float zoom) {
		this.zoom = zoom;
	}
	
	public float getScale() {
		return this.zoom * Config.TILE_SIZE;
	}

	public int time() {
		return this.time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}

	public void setPan(float panX, float panY) {
		this.panX = panX;
		this.panY = panY;
	}
	
	public PVector getPan() {
		return new PVector(this.panX, this.panY);
	}

	public ParticleSystem getMouseEffect() {
		return mouseEffect;
	}

	public void setMouseEffect(ParticleSystem mouseEffect) {
		this.mouseEffect = mouseEffect;
	}

	public boolean isPaused() {
		return paused;
	}

	public void pause() {
		this.paused = true;
	}
	
	public void resume() {
		this.paused = false;
	}
	
	public void togglePaused() {
		this.paused = !this.paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;		
	}

	public GUIElement getHoveredElement() {
		return hoveredElement;
	}
	
	public void setHoveredElement(GUIElement hoveredElement) {
		this.hoveredElement = hoveredElement;
	}

	// Adjust the apparent size of a grid square
	public void zoom(float amount) {
		zoom = PApplet.constrain(zoom - amount, Config.MIN_ZOOM, Config.MAX_ZOOM);
	}
	
	// Offset the viewport
	public void pan(int ax, int ay) {
		this.panX += ax * (0.5 / zoom);
		this.panY += ay * (0.5 / zoom);
	}
	
}
