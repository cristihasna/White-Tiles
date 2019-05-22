package com.example.whitetiles.helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.whitetiles.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ScoresListAdapter extends ArrayAdapter<Score> {
    private static final String TAG = "ListAdapter";
    private Context context;
    private int resource;
    private List<Score> items;
    private SimpleDateFormat parser = new SimpleDateFormat("d MMM YYYY, HH:mm:ss", Locale.ENGLISH);

    public ScoresListAdapter(Context context, int resource, List<Score> items){
        super(context, resource, items);
        this.context=context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Score score = getItem(position);

        if (convertView == null){
            LayoutInflater vi =  LayoutInflater.from(this.context);
            convertView = vi.inflate(this.resource, null);
        }

        if (score != null) {
            TextView scoreView = convertView.findViewById(R.id.scoreText);
            TextView dateView = convertView.findViewById(R.id.dateText);
            Log.d(TAG, "scoreView" + scoreView);
            Log.d(TAG, "Score: " + score.getDate() + " " + score.getScore());
            scoreView.setText(Integer.toString(score.getScore()));
            dateView.setText(parser.format(score.getDate()));
        }

        return convertView;
    }
}
