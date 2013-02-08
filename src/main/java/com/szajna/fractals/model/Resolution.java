package com.szajna.fractals.model;

public class Resolution {

	public static final Resolution VGA 		  = new Resolution(640,  480,  "VGA");
	public static final Resolution SVGA 	  = new Resolution(800,  600,  "SVGA");
	public static final Resolution WSVGA 	  = new Resolution(1024, 600,  "WSVGA");
	public static final Resolution XGA 		  = new Resolution(1024, 768,  "XGA");
	public static final Resolution XGAPLUS    = new Resolution(1152, 864,  "XGA+");
	public static final Resolution WXGA_16_9  = new Resolution(1280, 720,  "WXGA");
	public static final Resolution WXGA_5_3   = new Resolution(1280, 768,  "WXGA");
	public static final Resolution WXGA_16_10 = new Resolution(1280, 800,  "WXGA");
	public static final Resolution SXGA_MINUS = new Resolution(1280, 960,  "SXGA-");
	public static final Resolution SXGA       = new Resolution(1280, 1024, "SXGA");
	public static final Resolution HD1        = new Resolution(1360, 768,  "HD");
	public static final Resolution HD2        = new Resolution(1366, 768,  "HD");
	public static final Resolution SXGA_PLUS  = new Resolution(1400, 1050, "SXGA+");
	public static final Resolution WXGA_PLUS  = new Resolution(1440, 900,  "WXGA+");
	public static final Resolution HD_PLUS    = new Resolution(1600, 900,  "HD+");
	public static final Resolution UXGA       = new Resolution(1600, 1200, "UXGA");
	public static final Resolution WSXGA_PLUS = new Resolution(1680, 1050, "WSXGA+");
	public static final Resolution FHD        = new Resolution(1920, 1080, "FHD");
	public static final Resolution WUXGA      = new Resolution(1920, 1200, "WUXGA");
	public static final Resolution QWXGA      = new Resolution(2048, 1152, "QWXGA");
	public static final Resolution WQHD       = new Resolution(2560, 1440, "WQHD");
	public static final Resolution WQXGA      = new Resolution(2560, 1600, "WQXGA");

	private static final Resolution RESOLUTION_TABLE[] = {
		VGA, 
		SVGA,
		WSVGA,
		XGA,
		XGAPLUS,
		WXGA_16_9,
		WXGA_5_3,
		WXGA_16_10,
		SXGA_MINUS,
		SXGA,
		HD1,
		HD2,
		SXGA_PLUS,
		WXGA_PLUS,
		HD_PLUS,
		UXGA,
		WSXGA_PLUS,
		FHD,
		WUXGA,
		QWXGA,
		WQHD,
		WQXGA,
		};
	
	private int width;
	private int height;
	private String acronym;

	private Resolution(int width, int height, String acronym) {
		this.width = width;
		this.height = height;
		this.acronym = acronym;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getAcronym() {
		return acronym;
	}
	
	public String toString() {
		return "" + width + "x" + height + "  (" + acronym + ")";
	}
	
	public static final Resolution fromString(String resolutionString) {
		
		if (resolutionString == null)
			throw new IllegalArgumentException("Resolution.fromString - arg null not allowed");

		int separatorIndex = resolutionString.indexOf('x');
		if (separatorIndex > -1) {
			
			try {
				
				StringBuilder sb = new StringBuilder();
				for (int i = separatorIndex + 1; i < resolutionString.length(); ++i) {
					
					char c = resolutionString.charAt(i);
					if (Character.isDigit(c))
						sb.append(c);
					else 
						break;
				}

				int w = Integer.parseInt(resolutionString.substring(0, separatorIndex));
				int h = Integer.parseInt(sb.toString());
				
				for (int i = 0; i < RESOLUTION_TABLE.length; ++i) {
					if (RESOLUTION_TABLE[i].getWidth() == w && RESOLUTION_TABLE[i].getHeight() == h) {
						return RESOLUTION_TABLE[i];
					}
				}
				
			} catch (NumberFormatException e) {
				System.err.println("Resolution.fromString - NumberFormatException for arg: " + resolutionString);
			}
		}
		return null;
	}
}

