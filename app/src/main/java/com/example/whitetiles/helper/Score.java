package com.example.whitetiles.helper;

import java.util.Date;

public class Score {
    private Date date;
    private int score;

    public Score(Date date, int score){
        this.date = date;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public Date getDate() {
        return date;
    }
}
