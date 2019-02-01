package com.fxquery;


import javafx.scene.shape.Rectangle;

public class QueryHelper extends Rectangle {

    private double originalWidth, originalHeight;

    protected QueryHelper(double originalWidth,double originalHeight,double w, double h){
        super(w,h);
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }

    protected QueryHelper(double originalWidth,double originalHeight){
        super(originalWidth,originalHeight);
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }

    public double getOriginalWidth() {
        return originalWidth;
    }

    public double getOriginalHeight() {
        return originalHeight;
    }
}
