package com.fergusbentley.asproj.util;

public class Colour {

	public static int RED = colour(158, 0, 5);
	public static int YELLOW = colour(196, 179, 0);
	public static int GREEN = colour(0, 130, 0);
	public static int CYAN = colour(0, 198, 185);
	public static int BLUE = colour(33, 56, 130);
	public static int REGAL_BLUE = colour(24, 56, 86);
	public static int MAGENTA = colour(183, 51, 159);
	public static int BLACK = grey(0);
	public static int WHITE = grey(255);
	public static int TRANSPARENT = 16777215;

	public int r, g, b, a;
	public int col;
	
	public Colour (int r, int g, int b, int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.col = Colour.colour(r, g, b);
	}
	
	// Generate colour based on a grey-scale value 0-255
	public static final int grey(int grey) {
		if (grey > 255)
			grey = 255;
		else if (grey < 0)
			grey = 0;
		return 0xff000000 | (grey << 16) | (grey << 8) | grey;
	}

	// Float equiv. of above
	public static final int grey(float fgrey) {
		int grey = (int) fgrey;
		if (grey > 255)
			grey = 255;
		else if (grey < 0)
			grey = 0;
		return 0xff000000 | (grey << 16) | (grey << 8) | grey;
	}

	// grey-scale colour with opacity
	public static final int grey(int grey, int alpha) {
		if (alpha > 255)
			alpha = 255;
		else if (alpha < 0)
			alpha = 0;
		if (grey > 255) {
			// then assume this is actually a #FF8800
			return (alpha << 24) | (grey & 0xFFFFFF);
		} else {
			// if (grey > 255) grey = 255; else if (grey < 0) grey = 0;
			return (alpha << 24) | (grey << 16) | (grey << 8) | grey;
		}
	}

	public static final int grey(float fgrey, float falpha) {
		int grey = (int) fgrey;
		int alpha = (int) falpha;
		if (grey > 255)
			grey = 255;
		else if (grey < 0)
			grey = 0;
		if (alpha > 255)
			alpha = 255;
		else if (alpha < 0)
			alpha = 0;
		return (alpha << 24) | (grey << 16) | (grey << 8) | grey;
	}

	// RGB colour as a single integer
	public static final int colour(int r, int g, int b) {
		if (r > 255)
			r = 255;
		else if (r < 0)
			r = 0;
		if (g > 255)
			g = 255;
		else if (g < 0)
			g = 0;
		if (b > 255)
			b = 255;
		else if (b < 0)
			b = 0;

		// Pretend alpha is 255, most significant 8 bits are alpha, then red,
		// etc.
		return 0xff000000 | (r << 16) | (g << 8) | b;
	}

	// RGBA colour as a single integer
	public static final int colour(int r, int g, int b, int a) {
		if (a > 255)
			a = 255;
		else if (a < 0)
			a = 0;
		if (r > 255)
			r = 255;
		else if (r < 0)
			r = 0;
		if (g > 255)
			g = 255;
		else if (g < 0)
			g = 0;
		if (b > 255)
			b = 255;
		else if (b < 0)
			b = 0;

		// Most significant 8 bits are alpha, then r, g, b
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static final int colour(float r, float g, float b) {
		if (r > 255)
			r = 255;
		else if (r < 0)
			r = 0;
		if (g > 255)
			g = 255;
		else if (g < 0)
			g = 0;
		if (b > 255)
			b = 255;
		else if (b < 0)
			b = 0;

		return 0xff000000 | ((int) r << 16) | ((int) g << 8) | (int) b;
	}

	public static final int colour(float r, float g, float b, float a) {
		if (a > 255)
			a = 255;
		else if (a < 0)
			a = 0;
		if (r > 255)
			r = 255;
		else if (r < 0)
			r = 0;
		if (g > 255)
			g = 255;
		else if (g < 0)
			g = 0;
		if (b > 255)
			b = 255;
		else if (b < 0)
			b = 0;

		return ((int) a << 24) | ((int) r << 16) | ((int) g << 8) | (int) b;
	}

	// TODO: VERY slow, needs work
	public static float[] HSBtoRGB(float hue, float sat, float bri) {
		float[] rgb = new float[3];

		float h = hue / 255f;
		float s = sat / 255f;
		float v = bri / 255f;

		float r = 0, g = 0, b = 0;

		float i = (float) Math.floor(h * 6);
		float f = h * 6 - i;
		float p = v * (1 - s);
		float q = v * (1 - f * s);
		float t = v * (1 - (1 - f) * s);

		float j = i % 6;
		if (j == 0) {
			r = v;
			g = t;
			b = p;
		} else if (j == 1) {
			r = q;
			g = v;
			b = p;
		} else if (j == 2) {
			r = p;
			g = v;
			b = t;
		} else if (j == 3) {
			r = p;
			g = q;
			b = v;
		} else if (j == 4) {
			r = t;
			g = p;
			b = v;
		} else if (j == 5) {
			r = v;
			g = p;
			b = q;
		}

		rgb[0] = r * 255;
		rgb[1] = g * 255;
		rgb[2] = b * 255;

		return rgb;
	}

	// See above
	public static int colourHSB(float h, float s, float b) {
		float[] rgb = HSBtoRGB(h, s, b);
		return colour(rgb[0], rgb[1], rgb[2]);
	}

	public final static int alpha(int rgb) {
		int a = (rgb >> 24) & 0xff;
		return a;
	}

}
