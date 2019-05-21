package com.example.whitetiles.game;

import android.util.Log;

import com.example.whitetiles.helper.Tile;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class GameStats {
    public static final int COLS = 6;
    public static final int ROWS = 4;
    private static final String TAG = "GameStats";
    private static final int INITIAL_SPEED = 30;
    private Deque<Tile> tiles;
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
        tiles = new ArrayDeque<>();
        score = 0;
        speed = INITIAL_SPEED;
        canvasH = canvasHeight;
        tileW = canvasWidth / COLS;
        tileH = canvasH / ROWS;
        this.gameView = gameView;

        initTiles();
    }

    private void initTiles(){
        for (int i = 0; i <= ROWS; i++) {
            int col = getRandomAvailableCol();
            int y = (-i - 1) * tileH;
            tiles.addFirst(new Tile(col, y, tileW, tileH));
        }
        Log.d(TAG, "added tiles " + tiles.size());
    }

    public void relocateTile(Tile tile, int column) {
        tile.enable();
        tile.relocate(column);
        tile.y = -tileH;
    }

    public void update() {
        for(Tile tile:tiles){
            tile.moveDown(speed);
            if (tile.y >= canvasH){
                if (!tile.isDisabled()){
                    tile.highlight();
                    triggerGameOver();
                }
                else relocateTile(tile, getRandomAvailableCol());
            }
        }

        if (reversedCounter >= 0) {
            reversedCounter++;
            if (reversedCounter >= tileH / -speed + 1) {
                gameView.gameOver();
            }
        }

    }

    private void triggerGameOver() {
        speed = -30;
        reversedCounter = 0;
    }

    public Deque<Tile> getTiles() {
        return tiles;
    }

    public int getScore() {
        return score;
    }

    public int getRandomAvailableCol() {
        int newCol = rand.nextInt(COLS);
        while (newCol == lastCol)
            newCol = rand.nextInt(COLS);
        lastCol = newCol;
        return newCol;
    }


    public int handleTouch(float x, float y) {
        for (Tile tile : tiles) {
            if (!tile.isDisabled() && tile.contains((int) x, (int) y)) {
                tile.disable();
                score++;
                speed = INITIAL_SPEED + score / 20;
                return tile.column;
            }
        }
        return -1;
    }

    public void reset() {
        score = 0;
        speed = INITIAL_SPEED;
        reversedCounter = -1;
        tiles.clear();
        initTiles();
    }
}