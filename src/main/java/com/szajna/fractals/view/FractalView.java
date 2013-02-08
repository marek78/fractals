package com.szajna.fractals.view;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.szajna.fractals.model.Gradient;
import com.szajna.fractals.model.GradientColor;
import com.szajna.util.Toolbox;

public class FractalView extends JPanel {
	
	private static final int PREFERRED_WIDTH = 1024;
	private static final int PREFERRED_HEIGHT = 768;
	
	public enum ColorTheme {
		
		GRADIENT1,
		GRADIENT2,
		GRADIENT3,
		HSB,
		GRAYSCALE,
		VIOLET_SCHADES,
		//CUSTOM,
	};
	
	private static final long serialVersionUID = 1L;
	
	private BufferedImage canvas;
	private boolean canvasDirty;	// needs rebuilding from the fractal data if true
	
	private int[][] fractalData;
	private int iterationsCount;
	
	private int offsetX = 0;
	private int offsetY = 0;
	
	private Rectangle zoomRectangle = new Rectangle();
	private ColorTheme colorTheme;
	
	private Gradient gradientGreenTanOrangeKhaki;
	private Gradient gradientGreenBeigeRedBeige;
	private Gradient gradientRedOrangeBlue;
	//private Gradient gradientCustom;
	
	public FractalView(ColorTheme colorTheme) {
		
		canvas = null;
		canvasDirty = true;
		
		fractalData = new int[0][0];
		this.colorTheme = colorTheme;
		
		this.gradientGreenTanOrangeKhaki = new Gradient();
		this.gradientGreenTanOrangeKhaki.addColor(new GradientColor( 45/255.f,  62/255.f,  43/255.f, 0.000f));
		this.gradientGreenTanOrangeKhaki.addColor(new GradientColor(143/255.f, 186/255.f, 140/255.f, 0.125f));
		this.gradientGreenTanOrangeKhaki.addColor(new GradientColor( 62/255.f,  58/255.f,  46/255.f, 0.250f));
		this.gradientGreenTanOrangeKhaki.addColor(new GradientColor(208/255.f, 178/255.f, 140/255.f, 0.375f));
		this.gradientGreenTanOrangeKhaki.addColor(new GradientColor( 66/255.f,  54/255.f,  40/255.f, 0.500f));
		this.gradientGreenTanOrangeKhaki.addColor(new GradientColor(202/255.f, 132/255.f,  62/255.f, 0.625f));
		this.gradientGreenTanOrangeKhaki.addColor(new GradientColor( 66/255.f,  56/255.f,  44/255.f, 0.750f));
		this.gradientGreenTanOrangeKhaki.addColor(new GradientColor(237/255.f, 230/255.f, 139/255.f, 0.875f));
		this.gradientGreenTanOrangeKhaki.addColor(new GradientColor( 59/255.f,  61/255.f,  47/255.f, 1.000f));

		this.gradientGreenBeigeRedBeige = new Gradient();
		this.gradientGreenBeigeRedBeige.addColor(new GradientColor( 44/255.f,  60/255.f,  47/255.f, 0.000f));
		this.gradientGreenBeigeRedBeige.addColor(new GradientColor( 33/255.f, 135/255.f,  33/255.f, 0.125f));
		this.gradientGreenBeigeRedBeige.addColor(new GradientColor( 59/255.f,  63/255.f,  46/255.f, 0.250f));
		this.gradientGreenBeigeRedBeige.addColor(new GradientColor(240/255.f, 238/255.f, 213/255.f, 0.375f));
		this.gradientGreenBeigeRedBeige.addColor(new GradientColor( 65/255.f,  44/255.f,  43/255.f, 0.500f));
		this.gradientGreenBeigeRedBeige.addColor(new GradientColor(167/255.f,  39/255.f,  40/255.f, 0.625f));
		this.gradientGreenBeigeRedBeige.addColor(new GradientColor( 60/255.f,  47/255.f,  39/255.f, 0.750f));
		this.gradientGreenBeigeRedBeige.addColor(new GradientColor(240/255.f, 238/255.f, 213/255.f, 0.875f));
		this.gradientGreenBeigeRedBeige.addColor(new GradientColor( 54/255.f,  51/255.f,  46/255.f, 1.000f));

		this.gradientRedOrangeBlue = new Gradient();
		this.gradientRedOrangeBlue.addColor(new GradientColor(0.835f, 0.00f, 0.00f, 0.0f));
		this.gradientRedOrangeBlue.addColor(new GradientColor(0.84f,  0.40f, 0.12f, 0.5f));
		this.gradientRedOrangeBlue.addColor(new GradientColor(0.19f,  0.27f, 0.52f, 1.0f));

		this.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
	}
	
