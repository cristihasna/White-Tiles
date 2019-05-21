package com.example.whitetiles.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.example.whitetiles.GameActivity;
import com.example.whitetiles.R;
import com.example.whitetiles.helper.Tile;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "GameView";
    public static final int TILE_VERTICAL_GAP = 0;
    public static final int TILE_HORIZONTAL_GAP = 4;
    private TextView scoreView;

    private int sfxList[];
    private SoundPool sp;
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

    private int nextSound = 0;


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

        sp = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        sfxList = new int[7];
        sfxList[0] = sp.load(context, R.raw.key_1, 1);
        sfxList[1] = sp.load(context, R.raw.key_2, 1);
        sfxList[2] = sp.load(context, R.raw.key_3, 1);
        sfxList[3] = sp.load(context, R.raw.key_4, 1);
        sfxList[4] = sp.load(context, R.raw.key_5, 1);
        sfxList[5] = sp.load(context, R.raw.key_6, 1);
        sfxList[6] = sp.load(context, R.raw.end, 1);
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
        if (!running){
            timeDiff = System.nanoTime() - gameTime;
        } else {
            gameTime = System.nanoTime() - timeDiff;
        }
    }

    public void update(){
        if (!running || gameStats == null) return;
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
                canvas.drawRect(tile.getGraphicRect(TILE_HORIZONTAL_GAP), background);
            }

            // draw grid lines
            for (int i = 0; i < GameStats.COLS; i++) {
                int left = canvasW / GameStats.COLS * i - TILE_HORIZONTAL_GAP / 2;
                int right = left + TILE_HORIZONTAL_GAP;
                canvas.drawRect(left, 0, right, canvasH, gridBackground);

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (gameStats == null || !running) return false;
        for (int i = 0; i < event.getPointerCount(); i++) {
            int res = gameStats.handleTouch(event.getX(i), event.getY(i));
            if (res != -1) {
                sp.play(sfxList[res], 1, 1, 1, 0, 1);

            }
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
        sp.play(sfxList[6], 1, 1, 1, 0, 1);
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
