package com.king.reading.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.king.reading.R;


public class PopWindowListemTimer extends PopupWindow{
    private LayoutInflater layoutInflater;
    private View view;

    public PopWindowListemTimer(Context context){
        layoutInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        view = layoutInflater.inflate(R.layout.layout_popwindow_listen_timer,null);//加载布局文件
        setContentView(view);

        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(true);
        setOutsideTouchable(true);
    }

   
	public View getPopWindowView() {
		return view;
	}

}
