package com.example.whitetiles;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.whitetiles.helper.FontManager;
import com.example.whitetiles.game.GameView;

public class GameActivity extends Activity {
    private boolean running = true;
    private int score = 0;
    private TextView pauseButton;
    private View popupMenu;
    private GameView game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        popupMenu = findViewById(R.id.popupWrapper);
        popupMenu.setVisibility(View.GONE);

        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID));

        TextView popupContinueButton = findViewById(R.id.continueButton);
        popupContinueButton.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID));

        game = findViewById(R.id.gameView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (running) handlePause(null);
    }

    public void handleLeave(View view){
        Log.d("Game", "Leaving game with score " + score);
        finish();
    }

    public void handlePause(View view){
        running = !running;
        game.setRunning(running);

        if (!running) {
            pauseButton.setText(R.string.fa_play);
            popupMenu.setVisibility(View.VISIBLE);
        } else {
            pauseButton.setText(R.string.fa_pause);
            popupMenu.setVisibility(View.GONE);
        }
    }



    @Override
    public void onBackPressed() {
        if (running) handlePause(null);
    }
}
