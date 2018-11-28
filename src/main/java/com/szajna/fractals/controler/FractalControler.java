package com.szajna.fractals.controler;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.szajna.fractals.model.FractalModel;
import com.szajna.fractals.view.FractalView;
import com.szajna.util.Log;

public class FractalControler 
    implements MouseListener, MouseMotionListener, MouseWheelListener, 
    ComponentListener, ChangeListener {

    private static final String LOG_TAG = FractalControler.class.getSimpleName();
    private static final int ZOOM_RECTANGLE_DIAGONAL_MIN_SIZE = 10; // pixels
    private static final double DEFAULT_ZOOM_FACTOR = 1.5;
    
    private FractalModel model;
    private FractalView view;
    private Point pressPoint = new Point();
    private int pressedButton = 0;
    private int width = 0;
    private int height = 0;

    
    public FractalControler(FractalModel fractalModel, FractalView fractalView) {
        
        this.model = fractalModel;
        this.view = fractalView;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        
        Point p = e.getPoint();
        if (pressedButton == MouseEvent.BUTTON1) {
            
            view.setOffsets(pressPoint.x - p.x, pressPoint.y - p.y);
            view.repaint();
            
        } else if (pressedButton == MouseEvent.BUTTON3) {
            
            int width = Math.abs(pressPoint.x - p.x);
            int height = Math.abs(pressPoint.y - p.y);
            
            int left = p.x < pressPoint.x ? p.x : pressPoint.x;
            int top  = p.y < pressPoint.y ? p.y : pressPoint.y;
            
            double diagonal = Math.sqrt(width * width + height * height);
            if (diagonal >= ZOOM_RECTANGLE_DIAGONAL_MIN_SIZE) {
                view.setZoomRectangle(new Rectangle(left, top, width, height));
            } else {
                view.setZoomRectangle(new Rectangle());
            }
            view.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressPoint = e.getPoint();
        pressedButton = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        Point releasePoint = e.getPoint();
        if (pressedButton == MouseEvent.BUTTON1) {

            translate(pressPoint.x, pressPoint.y, releasePoint.x, releasePoint.y);
            
        } else if (pressedButton == MouseEvent.BUTTON3) {

            int zoomRectWidth = Math.abs(pressPoint.x - releasePoint.x);
            int zoomRectHeight = Math.abs(pressPoint.y - releasePoint.y);
            
            double diagonal = Math.sqrt(zoomRectWidth * zoomRectWidth + zoomRectHeight * zoomRectHeight);
            if (diagonal >= ZOOM_RECTANGLE_DIAGONAL_MIN_SIZE) {
                
                int centerX = Math.min(pressPoint.x, releasePoint.x) + zoomRectWidth / 2;
                int centerY = Math.min(pressPoint.y, releasePoint.y) + zoomRectHeight / 2;
                
                translate(centerX, centerY, width / 2, height / 2);
                
                double zoomFactor = Math.max(
                        zoomRectWidth / (double)width, 
                        zoomRectHeight / (double)height);
                
                Log.v(LOG_TAG, "zoomFactor: " + zoomFactor);
                zoom(zoomFactor);
            }

        } else if (pressedButton == MouseEvent.BUTTON2) {
            
            model.setToDefaults();
        }
        
        model.calculate();
        view.setOffsets(0, 0);
        view.setZoomRectangle(new Rectangle());
        view.setFractalData(model.getFractalData(), model.getIterationsCount());
        view.repaint();
    }

    
    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        
        width = e.getComponent().getWidth();
        height = e.getComponent().getHeight();
        Log.d(LOG_TAG, "componentResized: " + width + ", " + height);
        
        if (model.getWidth() != width || model.getHeight() != height) {
            
            model.setSize(width, height);
            model.calculate();
        }
        view.setSize(width, height);
        view.setOffsets(0, 0);
        view.setZoomRectangle(new Rectangle());
        view.setFractalData(model.getFractalData(), model.getIterationsCount());
        view.repaint();
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        
        Point currentPos = e.getPoint();
        int wheelRotation = e.getWheelRotation();

        double zoomFactor = wheelRotation < 0 ? 1.0 / DEFAULT_ZOOM_FACTOR : DEFAULT_ZOOM_FACTOR;
        
        translate(currentPos.x, currentPos.y, width / 2, height / 2);
        zoom(zoomFactor);
        // translate back
        translate(width / 2, height / 2, currentPos.x, currentPos.y);
        
        model.calculate();
        view.setOffsets(0, 0);
        view.setZoomRectangle(new Rectangle());
        view.setFractalData(model.getFractalData(), model.getIterationsCount());
        view.repaint();
    }
    
    /**
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    private void translate(int x1, int y1, int x2, int y2) {
        
        if (width > 0 && height > 0) {
            
            double dx = model.getZoomX() * (x2 - x1) / (double)width;
            double dy = model.getZoomY() * (y2 - y1) / (double)height;
            model.setCenterCx(model.getCenterCx() - dx);
            model.setCenterCy(model.getCenterCy() - dy);
        }
    }
    
    /**
     * 
     * @param zoomFactor
     */
    private void zoom(double zoomFactor) {

        model.setZoom(model.getZoom() * zoomFactor);
    }

    /**
     * ChangeListener interface implementation.
     */
    @Override
    public void stateChanged(ChangeEvent e) {

        Object source = e.getSource();
        if (source instanceof JSlider) {
            
            JSlider slider = (JSlider)source;
            if (!slider.getValueIsAdjusting()) {
                
                int iterationsCount = slider.getValue();
                if (model.getIterationsCount() != iterationsCount) {
                    
                    model.setIterationsCount(iterationsCount);
                    model.calculate();
                    view.setOffsets(0, 0);
                    view.setZoomRectangle(new Rectangle());
                    view.setFractalData(model.getFractalData(), model.getIterationsCount());
                    view.repaint();
                    Log.v(LOG_TAG, "" + slider.getValue());
                }
            }
        }
    }
}
