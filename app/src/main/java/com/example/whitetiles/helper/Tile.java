package com.example.whitetiles.helper;

import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;

public class Tile extends Rectangle{
    public Tile(int left, int top, int width, int height){
        this.x = left;
        this.y = top;
        this.width = width;
        this.height = height;
    }

    public Rect getGraphicRect(){
        return new Rect(x, y, x + width, y + height);
    }

    public void moveDown(int speed){
        this.y += speed;
    }
}
