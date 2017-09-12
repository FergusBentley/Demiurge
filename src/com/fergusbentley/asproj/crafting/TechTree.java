package com.fergusbentley.asproj.crafting;

import java.util.HashMap;
import java.util.Map;

import com.fergusbentley.asproj.entity.structure.StructAbode;

public class TechTree {
	
	// TODO: Use an actual tech tree with requirements
	
	Map<String, Recipe> recipes;
	
	public TechTree() {
		this.recipes = new HashMap<String, Recipe>();
		
		addRecipe("abode1", new Recipe(StructAbode.class, new MaterialStack(Material.WOOD, 50)));
	}
	
	public void addRecipe(String name, Recipe recipe) {
		this.recipes.put(name, recipe);
	}
	
	public Recipe getRecipe(String name) {
		return this.recipes.get(name);
	}
	
}
