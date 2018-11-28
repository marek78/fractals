package com.szajna.fractals.model;

public class FractalWorker implements Runnable {

    private FractalModel model;
    private int startX;
    private int endX;
    
    public FractalWorker(FractalModel model, int startX, int endX) {
        
        this.model = model;
        this.startX = startX;
        this.endX = endX;
    }
    
    @Override
    public void run() {
        model.calculate(startX, endX);
    }
}
