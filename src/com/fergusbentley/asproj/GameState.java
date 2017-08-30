package com.fergusbentley.asproj;

import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.entity.particle.ParticleSystem;

import processing.core.PVector;

public class GameState {

	private int gameState;
	
	private int time;
	private boolean paused;
	
	private float zoom;
	private float panX;
	private float panY;
	
	private Entity selectedEntity;
	private ParticleSystem mouseEffect;
	
	public GameState() {
		this.setState(0);
		this.setSelectedEntity(null);
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
	
}
