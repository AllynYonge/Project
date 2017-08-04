package com.king.reading.model;

import com.king.reading.ddb.PlayBook;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     王开冰
 * 创建时间   2017/7/26 14:48
 * 描述	      剧本信息以及选中角色
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class EventMessagePlayBook {
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

    @Override
    public String toString() {
        return "EventMessagePlayBook{" +
                "mPlayBook=" + mPlayBook +
                ", mSelectedRoles=" + mSelectedRoles +
                '}';
    }
}
