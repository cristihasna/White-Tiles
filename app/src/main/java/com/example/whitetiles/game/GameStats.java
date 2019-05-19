package com.example.whitetiles.game;

import android.util.Log;

import com.example.whitetiles.helper.Tile;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class GameStats {
    private static final int TILE_VERTICAL_GAP = 50;
    private static final int TILE_HORIZONTAL_GAP = 10;
    private static final String TAG = "GameStats";

    private static final int COLS = 6;
    private static final int ROWS = 4;
    private List<Deque<Tile>> tiles;
    private int score;
    private int speed;

    private Random rand = new Random();

    private int canvasH;
    private int tileH;
    private int tileW;

    public GameStats(int canvasWidth, int canvasHeight){
        tiles = new ArrayList<>();
        for(int i = 0; i < COLS; i++)
            tiles.add(new ArrayDeque<Tile>());
        score = 0;
        speed = 30;
        canvasH = canvasHeight;
        tileW = canvasWidth / COLS;
        tileH = canvasH / ROWS;
    }

    public void addTile(int column){
        int left = column * tileW + TILE_HORIZONTAL_GAP / 2;
        Tile tile = new Tile(left, -tileH, tileW - TILE_HORIZONTAL_GAP, tileH);
        Deque<Tile> colStack = tiles.get(column);
        colStack.addFirst(tile);
        Log.d(TAG, "tile added");
    }

    public void update(){
        for(Deque<Tile> colStack: tiles){
            if (colStack.isEmpty()) continue;
            for (Tile tile: colStack){
                tile.moveDown(speed);
            }
            if (colStack.getLast().y > canvasH){
                Log.d(TAG, "tile removed");
                colStack.removeLast();
            }
        }

    }

    public List<Tile> getTiles() {
        List<Tile> tiles = new ArrayList<>();
        for (Deque<Tile> colStack: this.tiles){
            tiles.addAll(colStack);
        }
        return tiles;
    }

    public int getScore() {
        return score;
    }

    public int getRandomAvailableCol() {
        List<Integer> availableCols = new ArrayList<>();
        for (int i = 0; i < COLS; i++){

            Tile firstTile = tiles.get(i).peekFirst();
            if (firstTile == null || firstTile.y > TILE_VERTICAL_GAP)
                availableCols.add(i);
        }
        if (availableCols.isEmpty()) return -1;
        return availableCols.get(rand.nextInt(availableCols.size()));
    }

    public long getDelay() {
        return 2_000_000_000 - (score + 1) * 100_000_000;
    }
}
