package com.example.whitetiles;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = findViewById(R.id.container);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.main_background, null);
        view.setBackground(drawable);
    }

    public void onPlay(View view){
        Log.d("Game", "New game");
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }

    public void onQuit(View view){
        Log.d("Game", "Exiting");
        finish();
    }

    public void onHighScores(View view){
        Log.d("Game", "High Scores");
        Intent intent = new Intent(MainActivity.this, HSActivity.class);
        startActivity(intent);
    }

}
