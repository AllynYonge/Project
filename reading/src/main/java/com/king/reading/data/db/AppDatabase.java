package com.king.reading.data.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by AllynYonge on 05/07/2017.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "DDB";

    public static final int VERSION = 4;
}
