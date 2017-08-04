package com.king.reading.common.jsbridge;

import com.orhanobut.logger.Logger;

/**
 * Created by AllynYonge on 22/07/2017.
 */

public class DDBHandler implements BridgeHandler {
    @Override
    public void handler(String data, CallBackFunction function) {
        Logger.d(data);
    }
}
