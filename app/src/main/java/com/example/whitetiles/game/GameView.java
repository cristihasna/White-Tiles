package com.example.whitetiles.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.whitetiles.R;
import com.example.whitetiles.helper.Tile;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "GameView";
    
    private GameThread thread;
    private GameStats gameStats;
    private boolean running;
    private Paint tileBackground;
    private Context context;

    private long gameTime = 0;


    public GameView(Context context){
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attributes){
        super(context, attributes);
        init(context);
    }

    public GameView(Context context, AttributeSet attributes, int defStyle){
        super(context, attributes, defStyle);
        init(context);
    }

    private void init(Context context){
        getHolder().addCallback(this);
        running = true;
        tileBackground = new Paint();
        tileBackground.setColor(getResources().getColor(R.color.purple));
        this.context = context;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
        Log.d(TAG, "surface created");
        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (gameStats == null) {
            gameStats = new GameStats(width, height);
            gameTime = System.nanoTime();
        }
        Log.d(TAG, "surfaceChanged: " + width + " | " + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
            } catch (Exception e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void setRunning(boolean running){
        this.running  = running;
    }

    public void update(){
        if (!running || gameStats == null) return;

        long newTime = System.nanoTime();
        if (newTime - gameTime > gameStats.getDelay()){
            int availableCol = gameStats.getRandomAvailableCol();
            if (availableCol != -1){
                gameStats.addTile(availableCol);
                gameTime = newTime;
            }
        }
        gameStats.update();
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        if (gameStats != null){
            for (Tile tile: gameStats.getTiles()){
                canvas.drawRect(tile.getGraphicRect(), tileBackground);
            }
        }
    }
}
