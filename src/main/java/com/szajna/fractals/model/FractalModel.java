package com.szajna.fractals.model;

import com.szajna.util.Log;

public class FractalModel {
    
    private static final String LOG_TAG = FractalModel.class.getSimpleName();

    public static final int ITERATIONS_COUNT_MIN = 100;
    public static final int ITERATIONS_COUNT_MAX = 600;
    public static final int ITERATIONS_COUNT_DEFAULT = 300;
    
    private final static int THREADS_COUNT = 4;
    private final static double MAX_BETRAG_QUADRAT = 16.0;
    
    private int[][] fractalData;

    private int width = 0;
    private int height = 0;
    private int iterationsCount;

    private double centerCx;
    private double centerCy;
    private double zoom;

    
    /**
     * Constructs fractal model object.
     * @param width - fractal width.
     * @param height - fractal height.
     */
    public FractalModel() {
        setToDefaults();
        setSize(0, 0);
        iterationsCount = ITERATIONS_COUNT_DEFAULT;
    }
    
    /**
     * Creates fractal taking parameters from another model.
     * @param width - fractal width in pixels.
     * @param height - fractal height in pixels.
     * @param other - other model, must be != null.
     */
    public FractalModel(int width, int height, FractalModel other) {
        
        this.width = other.width;
        this.height= other.height;
        this.iterationsCount = other.iterationsCount;
        this.centerCx = other.centerCx;
        this.centerCy = other.centerCy;
        this.zoom = other.zoom;
        setSize(width, height);
    }
    
    /**
     * Sets zoomX, zoomY, minCx, minCy and iterationsCount to default values.
     */
    public void setToDefaults() {
        
        centerCx = -0.50;
        centerCy =  0.00;
        zoom = 6.0;
    }
    
    /**
     * Calculates fractal.
     */
    public void calculate() {
        
        // single thread implementation
        // calculate(0, width);
        Log.v(LOG_TAG, "cneter: " + centerCx + ", " + centerCy);

        // multi threaded calculation
        Thread [] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < threads.length; ++i) {
            
            int startX = i * width / THREADS_COUNT;
            int endX = (i + 1) * width / THREADS_COUNT;
            
            threads[i] = new Thread(new FractalWorker(this, startX, endX));
            threads[i].start();
        }

        try {
            for (int i = 0; i < threads.length; ++i)
                threads[i].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Calculates fractal - useful for dividing job on more threads.
     * @param startX
     * @param endX
     */
    public void calculate(int startX, int endX) { 

        double cx;
        double cy;

        double zoomX = getZoomX();
        double zoomY = getZoomY();
        
        for (int x = startX; x < endX; ++x) {
            
            cx = centerCx - zoomX / 2.0 + ((double)x / (width - 1)) * zoomX;
            for (int y = 0; y < height; ++y) {
                
                cy = centerCy - zoomY / 2.0 + ((double)y / (height - 1)) * zoomY;
                fractalData[x][y] = pointIteration(cx, cy, MAX_BETRAG_QUADRAT, iterationsCount);
            }
        }

    }

    /**
     * Gets fractal data.
     * @return fractal data.
     */
    public int[][] getFractalData() {
        return fractalData;
    }

    public void setSize(int width, int height) {
        
        if (width < 1)
            width = 0;
        if (height < 1)
            height = 0;
        
        fractalData = new int[width][height];

        this.width = width;
        this.height = height;
    }

    public void setIterationsCount(int iterationsCount) {
        this.iterationsCount = iterationsCount;
    }
    public void setCenterCx(double centerCx) {
        this.centerCx = centerCx;
    }
    public void setCenterCy(double centerCy) {
        this.centerCy = centerCy;
    }
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getIterationsCount() {
        return iterationsCount;
    }
    public double getCenterCx() {
        return centerCx;
    }
    public double getCenterCy() {
        return centerCy;
    }
    public double getZoom() {
        return zoom;
    }
    public double getZoomX() {
        
        double aspect = width / (double)height;
        return aspect > 1.0 ? zoom * aspect : zoom;
    }
    public double getZoomY() {

        double aspect = width / (double)height;
        return aspect > 1.0 ? zoom : zoom * 1.0 / aspect;
    }

    private int pointIteration(double cx, double cy, double maxBetragQuadrat, int maxIterations) {
        
        double betragQuadrat = 0;
        double x = 0;
        double y = 0;
        int iter = 0;
        
        double xt;
        double yt;
        
        double xQuadrat;
        double yQuadrat;
        
        while (betragQuadrat <= maxBetragQuadrat && iter < maxIterations) {
            
            xQuadrat = x * x;
            yQuadrat = y * y;
            
            xt = xQuadrat - yQuadrat + cx;
            yt = 2 * x * y + cy;
            x = xt;
            y = yt;
            
            betragQuadrat = xQuadrat + yQuadrat;
            ++iter;
        }
        //return iter - (float)(Math.log(Math.log(betragQuadrat) / Math.log(4)) / Math.log(2));
        return iter;
    }
}

