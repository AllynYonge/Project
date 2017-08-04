package com.king.reading.common.push;


import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.orhanobut.logger.Logger;

import java.util.Map;

/**
 * Created by AllynYonge on 25/07/2017.
 */

public class PopupPushActivity extends AndroidPopupActivity {
    /**
     * 实现通知打开回调方法，获取通知相关信息
     * @param title     标题
     * @param summary   内容
     * @param extMap    额外参数
     */
    @Override
    protected void onSysNoticeOpened(String title, String summary, Map<String, String> extMap) {
        Logger.d("OnMiPushSysNoticeOpened, title: " + title + ", content: " + summary + ", extMap: " + extMap);
    }
}
