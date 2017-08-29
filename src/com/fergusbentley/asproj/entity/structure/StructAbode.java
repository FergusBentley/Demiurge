package com.fergusbentley.asproj.entity.structure;

import java.util.ArrayList;
import java.util.List;

import com.fergusbentley.asproj.Resources;
import com.fergusbentley.asproj.ai.HumanBehaviour;
import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.entity.Interactable;
import com.fergusbentley.asproj.entity.living.Actor;
import com.fergusbentley.asproj.entity.living.ActorHuman;
import com.fergusbentley.asproj.world.World;

public class StructAbode extends Entity implements Interactable {

	private List<Actor> residents = new ArrayList<Actor>();
	
	public StructAbode(World w, int x, int y, int level) {
		super(w, x, y, Resources.getRandom("building/abode/" + level), BOTTOM, CENTER);
		this.colliderWidth = this.w - 2;
		this.colliderHeight = 2;
		this.updateCorners();
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void interact(ActorHuman cause) {
		this.residents.add(cause);
		HumanBehaviour hb = ((HumanBehaviour)cause.behaviour);
		hb.indoors = true;
		hb.abode = this;
	}
	
	public void exit(ActorHuman actor) {
		this.residents.remove(actor);
		HumanBehaviour hb = ((HumanBehaviour)actor.behaviour);
		hb.indoors = false;
		hb.abode = null;
	}

}
