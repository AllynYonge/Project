package com.king.reading.model;

import com.king.reading.ddb.PlayBook;
import com.king.reading.model.RoleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     王开冰
 * 创建时间   2017/7/27 18:45
 * 描述	      角色扮演完成跳转得分详情页的信息
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class EventMessageRoleScore {
    private float mScore;
    private PlayBook mPlayBook;
    private List<RoleModel> mSelectedRoles;

    public PlayBook getPlayBook() {
        return mPlayBook;
    }

    public void setPlayBook(PlayBook playBook) {
        mPlayBook = playBook;
    }

    public List<RoleModel> getSelectedRoles() {
        return mSelectedRoles;
    }

    public void setSelectedRoles(List<RoleModel> selectedRoles) {
        mSelectedRoles = new ArrayList<>(selectedRoles);
    }

    public float getScore() {
        return mScore;
    }

    public void setScore(float score) {
        mScore = score;
    }

    @Override
    public String toString() {
        return "EventMessageRoleScore{" +
                "mScore=" + mScore +
                ", mPlayBook=" + mPlayBook +
                ", mSelectedRoles=" + mSelectedRoles +
                '}';
    }
}
