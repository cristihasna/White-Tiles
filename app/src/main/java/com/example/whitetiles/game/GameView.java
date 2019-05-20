package com.example.whitetiles.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.example.whitetiles.GameActivity;
import com.example.whitetiles.R;
import com.example.whitetiles.helper.Tile;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "GameView";
    private TextView scoreView;

    private GameThread thread;
    private GameStats gameStats;

    private boolean running;

    private Paint tileBackground;
    private Paint disabledTileBackground;
    private Paint gridBackground;
    private Paint highlightedTileBackground;

    private Context context;
    private GameActivity activity;

    private long gameTime = 0; // used to determine when to add a new tile
    private long timeDiff = 0; // used for managing paused time;

    private int canvasH;
    private int canvasW;


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

        disabledTileBackground = new Paint();
        disabledTileBackground.setColor(getResources().getColor(R.color.lightPurple));

        highlightedTileBackground = new Paint();
        highlightedTileBackground.setColor(getResources().getColor(R.color.red));

        gridBackground = new Paint();
        gridBackground.setColor(getResources().getColor(R.color.lightGrey));
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
            gameStats = new GameStats(width, height, this);
            canvasH = height;
            canvasW = width;
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
        if (running == false){
            timeDiff = System.nanoTime() - gameTime;
        } else {
            gameTime = System.nanoTime() - timeDiff;
        }
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
                // update score
                if (scoreView != null)
                    scoreView.setText("Score: "+ gameStats.getScore());

            }
        });

    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        if (gameStats != null){
            // draw each tile
            for (Tile tile: gameStats.getTiles()){
                Paint background;
                if (tile.isDisabled()) background = disabledTileBackground;
                else if (tile.isHighlighted()) background = highlightedTileBackground;
                else background = tileBackground;
                canvas.drawRect(tile.getGraphicRect(), background);
            }

            // draw grid lines
            for (int i = 0; i < GameStats.COLS; i++) {
                int left = canvasW / GameStats.COLS * i - GameStats.TILE_HORIZONTAL_GAP / 2;
                int right = left + GameStats.TILE_HORIZONTAL_GAP;
                canvas.drawRect(left, 0, right, canvasH, gridBackground);

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (gameStats == null) return false;
        for (int i = 0; i < event.getPointerCount(); i++) {
            gameStats.handleTouch(event.getX(i), event.getY(i));
        }
        return true;
    }

    public void setScoreView(TextView scoreView) {
        this.scoreView = scoreView;
    }

    public void setActivity(GameActivity activity) {
        this.activity = activity;
    }

    public void gameOver() {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.handleGameOver(gameStats.getScore());
            }
        });
    }

    public void reset() {
        gameStats.reset();
    }
}
