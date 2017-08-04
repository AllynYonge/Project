package com.king.reading.model;

/**
 * Created by AllynYonge on 20/06/2017.
 */

public class Notice {
    public boolean isRedRot;
    public String title;
    public String content;
    public String time;

    public Notice(boolean isRedRot, String title, String content, String time) {
        this.isRedRot = isRedRot;
        this.title = title;
        this.content = content;
        this.time = time;
    }
}
