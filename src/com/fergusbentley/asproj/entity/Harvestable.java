package com.fergusbentley.asproj.entity;

import com.fergusbentley.asproj.crafting.MaterialStack;

public interface Harvestable {
	
	public MaterialStack harvest();
	
	public boolean isHarvestable();
}
