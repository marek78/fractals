package com.szajna.fractals.model;

import java.util.ArrayList;

import com.szajna.util.Toolbox;

public class Gradient {

	private ArrayList<GradientColor> colors;
	
	public Gradient() {
		colors = new ArrayList<GradientColor>();
	}
	
	public void clear() {
		colors.clear();
	}
	
	public void addColor(GradientColor color) {
		
		int insertIndex = colors.size();
		for (int i = 0; i < colors.size(); ++i) {
			if (color.getPosition() < colors.get(i).getPosition()) {
				insertIndex = i;
				break;
			}
		}
		colors.add(insertIndex, color);
	}
	
	public boolean isEmpty() {
		return colors.isEmpty();
	}
	
	public int getColorsCount() {
		return colors.size();
	}
	
	public GradientColor getColor(int index) {
		return colors.get(index);
	}
	
	public int getColorAtPosition(float position) {
		
		if (! Toolbox.isInRange(position, 0.f, 1.f))
			throw new IllegalArgumentException(
					"Gradient.getColorAtPosition: position must be in range [0.0;1.0]");

		int colorIndex = colors.size() - 1;
		for (int i = 0; i < colors.size(); ++i) {
			if (position < colors.get(i).getPosition()) {
				colorIndex = i;
				break;
			}
		}

		GradientColor c1 = colors.get(colorIndex - 1);
		GradientColor c2 = colors.get(colorIndex);
		
		float segmentLength = c2.getPosition() - c1.getPosition();
		
		if (segmentLength == 0.0f) {
			
			float r = c1.getR();
			float g = c1.getG();
			float b = c1.getB();
			return Toolbox.getColor(r, g, b);
			
		} else {
			
			float posDistance = position - c1.getPosition();
			float coeff = posDistance / segmentLength;
			
			float r = c1.getR() + (c2.getR() - c1.getR()) * coeff;
			float g = c1.getG() + (c2.getG() - c1.getG()) * coeff;
			float b = c1.getB() + (c2.getB() - c1.getB()) * coeff;
			return Toolbox.getColor(r, g, b);
		}
	}
}
