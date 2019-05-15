package com.example.whitetiles.game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;


public class GameThread extends Thread {
    private int FPS = 2;
    private double avgFPS;
    private SurfaceHolder holder;
    private GameView game;
    private boolean running;
    private Canvas canvas;

    public GameThread(SurfaceHolder holder, GameView game){
        super();
        this.holder = holder;
        this.game = game;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.holder.lockCanvas();
                synchronized (canvas) {
                    game.update();
                    game.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        this.holder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1_000_000;
            waitTime = targetTime - timeMillis;

            try {
                if (waitTime > 0) sleep(waitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                avgFPS = 1000 / ((totalTime / frameCount) / 1_000_000);
                Log.d("Game", "FPS: " + avgFPS);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    public void setRunning(boolean running){
        this.running = running;
    }
}
