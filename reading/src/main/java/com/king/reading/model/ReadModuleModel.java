package com.king.reading.model;

import java.util.List;

/**
 * Created by hu.yang on 2017/5/25.
 */

public class ReadModuleModel extends ExpandNestedList<ReadUnit> {
    public ReadModuleModel(boolean isExpand, String moduleName, String modulePageNumber, List<ReadUnit> units) {
        super(isExpand, units);
        this.moduleName = moduleName;
        this.modulePageNumber = modulePageNumber;
    }

    public String moduleName;
    public String modulePageNumber;
}
