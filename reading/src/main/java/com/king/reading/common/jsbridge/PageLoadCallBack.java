package com.king.reading.common.jsbridge;

/**
 * Created by AllynYonge on 22/07/2017.
 */

public interface PageLoadCallBack {
    void loadError(String failUrl);
    void loadFinish(String url);
}
