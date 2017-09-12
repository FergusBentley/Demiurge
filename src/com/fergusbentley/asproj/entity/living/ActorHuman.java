package com.fergusbentley.asproj.entity.living;

import com.fergusbentley.asproj.Resources;
import com.fergusbentley.asproj.ai.BehaviourHuman;
import com.fergusbentley.asproj.util.Colour;
import com.fergusbentley.asproj.util.Util;
import com.fergusbentley.asproj.world.World;

public class ActorHuman extends Actor {

	String faction;
	String name;
	
	int skintone;
	
	public ActorHuman(World w, int x, int y) {
		super(w, x, y, new int[2], 1, 2, 1, 1);
		this.behaviour = new BehaviourHuman(this);
		this.skintone = Resources.getFromMap("animal/skintones", (int) Math.floor(Util.randInt(0, 116)));
		this.texture[0] = this.skintone;
		this.texture[1] = Colour.RED;
	}
	
	public Float getStat(String s) {
		BehaviourHuman b = (BehaviourHuman) this.behaviour;
		return b.stats.get(s);
	}

}
