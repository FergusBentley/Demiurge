package com.fergusbentley.asproj.crafting;

import java.util.Arrays;
import java.util.List;

import com.fergusbentley.asproj.entity.Entity;

public class Recipe {

	public List<MaterialStack> required;
	public Class<? extends Entity> result;
	
	public Recipe(Class<? extends Entity> res, MaterialStack... req) {
		this.result = res;
		this.required = Arrays.asList(req);
	}
	
}
