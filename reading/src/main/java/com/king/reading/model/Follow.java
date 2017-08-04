package com.king.reading.model;

import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     王开冰
 * 创建时间   2017/7/29 14:25
 * 描述	      跟读闯关每个具体任务word的bean
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class Follow extends BaseModel implements Serializable{
    private int parentID;//所属模块ID
    private String userName;//所属用户名
    private int Sort;//顺序号
    private String Text;//文本
    private String Sound;//声音路径
    private float score=-1;//分数
    private List<WordResult> words = new ArrayList<>();//每个词的评测结果
    private String recordUrl;//录音链接地址（云之声）
    private String recordPath;//录音文件路径（本地）

    private int rMaxProgress=0;
    private int rCurProgress=0;
    private int rState=0;//0-默认状态，1-进行中

    public int getrMaxProgress() {
        return rMaxProgress;
    }

    public void setrMaxProgress(int rMaxProgress) {
        this.rMaxProgress = rMaxProgress;
    }

    public int getrCurProgress() {
        return rCurProgress;
    }

    public void setrCurProgress(int rCurProgress) {
        this.rCurProgress = rCurProgress;
    }

    public int getrState() {
        return rState;
    }

    public void setrState(int rState) {
        this.rState = rState;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getSound() {
        return Sound;
    }

    public void setSound(String sound) {
        Sound = sound;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public List<WordResult> getWords() {
        return words;
    }

    public void setWords(List<WordResult> words) {
        this.words = new ArrayList<>(words);
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }

    public String getRecordPath() {
        return recordPath;
    }

    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "parentID=" + parentID +
                ", userName='" + userName + '\'' +
                ", Sort=" + Sort +
                ", Text='" + Text + '\'' +
                ", Sound='" + Sound + '\'' +
                ", score=" + score +
                ", words=" + words +
                ", recordUrl='" + recordUrl + '\'' +
                ", recordPath='" + recordPath + '\'' +
                ", rMaxProgress=" + rMaxProgress +
                ", rCurProgress=" + rCurProgress +
                ", rState=" + rState +
                '}';
    }
}
