package com.king.reading.model;

import java.util.List;

/**
 * Created by AllynYonge on 15/06/2017.
 */

public class BreakThroughUnit extends BaseNestedList<BreakThroughUnit.SubUnit> {
    public String unitName;

    public BreakThroughUnit(String unitName, List<SubUnit> items) {
        super(items);
        this.unitName = unitName;
    }

    public static class SubUnit{

        public final int missionId;

        public SubUnit(String subUnitName, int starNum, int maxStarNum, int missionId) {
            this.SubUnitName = subUnitName;
            this.starNum = starNum;
            this.maxStarNum = maxStarNum;
            this.missionId = missionId;
        }

        public String SubUnitName;
        public int starNum;
        public int maxStarNum;
    }
}
