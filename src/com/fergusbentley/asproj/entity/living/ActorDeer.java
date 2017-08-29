package com.fergusbentley.asproj.entity.living;

import com.fergusbentley.asproj.Resources;
import com.fergusbentley.asproj.ai.BehaviourFlocking;
import com.fergusbentley.asproj.util.Util;
import com.fergusbentley.asproj.world.World;

import processing.core.PConstants;

public class ActorDeer extends Actor implements PConstants { 
	
	private int skintone;
	
	public ActorDeer(World w, int x, int y) {
		super(w, x, y, new int[1], 1, 1, 1, 1);
		this.behaviour = new BehaviourFlocking(this, "deer");
		this.skintone = Resources.getFromMap("animal/deertones", (int) Math.floor(Util.randInt(0, 16)));
		this.texture[0] = this.skintone;
	}

}
