package com.example.whitetiles.game;

import android.util.Log;

import com.example.whitetiles.helper.Tile;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class GameStats {
    public static final int TILE_VERTICAL_GAP = 0;
    public static final int TILE_HORIZONTAL_GAP = 4;
    private static final String TAG = "GameStats";

    public static final int COLS = 5;
    public static final int ROWS = 3;
    private List<Deque<Tile>> tiles;
    private int score;
    private int speed;

    private Random rand = new Random();
    private GameView gameView;

    private int canvasH;
    private int tileH;
    private int tileW;

    private int reversedCounter = -1;

    private int lastCol = -1;

    public GameStats(int canvasWidth, int canvasHeight, GameView gameView) {
        tiles = new ArrayList<>();
        for (int i = 0; i < COLS; i++)
            tiles.add(new ArrayDeque<Tile>());
        score = 0;
        speed = 10;
        canvasH = canvasHeight;
        tileW = canvasWidth / COLS;
        tileH = canvasH / ROWS;
        this.gameView = gameView;
    }

    public void addTile(int column) {
        int left = column * tileW + TILE_HORIZONTAL_GAP / 2;
        Tile tile = new Tile(left, -tileH, tileW - TILE_HORIZONTAL_GAP, tileH);
        Deque<Tile> colStack = tiles.get(column);
        colStack.addFirst(tile);
    }

    public void update() {
        for (int i = 0; i < tiles.size(); i++) {
            Deque<Tile> colStack = tiles.get(i);
            if (colStack.isEmpty()) continue;
            for (Tile tile : colStack) {
                tile.moveDown(speed);
            }
            Tile lastTile = colStack.getLast();
            if (lastTile.y > canvasH) {
                if (!lastTile.isDisabled()){
                    lastTile.highlight();
                    triggerGameOver();
                }
                else colStack.removeLast();
            }
        }
        if (reversedCounter >= 0){
            reversedCounter++;
            if (reversedCounter >= tileH / -speed + 1){
                gameView.gameOver();
            }
        }

    }

    private void triggerGameOver() {
        speed = -30;
        reversedCounter = 0;
    }

    public List<Tile> getTiles() {
        List<Tile> tiles = new ArrayList<>();
        for (Deque<Tile> colStack : this.tiles) {
            tiles.addAll(colStack);
        }
        return tiles;
    }

    public int getScore() {
        return score;
    }

    public int getRandomAvailableCol() {
        List<Integer> availableCols = new ArrayList<>();
        for (int i = 0; i < COLS; i++) {

            Tile firstTile = tiles.get(i).peekFirst();
            if ((firstTile == null || firstTile.y > TILE_VERTICAL_GAP) && i != lastCol)
                availableCols.add(i);
        }
        if (availableCols.isEmpty()) return -1;
        lastCol = availableCols.get(rand.nextInt(availableCols.size()));
        return lastCol;
    }

    public long getDelay() {
        long framesPerTile = tileH / speed;
        long nanosPerFrame = 1_000_000_000 / GameThread.FPS; // 1 sec / num of fps
        return (framesPerTile + 1) * nanosPerFrame;
    }

    public void handleTouch(float x, float y) {
        for (Deque<Tile> colStack : tiles) {
            if (colStack.isEmpty()) continue;
            for (Tile tile : colStack) {
                if (!tile.isDisabled() && tile.contains((int)x, (int)y)){
                    tile.disable();
                    score++;
                    speed = 10 + score / 10;
                }
            }
        }
    }

    public void reset() {
        score = 0;
        speed = 10;
        reversedCounter = -1;
        for (Deque colStack: tiles){
            colStack.clear();
        }
    }
}