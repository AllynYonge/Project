package com.king.reading.model;

import java.util.List;

/**
 * Created by AllynYonge on 14/06/2017.
 */

public class BreakThroughResult {
    public int totalScore;
    public List<ScoreDetail> list;

    public BreakThroughResult(int totalScore, List<ScoreDetail> list) {
        this.totalScore = totalScore;
        this.list = list;
    }

    public static class ScoreDetail{
        public String word;
        public int score;

        public ScoreDetail(String word, int score) {
            this.word = word;
            this.score = score;
        }
    }
}
