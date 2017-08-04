package com.king.reading.model;

/**
 * 创建者     王开冰
 * 创建时间   2017/7/26 10:53
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class RoleModel {
    public String name;
    public boolean isSelect;

    public RoleModel(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "RoleModel{" +
                "name='" + name + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
