package com.example.whitetiles.helper;

import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;

enum TileState {
    ACTIVE,
    DISABLED,
    HIGHLIGHTED
}

public class Tile extends Rectangle{
    private TileState state = TileState.ACTIVE;
    public int column;
    public Tile(int column, int top, int width, int height){
        this.column = column;
        this.x = column * width;
        this.y = top;
        this.width = width;
        this.height = height;
    }

    public Rect getGraphicRect(int horizontalGap){
        int left = column * width + horizontalGap / 2;
        int right = left + width - horizontalGap / 2;
        return new Rect(left, y, right, y + height);
    }

    public void disable(){
        state = TileState.DISABLED;
    }

    public void enable() {
        state = TileState.ACTIVE;
    }

    public void relocate(int newColumn){
        this.column = newColumn;
        this.x = newColumn * width;
    }
    public boolean isDisabled(){
        return state == TileState.DISABLED;
    }

    public boolean isHighlighted(){
        return state == TileState.HIGHLIGHTED;
    }

    public void moveDown(int speed){
        this.y += speed;
    }

    public void highlight() {
        this.state = TileState.HIGHLIGHTED;
    }
}
