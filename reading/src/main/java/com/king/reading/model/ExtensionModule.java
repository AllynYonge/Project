package com.king.reading.model;

/**
 * Created by AllynYonge on 24/06/2017.
 */

public class ExtensionModule {
    public String extensionName;
    public boolean isAdd;
    public long extensionId;

    public ExtensionModule(String extensionName, boolean isAdd, long extensionId) {
        this.extensionName = extensionName;
        this.isAdd = isAdd;
        this.extensionId = extensionId;
    }
}
