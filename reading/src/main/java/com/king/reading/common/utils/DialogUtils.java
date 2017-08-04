package com.king.reading.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.adapter.SelectRoleAdapter;
import com.king.reading.model.EventMessagePlayBook;
import com.king.reading.ddb.PlayBook;
import com.king.reading.model.RoleModel;

import java.util.Arrays;
import java.util.List;


/**
 * 创建者     王开冰
 * 创建时间   2017/6/29 16:44
 * 描述	      常规对话框的封装
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class DialogUtils {
    private static AlertDialog mDialog;


    private CustomDialogOnclickListener mCustomDialogOnclickListener;

    public interface CustomDialogOnclickListener {
        void confirm();

        void cancle();
    }

    /**
     * 单词听写速率和间隔时间设置
     *
     * @param activity
     * @param handler
     */
    public static void listenWordSpeedRateAndTimerDialog(Activity activity, final Handler handler) {
        dismissDialog();
        mDialog = new AlertDialog.Builder(activity, R.style.CustomDialog).create();

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.show();
        final View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_listen_word_speed_time, null);
        final RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.tab_button_container);
        final SeekBar seekBar = (SeekBar) layout.findViewById(R.id.seekbar);
        final TextView tvTimer = (TextView) layout.findViewById(R.id.tv_timer);
        int listenWordSpeedRadioButtonId = SpUtils.sharePreGetInt(SysApplication.getApplication(), "listenWordSpeedRadioButtonId");
        if (listenWordSpeedRadioButtonId != 0) {
            RadioButton radioButton = (RadioButton) layout.findViewById(listenWordSpeedRadioButtonId);
            radioButton.setChecked(true);
        }

        int listenWordTimer = SpUtils.sharePreGetInt(SysApplication.getApplication(), "listenWordTimer");
        if (listenWordTimer != 0) {
            seekBar.setProgress(listenWordTimer - 4);
            tvTimer.setText(listenWordTimer + "s");
        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTimer.setText((progress + 4) + "s");
                SpUtils.sharePreSaveInt(SysApplication.getApplication(), "listenWordTimer", (progress + 4));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button btOK = (Button) layout.findViewById(R.id.bt_ok);
        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                SpUtils.sharePreSaveInt(SysApplication.getApplication(), "listenWordSpeedRadioButtonId", checkedRadioButtonId);
                RadioButton radioButton = (RadioButton) layout.findViewById(checkedRadioButtonId);
                String speed = radioButton.getText().toString();
                if ("0.8X".equals(speed)) {
                    SpUtils.sharePreSaveFloat(SysApplication.getApplication(), "listenWordSpeed", 0.8f);
                } else if ("1.0X".equals(speed)) {
                    SpUtils.sharePreSaveFloat(SysApplication.getApplication(), "listenWordSpeed", 1.0f);
                } else if ("1.2X".equals(speed)) {
                    SpUtils.sharePreSaveFloat(SysApplication.getApplication(), "listenWordSpeed", 1.2f);
                }
                handler.sendEmptyMessage(C.LISTEN_WORD_SPEED);
                dismissDialog();
            }

        });
        mDialog.setContentView(layout);
    }

    public static void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 选择角色对话框
     * @param activity
     * @param playBook
     * @param rolesSelected
     * @param handler
     */
    public static void chooseRoleDialog(Activity activity, final PlayBook playBook, List<RoleModel> rolesSelected, final Handler handler) {
        dismissDialog();
        mDialog = new AlertDialog.Builder(activity, R.style.CustomDialog).create();
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.show();
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels; // 屏幕宽度（像素）
        int height = metrics.heightPixels; // 屏幕高度（像素）
        int screenWidth = width < height ? width : height;
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = (int) (screenWidth * 0.9f);
        params.height = (int) (screenWidth * 1.0f);
        mDialog.getWindow().setAttributes(params);
        View layout = LayoutInflater.from(SysApplication.getApplication()).inflate(R.layout.dialog_choose_role, null);
        GridView roles = (GridView) layout.findViewById(R.id.gv_dcr_roles);
        Button confirm = (Button) layout.findViewById(R.id.btn_dcr_start);
        final SelectRoleAdapter adapter = new SelectRoleAdapter(playBook, rolesSelected);
        roles.setAdapter(adapter);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RoleModel> selectedRoles = adapter.getSelectedRoles();
                for (int i = 0; i < selectedRoles.size(); i++) {
                    if (selectedRoles.get(i).isSelect) {
                        EventMessagePlayBook eventMessagePlayBook = new EventMessagePlayBook();
                        eventMessagePlayBook.setPlayBook(playBook);
                        eventMessagePlayBook.setSelectedRoles(selectedRoles);
                        Message message = new Message();
                        message.what = C.ROLEPLAY_SELECTEDROLE;
                        message.obj = eventMessagePlayBook;
                        handler.sendMessage(message);
                        dismissDialog();
                        return;
                    }
                    if (i == selectedRoles.size() - 1) {

                        Toast.makeText(SysApplication.getApplication(), "没有选中角色", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        mDialog.setContentView(layout);
    }

    /**
     * 角色扮演暂停对话框
     * @param context
     * @param handler
     */
    public static void createRoleActStopDialog(Context context, final Handler handler) {
        dismissDialog();
        mDialog = new AlertDialog.Builder(context, R.style.CustomDialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();
        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_role_act_stop, null);
        Button exit = (Button) layout.findViewById(R.id.btn_role_exit);
        Button restart = (Button) layout.findViewById(R.id.btn_role_restart);
        Button goContinue = (Button) layout.findViewById(R.id.btn_role_continue);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 退出
                handler.sendEmptyMessage(C.ROLE_ACT_EXIT);
                dismissDialog();
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 重新扮演
                handler.sendEmptyMessage(C.ROLE_ACT_RESTART);
                dismissDialog();
            }
        });
        goContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 继续扮演
                handler.sendEmptyMessage(C.ROLE_ACT_CONTINUE);
                dismissDialog();
            }
        });
        mDialog.setContentView(layout);
    }

    public static MaterialDialog showProgressDialog(Context context, String content) {
        return new MaterialDialog.Builder(context)
                .content(content)
                .progress(true, 0)
                .show();
    }

    public static MaterialDialog showProgressDialog(Context context, int contentResId) {
        return new MaterialDialog.Builder(context)
                .content(contentResId)
                .progress(true, 0)
                .show();
    }

    public interface IDialogButtonCallback {
        void onPositiveBtnClick();

        void onNegativeBtnClick();
    }

    //有确认和取消按钮
    public static MaterialDialog
        showConfirmDialog(Context context, String title, String content, String positiveText, String negativeText, final IDialogButtonCallback callback) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        if (callback != null) {
                            callback.onPositiveBtnClick();
                        }
                    }
                })
                .negativeText(negativeText)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        if (callback != null) {
                            callback.onNegativeBtnClick();
                        }
                    }
                })
                .show();
    }

    public static MaterialDialog showConfirmDialog(Context context, String title, String content, final IDialogButtonCallback callback) {
        return showConfirmDialog(context, title, content, "确认", "取消", callback);
    }

    public static MaterialDialog showConfirmDialog(Context context, String content, final IDialogButtonCallback callback) {
        return showConfirmDialog(context, null, content, callback);
    }

    //只有确认按钮
    public static MaterialDialog showPromptDialog(Activity activity, String title, String content) {
        return showPromptDialog(activity, title, content, null, true);
    }

    //只有确认按钮
    public static MaterialDialog showPromptDialog(Activity activity, String title, String content, final IDialogButtonCallback callback,
                                                  boolean canCancel) {
        //bad token when activity is finish
        if (activity.isFinishing()) {
            return null;
        }
        return new MaterialDialog.Builder(activity)
                .title(title)
                .content(content)
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        if (callback != null) {
                            callback.onPositiveBtnClick();
                        }
                    }
                })
                .cancelable(canCancel)
                .show();
    }

    public interface IListDialogItemCallback {
        void onListItemSelected(CharSequence text, int which);
    }

    public static MaterialDialog showListDialog(Context context, int arraysRes, String title, final IListDialogItemCallback callback) {
        return showListDialog(context, Arrays.asList(context.getResources().getStringArray(arraysRes)), title, callback);
    }

    public static MaterialDialog showListDialog(Context context, List<String> arraysRes, String title, final IListDialogItemCallback callback) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .items(arraysRes)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (callback != null) {
                            callback.onListItemSelected(text, which);
                        }
                    }
                })
                .show();
    }
}
