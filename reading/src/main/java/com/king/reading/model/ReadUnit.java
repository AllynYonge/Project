package com.king.reading.model;

/**
 * Created by hu.yang on 2017/5/25.
 */

public class ReadUnit {

    public final long unitId;

    public ReadUnit(String url, String unit, String pageNumber, long unitId) {
        this.url = url;
        this.unit = unit;
        this.pageNumber = pageNumber;
        this.unitId = unitId;
    }

    public String url;
    public String unit;
    public String pageNumber;
}
