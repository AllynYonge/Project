package com.king.reading.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.king.reading.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 创建者     王开冰
 * 创建时间   2017/8/1 20:52
 * 描述	      分享app的对话框
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class ShareDialog {
    private AlertDialog dialog;
    private GridView gridView;
    private RelativeLayout cancelButton;
    private SimpleAdapter saImageItems;
    private int[] image={R.mipmap.logo_qq,R.mipmap.logo_qzone,R.mipmap.logo_wechat,R.mipmap.logo_wechatmoments};
    private String[] name={"QQ","QQ空间","微信好友","微信朋友圈"};

    public ShareDialog(Activity context){
        dialog=new android.app.AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.share_dialog);
        gridView=(GridView) window.findViewById(R.id.share_gridView);
        cancelButton=(RelativeLayout) window.findViewById(R.id.share_cancel);
        List<HashMap<String, Object>> shareList=new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<image.length;i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", image[i]);//添加图像资源的ID
            map.put("ItemText", name[i]);//按序号做ItemText
            shareList.add(map);
        }

        saImageItems =new SimpleAdapter(context, shareList, R.layout.share_item, new String[] {"ItemImage","ItemText"}, new int[] {R.id.imageView1,R.id.textView1});
        gridView.setAdapter(saImageItems);

    }

    public void setCancelButtonOnClickListener(View.OnClickListener Listener){
        cancelButton.setOnClickListener(Listener);
    }


    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        gridView.setOnItemClickListener(listener);
    }


    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }
}
