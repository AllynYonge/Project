package com.king.reading.module.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.EmptyUtils;
import com.king.reading.R;
import com.king.reading.base.fragment.RecyclerFragment;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.common.utils.DateUtils;
import com.king.reading.common.utils.DialogUtils;
import com.king.reading.common.utils.ToastUtils;
import com.king.reading.data.entities.NoticeEntity;
import com.king.reading.model.Notice;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 20/06/2017.
 */

public class NoticeFragment extends RecyclerFragment<NoticeEntity>  {
    private List<Notice> notices;
    private List<NoticeEntity> mNoticeEntities = new ArrayList<>();

    @Override
    public void onInitData(Bundle savedInstanceState) {
        notices = new ArrayList<>();
        notices.add(new Notice(true, "会员开通成功", "有效期：2017.01.03 至 2017.02.03\n支付金额：10元\n支付渠道：微信支付","2017.01.03 18:29"));
        notices.add(new Notice(false, "推荐好友，即可免费享受会员服务", "2017.01.01 至 2017.01.03 成功推荐好友注册，即可免费 获取1个月的会员服务！","2017.01.03 18:29"));

        NoticeAndAmusementActivity activity = (NoticeAndAmusementActivity) getActivity();
        activity.setOnMarkReadOnClickListener(new NoticeAndAmusementActivity.OnMarkReadOnClickListener() {
            @Override
            public void markRead() {
                DialogUtils.showConfirmDialog(getActivity(), "是否要全部标记消息已读", new DialogUtils.IDialogButtonCallback() {
                    @Override
                    public void onPositiveBtnClick() {

                    }

                    @Override
                    public void onNegativeBtnClick() {

                    }
                });
            }
        });
    }

    @Override
    public void onInitView() {
        super.onInitView();
        TitleBuilder.build(this)
                .setShowTitleBar(false)
                .init();

    }

    @Override
    public BaseQuickAdapter<NoticeEntity, BaseViewHolder> getAdapter() {
        return new BaseQuickAdapter<NoticeEntity, BaseViewHolder>(R.layout.item_notice, mNoticeEntities) {

            @Override
            protected void convert(BaseViewHolder helper, NoticeEntity item) {
                ExpandableTextView expandableTextView = helper.getView(R.id.expand_text_view);
                ImageView ivArrow= helper.getView(R.id.iv_arrow);
                if (TextUtils.isEmpty(item.jumpURL)){
                    ivArrow.setVisibility(View.INVISIBLE);
                }else{
                    ivArrow.setVisibility(View.VISIBLE);
                }
                helper.setText(R.id.tv_notice_title, item.title);
                expandableTextView.setText(item.content);
                helper.setText(R.id.tv_notice_time, DateUtils.formatTime(item.createTime,"yyyy-MM-dd HH:mm:ss"));
                helper.addOnClickListener(R.id.ll_item);
                helper.addOnLongClickListener(R.id.ll_item);
            }
        };
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        super.onItemChildClick(adapter, view, position);
        ToastUtils.show("消息已读，并进行跳转");
    }



    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                if (EmptyUtils.isNotEmpty(getActivity())){
                    NoticeAndAmusementActivity.class.cast(getActivity()).getUserRepository().getNotices(true).subscribe(new Consumer<List<NoticeEntity>>() {
                        @Override
                        public void accept(@NonNull List<NoticeEntity> noticeEntities) throws Exception {
                            Logger.d(noticeEntities);
                            refreshUI(noticeEntities);
                        }
                    });
                }
            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }
        };
    }


    @Override
    public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
        ToastUtils.show("长按了，弹出删除对话框");
        DialogUtils.showConfirmDialog(getActivity(), "是否要删除该条消息", new DialogUtils.IDialogButtonCallback() {
            @Override
            public void onPositiveBtnClick() {

            }

            @Override
            public void onNegativeBtnClick() {

            }
        });
        return true;
    }


}
