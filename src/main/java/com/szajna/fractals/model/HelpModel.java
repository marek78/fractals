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
                "</div>" +
                
                "<div align=\"left\">" +
                "<pre>" +
                "    Mouse controls:    <br>" +
                "    Panning - move the mouse with left button pressed    <br>" +
                "    Zooming - move the mouse with right button pressed    <br>" +
                "    Zooming (alternative) - use mouse scroll wheel    <br>" +
                "    Reset view - click center mouse button    <br>" +
                "<br>" +
                "</pre>" +
                "</div>" +
                
                "<div align=\"left\">" +
                "<pre>" +
                "    Keyboard controls:    <br>" +
                "    Panning: left, right, up, down arrow keys    <br>" +
                "    Zooming (at screen  center): 'G' zoom in, 'H' zoom out    <br>" +
                "    Zooming (at mouse position): 'Q' or ',' zoom in, 'W' or '.' zoom out    <br>" +
                "    Reset view - 'F1' or 'Home' key    <br>" +
                "<br>" +
                "</pre>" +
                "</div>" +
                
                "</HTML>";
    }
}
