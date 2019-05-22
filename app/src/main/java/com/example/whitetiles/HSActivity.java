package com.example.whitetiles;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.whitetiles.helper.Score;
import com.example.whitetiles.helper.ScoresListAdapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HSActivity extends Activity {

    private static final String TAG = "HighScores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hs);
        View container = findViewById(R.id.hs_container);
        Drawable bg = ResourcesCompat.getDrawable(getResources(), R.drawable.high_scores_background, null);
        container.setBackground(bg);
        List<Score> scores = new ArrayList<>();
        String filename = "score";
        FileInputStream inputStream;
        try {
            inputStream = openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            SimpleDateFormat parser = new SimpleDateFormat("d MMM YYYY, HH:mm:ss", Locale.ENGLISH);
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("[|]");
                Date date = parser.parse(parts[0]);
                String scoreValue = parts[1];
                scores.add(new Score(date, Integer.parseInt(scoreValue)));
            }
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.d(TAG, "Scores: " + scores);
        ListAdapter sensorListAdapter = new ScoresListAdapter(this, R.layout.score_list_item, scores);

        ListView listView = findViewById(R.id.scores_list);
        Log.d(TAG, "listView" + listView);
        listView.setAdapter(sensorListAdapter);
    }

    public void onBack(View view){
        finish();
    }
}
