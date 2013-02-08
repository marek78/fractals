package com.szajna.fractals.model;

import com.szajna.fractals.App;

public class HelpModel {

	public HelpModel() {
	}
	
	public String getText() {
		return "<HTML>" +
				"<div align=\"center\">" +
				"<H2>" + App.APP_NAME_SPACED + "</H2>" +
				"<H4>version " + App.APP_VERSION + "</H4>" +
				"<H4>Controls:</H4>" +
				"</div>" +
				"<ul>" +
				"<li>Panning - move the mouse with left button clicked &nbsp;&nbsp;</li>" +
				"<li>Zooming - move the mouse with right button clicked &nbsp;&nbsp;</li>" +
				"<li>Alternative zooming - use mouse scroll wheel &nbsp;&nbsp;</li>" +
				"<li>Reset view - click center mouse button &nbsp;&nbsp;</li>" +
				"</ul>" + 
				"</HTML>";
	}
}
