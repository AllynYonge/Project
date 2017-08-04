package com.king.reading.model;

/**
 * Created by hu.yang on 2017/6/9.
 */

public class ListenUnit{
    boolean isPlaying;
    boolean isChecked;
    boolean isPause;
    public String title;
    public String pageNumber;
    public int startPage;
    public int endPage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStartPage() {
        return startPage;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public ListenUnit(boolean isPlaying, boolean isChecked, String title, String pageNumber, int startPage, int endPage) {
        this.isPlaying = isPlaying;
        this.isChecked = isChecked;
        this.title = title;
        this.pageNumber = pageNumber;
        this.startPage = startPage;
        this.endPage = endPage;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }
}
