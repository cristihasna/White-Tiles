package com.example.whitetiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.whitetiles.helper.FontManager;
import com.example.whitetiles.game.GameView;

enum Game {
    RUNNING,
    PAUSED,
    GAME_OVER
}

public class GameActivity extends Activity {
    private Game gameState = Game.RUNNING;
    private TextView pauseButton;
    private View pauseMenu;
    private View gameOverMenu;
    private GameView game;

    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        pauseMenu = findViewById(R.id.popupWrapper);
        pauseMenu.setVisibility(View.GONE);

        gameOverMenu = findViewById(R.id.gameOverPopupWrapper);
        gameOverMenu.setVisibility(View.GONE);

        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID));
        ((TextView)findViewById(R.id.timesIcon)).setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID));

        TextView popupContinueButton = findViewById(R.id.continueButton);
        popupContinueButton.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID));

        game = findViewById(R.id.gameView);
        TextView scoreView = findViewById(R.id.scoreText);
        game.setScoreView(scoreView);
        game.setActivity(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (gameState == Game.RUNNING) handlePause(null);
    }

    public void handleLeave(View view){
        Log.d("Game", "Exiting game ");
        finish();
    }

    public void handlePause(View view){
        if (gameState == Game.RUNNING) gameState = Game.PAUSED;
        else if (gameState == Game.PAUSED) gameState = Game.RUNNING;
        game.setRunning(gameState == Game.RUNNING);

        if (gameState == Game.PAUSED) {
            pauseButton.setText(R.string.fa_play);
            pauseMenu.setVisibility(View.VISIBLE);
            gameOverMenu.setVisibility(View.GONE);
        } else if (gameState == Game.GAME_OVER){
            pauseButton.setText(R.string.fa_play);
            pauseMenu.setVisibility(View.GONE);
            gameOverMenu.setVisibility(View.VISIBLE);
        } else {
            pauseButton.setText(R.string.fa_pause);
            pauseMenu.setVisibility(View.GONE);
            gameOverMenu.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        handlePause(null);
    }

    public void handleGameOver(int score) {
        this.score = score;
        game.setRunning(false);
        gameState = Game.GAME_OVER;
        handlePause(null);
        // save score
    }

    public void handleNewGame(View view){
        game.reset();
        game.setRunning(true);
        gameState = Game.RUNNING;
        pauseButton.setText(R.string.fa_pause);
        pauseMenu.setVisibility(View.GONE);
        gameOverMenu.setVisibility(View.GONE);
    }

    public void handleShare(View view){
        String shareString = "I just scored " + this.score + " on WhiteTiles!";
        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_TEXT, shareString);
        it.setType("text/plain");
        startActivity(Intent.createChooser(it, "Share score"));
    }

}
