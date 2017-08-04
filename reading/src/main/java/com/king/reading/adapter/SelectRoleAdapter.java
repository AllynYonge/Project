package com.king.reading.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.ddb.PlayBook;
import com.king.reading.model.RoleModel;

import java.util.List;

/**
 * 创建者     王开冰
 * 创建时间   2017/7/26 11:29
 * 描述	      角色扮演对话框的适配器
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class SelectRoleAdapter extends BaseAdapter {

    private PlayBook mPlayBook;
    private List<RoleModel> mSelectedRoles;
    private LayoutInflater mLayoutInflater;

    public SelectRoleAdapter(PlayBook playBook, List<RoleModel> rolesSelected) {
        mPlayBook = playBook;
        mSelectedRoles = rolesSelected;
        mLayoutInflater = (LayoutInflater) SysApplication.getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if (mPlayBook.roles != null) {
            return mPlayBook.roles.size();
        }
        return 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=mLayoutInflater.inflate(R.layout.item_listen_timer, null);
            holder.checkbox=(CheckBox) convertView.findViewById(R.id.chk_listen_timer_selected);
            holder.text=(TextView)convertView.findViewById(R.id.tv_listen_timer);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.text.setText(mPlayBook.roles.get(position));
        holder.checkbox.setChecked(mSelectedRoles.get(position).isSelect);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isSelect = mSelectedRoles.get(position).isSelect;
                holder.checkbox.setChecked(!isSelect);
                mSelectedRoles.set(position,new RoleModel(mSelectedRoles.get(position).name,!isSelect));
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public List<RoleModel> getSelectedRoles(){
        return mSelectedRoles;
    }

    private class ViewHolder{
        private CheckBox checkbox;
        private TextView text;
    }
}
