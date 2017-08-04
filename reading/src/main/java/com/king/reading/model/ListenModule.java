package com.king.reading.model;

import java.util.List;

/**
 * Created by hu.yang on 2017/5/25.
 */

public class ListenModule extends ExpandNestedList<ListenUnit> {
    public boolean isChecked;
    public String moduleName;
    public String modulePageNumber;


    public ListenModule(boolean isExpand, boolean isChecked, String moduleName, String modulePageNumber, List<ListenUnit> units) {
        super(isExpand, units);
        this.isChecked = isChecked;
        this.moduleName = moduleName;
        this.modulePageNumber = modulePageNumber;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModulePageNumber() {
        return modulePageNumber;
    }

    public void setModulePageNumber(String modulePageNumber) {
        this.modulePageNumber = modulePageNumber;
    }
}
