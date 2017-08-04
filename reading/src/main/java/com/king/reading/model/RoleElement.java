package com.king.reading.model;

/**
 * 创建者     王开冰
 * 创建时间   2017/7/27 15:13
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class RoleElement {

    private String mText;//文本
    private String mSound;//声音路径
    private float mScore =-1;
    private int mMaxProgress =0;
    private int mCurProgress =0;
    private int mState =0;//0-默认状态，1-进行中

    public RoleElement(String text, String sound) {
        super();
        mText = text;
        mSound = sound;
    }
    public String getText() {
        return mText;
    }
    public void setText(String text) {
        mText = text;
    }
    public String getSound() {
        return mSound;
    }
    public void setSound(String sound) {
        mSound = sound;
    }

    public float getScore() {
        return mScore;
    }
    public void setScore(float score) {
        this.mScore = score;
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }
    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }
    public int getCurProgress() {
        return mCurProgress;
    }
    public void setCurProgress(int curProgress) {
        this.mCurProgress = curProgress;
    }
    public int getState() {
        return mState;
    }
    public void setState(int state) {
        this.mState = state;
    }
    @Override
    public String toString() {
        return "RoleElement [mText=" + mText + ", mSound=" + mSound + "]";
    }

}
