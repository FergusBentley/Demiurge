package com.fergusbentley.asproj.entity.living;

import com.fergusbentley.asproj.Resources;
import com.fergusbentley.asproj.ai.BehaviourFlocking;
import com.fergusbentley.asproj.util.Colour;
import com.fergusbentley.asproj.util.Util;
import com.fergusbentley.asproj.world.World;

import processing.core.PConstants;

public class ActorSheep extends Actor implements PConstants { 
	
	int[] left, right;
	int woolColour;
	
	public ActorSheep(World w, int x, int y) {
		super(w, x, y, Resources.get("animal/sheep"), 2, 1);
		this.behaviour = new BehaviourFlocking(this, "sheep");
		this.woolColour = Colour.grey(Util.lerp(230, 170, (float)Math.random()));
		int[] r = {this.woolColour, this.texture[0]};
		int[] l = {this.texture[0], this.woolColour};
		this.right = r;
		this.left = l;
		this.texture = this.right;
	}
	
	public void facing(int dir) {
		if (dir == -1) {
			this.texture = this.left;
		} else {
			this.texture = this.right;
		}
	}

}
