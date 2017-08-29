package com.fergusbentley.asproj.entity.structure;

import java.util.HashMap;
import java.util.Map;

import com.fergusbentley.asproj.entity.Entity;

public class TechTree {
	
	Map<Class<? extends Entity>, Map<Material, Integer>> recipes;
	
	public TechTree() {
		this.recipes = new HashMap<Class<? extends Entity>, Map<Material, Integer>>();
	}
	
	public void addRecipe(Class<? extends Entity> result, Map<Material, Integer> resources) {
		this.recipes.put(result, resources);
	}
	
}
