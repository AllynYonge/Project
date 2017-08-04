package com.king.reading.module.user;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.common.adapter.expandanim.ExpandableViewHoldersUtil;
import com.king.reading.model.Help;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AllynYonge on 22/06/2017.
 */
@Route(path = C.ROUTER_FEEDBACK)
public class HelpAndFeedbackActivity extends RecyclerViewActivity<Help>{
    final ExpandableViewHoldersUtil.KeepOneH<BaseViewHolder> keepOne = new ExpandableViewHoldersUtil.KeepOneH<BaseViewHolder>();
    private List<Help> helps;

    @Override
    public void onInitData(Bundle savedInstanceState) {
        helps = new ArrayList<>();
        helps.add(new Help(true, "1.忘记密码怎么办？", new ArrayList<String>(){{
            add("第一步：点击登陆页面的“忘记密码”；第二步： 在找回密码页面输入注册的手机号，点击“立即获 取”，输入收到的验证码点击“下一步”；第三步： 输入新密码，确认密码，点击“确定”，密码修改 成功。");
        }}));
        helps.add(new Help(false, "2.怎样修改密码？", new ArrayList<String>(){{
            add("第一步：点击登陆页面的“忘记密码”；第二步： 在找回密码页面输入注册的手机号，点击“立即获 取”，输入收到的验证码点击“下一步”；第三步： 输入新密码，确认密码，点击“确定”，密码修改 成功。");
        }}));
        helps.add(new Help(false, "3.若注册是提示“手机号已被注册”怎么办？", new ArrayList<String>(){{
            add("第一步：点击登陆页面的“忘记密码”；第二步： 在找回密码页面输入注册的手机号，点击“立即获 取”，输入收到的验证码点击“下一步”；第三步： 输入新密码，确认密码，点击“确定”，密码修改 成功。");
        }}));
        helps.add(new Help(false, "4.为什么没有其他教材版本？", new ArrayList<String>(){{
            add("第一步：点击登陆页面的“忘记密码”；第二步： 在找回密码页面输入注册的手机号，点击“立即获 取”，输入收到的验证码点击“下一步”；第三步： 输入新密码，确认密码，点击“确定”，密码修改 成功。");
        }}));
    }

    @Override
    public void onInitView() {
        super.onInitView();
        setCenterTitle("帮助与反馈");
        setLeftImageIcon(R.mipmap.ic_back);
        setEnableRefresh(false);
    }

    @Override
    public BaseQuickAdapter<Help, BaseViewHolder> getAdapter() {
        return new BaseQuickAdapter<Help, BaseViewHolder>(R.layout.item_help, helps) {
            @Override
            protected void convert(final BaseViewHolder helper, final Help item) {
                final FrameLayout containerLayout = helper.getView(R.id.frame_help_container);
                if (item.isExpand()) ExpandableViewHoldersUtil.openH(helper, containerLayout, false);
                else ExpandableViewHoldersUtil.closeH(helper, containerLayout, false);
                helper.setOnClickListener(R.id.tv_help_question, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keepOne.toggle(helper, containerLayout, item);
                    }
                });

                helper.setText(R.id.tv_help_question, item.question);
                helper.setText(R.id.tv_help_answer, item.items.get(0));
            }
        };
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }

        };
    }

    @Override
    public int getContentView() {
        return R.layout.activity_help;
    }
}
