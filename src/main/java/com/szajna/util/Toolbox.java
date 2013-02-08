package com.szajna.util;


public class Toolbox {

	public static int getColor(float r, float g, float b) {
		return 0xff000000 | ((int)(r * 255.f) << 16) | ((int)(g * 255.f) << 8) | (int)(b * 255.f);
	}

	public static int getColor(int r, int g, int b) {
		return 0xff000000 | (r  << 16) | (g << 8) | b;
	}
	
	public static boolean isInRange(float value, float min, float max) {
		return (value >= min && value <= max);
	}
}
