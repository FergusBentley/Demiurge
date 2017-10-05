package com.fergusbentley.asproj.powers;

import com.fergusbentley.asproj.world.World;

public abstract class Power {

	int cost;
	int rate;
	
	public Power (int cost, int rate) {
		this.cost = cost;
		this.rate = rate;
	}
	
	public abstract void startCasting(int x, int y, World w);
	
	public abstract void whileCasting(int x, int y, World w);
	
	public abstract void stopCasting(int x, int y, World w);
	
}
