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
    public Tile(int left, int top, int width, int height){
        this.x = left;
        this.y = top;
        this.width = width;
        this.height = height;
    }

    public Rect getGraphicRect(){
        return new Rect(x, y, x + width, y + height);
    }

    public void disable(){
        state = TileState.DISABLED;
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
