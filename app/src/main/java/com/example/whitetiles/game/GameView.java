package com.example.whitetiles.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.example.whitetiles.R;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread thread;
    private boolean running;
    private Paint tileBackground;

    private int width;
    private int height;

    private int rectX;
    private int rectY;

    private int speed = 5;

    private void init(){
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        running = true;
        width = getWidth();
        Log.d("Game", "width: " + width);
        height = getHeight();
        rectX = width / 2;
        rectY = 0;
        tileBackground = new Paint();
        tileBackground.setColor(getResources().getColor(R.color.purple));
    }

    public GameView(Context context){
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attributes){
        super(context, attributes);
        init();
    }

    public GameView(Context context, AttributeSet attributes, int defStyle){
        super(context, attributes, defStyle);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

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
        if (!running) return;
        Log.d("GameUpdate", "Updating");
        rectY += speed;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("Game", "drawing at " + rectX + " | " + rectY);

        canvas.drawRect(rectX, rectY, rectX + 100, rectY + 100, tileBackground);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("Game", "Measured: " + widthMeasureSpec + " " + heightMeasureSpec);
    }
}
