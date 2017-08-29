package com.fergusbentley.asproj.world;

import java.io.Serializable;

public enum Weather implements Serializable {

	RAIN(2, 1), SNOW(4, 1), STORM(2, 1), MILD(1, 1), HEAT(1, 1.5f);
	
	public float exposureModifier, thirstModifier;
	
	Weather(float exp, float thi) {
		this.exposureModifier = exp;
		this.thirstModifier = thi;
	}
}
