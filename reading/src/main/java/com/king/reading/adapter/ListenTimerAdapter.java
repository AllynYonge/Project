package com.king.reading.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.king.reading.R;
import com.king.reading.SysApplication;

import java.util.Arrays;
import java.util.List;

/**
 * 创建者     王开冰
 * 创建时间   2017/7/19 18:44
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class ListenTimerAdapter extends BaseAdapter{

    private  LayoutInflater mInflater;
    private final List<String> mListenTimers;
    private int mCheckedPostion;

    public ListenTimerAdapter(int checkedPostion) {
        mInflater=(LayoutInflater) SysApplication.getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListenTimers = Arrays.asList(SysApplication.getApplication().getResources().getStringArray(R.array.listen_timer));
        mCheckedPostion = checkedPostion;
    }

    public void setCheckedPostion(int checkedPostion){
        mCheckedPostion = checkedPostion;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView=mInflater.inflate(R.layout.item_listen_timer, parent, false);
        TextView tv = (TextView) convertView.findViewById(R.id.tv_listen_timer);
        AppCompatCheckBox checkBox = (AppCompatCheckBox) convertView.findViewById(R.id.chk_listen_timer_selected);
        if (position == mCheckedPostion){
            checkBox.setChecked(true);
            tv.setTextColor(SysApplication.getApplication().getResources().getColor(R.color.yellow_60));
        }else{
            checkBox.setChecked(false);
            tv.setTextColor(SysApplication.getApplication().getResources().getColor(R.color.black));

        }
        tv.setText(mListenTimers.get(position));
        return convertView;
    }
}
