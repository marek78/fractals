package com.szajna.fractals.model;

import com.szajna.util.Toolbox;

public class GradientColor {
	
	/** 0.0 - 1.0 */
	private float r;
	private float g;
	private float b;
	private float position;
	
	public GradientColor(float r, float g, float b, float position) {
		
		if (!Toolbox.isInRange(r, 0.f, 1.f) || 
			!Toolbox.isInRange(g, 0.f, 1.f) || 
			!Toolbox.isInRange(b, 0.f, 1.f) || 
			!Toolbox.isInRange(position, 0.f, 1.f))
			throw new IllegalArgumentException(
					"GradientColor: r,g,b and position must be in range [0.0;1.0]");
		
		this.r = r;
		this.g = g;
		this.b = b;
		this.position = position;
	}

	public float getR() {
		return r;
	}

	public float getG() {
		return g;
	}

	public float getB() {
		return b;
	}

	public float getPosition() {
		return position;
	}
}

