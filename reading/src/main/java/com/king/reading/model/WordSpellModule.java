package com.king.reading.model;

import android.support.annotation.NonNull;

/**
 * Created by AllynYonge on 14/06/2017.
 */

public class WordSpellModule implements Comparable<WordSpellModule>{
    public String unitName;
    public long unitId;

    public WordSpellModule(String unitName, long unitId) {
        this.unitName = unitName;
        this.unitId = unitId;
    }

    @Override
    public int compareTo(@NonNull WordSpellModule o) {
        return (int) (this.unitId - o.unitId);
    }
}