	public BufferedImage getCanvas() {
		return canvas;
	}
	
	public BufferedImage renderImage(int[][] fractalData, int iterationsCount) {
		
		if (fractalData == null)
			throw new IllegalArgumentException(
					"FractalView.BufferedImage: fractalData must not be null");
		
		BufferedImage img = new BufferedImage(fractalData.length, fractalData[0].length, 
				BufferedImage.TYPE_INT_RGB);
		
		// render the image
		for (int x = 0; x < fractalData.length; ++x) {
			for (int y = 0; y < fractalData[x].length; ++y) {
				img.setRGB(x, y, getColor(fractalData[x][y], iterationsCount));
			}
		}
		return img;
	}
	
	public void setSize(int width, int height) {
		
		if (width < 1 || height < 1)
			canvas = null;
		else
			canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	public void setOffsets(int offsetX, int offsetY) {
		
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	public void setFractalData(int[][] fractalData, int iterationsCount) {
		
		this.fractalData = fractalData;
		this.iterationsCount = iterationsCount;
		this.canvasDirty = true;
	}
	
	public void setZoomRectangle(Rectangle rect) {
		zoomRectangle = rect;
	}
	
	public void setColorTheme(ColorTheme colorTheme) {
		this.colorTheme = colorTheme;
		this.canvasDirty = true;
	}
	
	@Override
	public void paint(Graphics g) {

		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setBackground(Color.BLACK);
		g2d.clearRect(0, 0, getWidth(), getHeight());
		//System.out.println("paint: " + getWidth() + ", " + getHeight());
		
		g2d.translate(-offsetX, -offsetY);
		drawMandlebrot(g2d);
		
		if (! zoomRectangle.isEmpty()) {

			// fill zoom rectangle
			g2d.setColor(new Color(0x40, 0x60, 0xB0, 0x80));
			g2d.fill(zoomRectangle);

			float thickness = 1;
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(thickness));

			// draw border
			g2d.setColor(new Color(0x80, 0xA0, 0xB0, 0x80));
			g2d.draw(zoomRectangle);
			
			g2d.setStroke(oldStroke);
		}
	}
	
	private void drawMandlebrot(Graphics2D g2d) {

		if (canvas == null)
			return;
		
		//long drawStartTime = System.currentTimeMillis();
		if (canvasDirty) {
			
			// build the canvas from fractal data
			for (int x = 0; x < fractalData.length; ++x) {
				for (int y = 0; y < fractalData[x].length; ++y) {
					canvas.setRGB(x, y, getColor(fractalData[x][y], iterationsCount));
				}
			}
			canvasDirty = false;
		}
		
		g2d.drawImage(canvas, null, 0, 0);
		//System.out.println("Drawing time: " + (System.currentTimeMillis() - drawStartTime) + "ms");
	}

	private int getColor(float value1, int value2) {

		float lum = (value1) / value2;
		if (lum < 0.f)
			lum = 0.f;
		else if (lum > 1.0f)
			lum = 1.0f;
		
		switch (colorTheme) {

		default:
		case GRADIENT1:
			return gradientGreenTanOrangeKhaki.getColorAtPosition(lum);

		case GRADIENT2:
			return gradientGreenBeigeRedBeige.getColorAtPosition(lum);			

		case GRADIENT3:
			return gradientRedOrangeBlue.getColorAtPosition(lum);
			
		case HSB:
			return Color.HSBtoRGB(lum, 1.0f, 1.0f);

		case GRAYSCALE: {
			int lumInt = (int)(lum * 255.0f);
			return Toolbox.getColor(lumInt, lumInt, lumInt);
		}
		
		case VIOLET_SCHADES: {
			float lumQuadrat = lum * lum;
			int lumInt = (int)(lum * 255.0f);
			int lumQuadratInt = (int)(lumQuadrat * 255.0f);
			return Toolbox.getColor(lumQuadratInt, lumQuadratInt, lumInt);
		}
		}

		//Color hsb = new Color(Color.HSBtoRGB(lum, 1.0f, 1.0f));
//		Color rgb = new Color(lum * lum, lum * lum, lum);
//		
//		Color c = new Color(
//				(int)Math.sqrt(hsb.getRed()   * rgb.getRed()),
//				(int)Math.sqrt(hsb.getGreen() * rgb.getGreen()), 
//				(int)Math.sqrt(hsb.getBlue()  * rgb.getBlue())
//				);
//		return Color.HSBtoRGB(value1 / value2, 0.9f, 0.9f);
//		return (int)(255 * value1 / value2);
	}
}

