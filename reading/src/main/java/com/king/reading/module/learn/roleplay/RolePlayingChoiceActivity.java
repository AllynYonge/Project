package com.king.reading.module.learn.roleplay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.common.utils.DialogUtils;
import com.king.reading.data.entities.PlayBooksEntity;
import com.king.reading.data.repository.ResRepository;
import com.king.reading.ddb.PlayBook;
import com.king.reading.model.EventMessagePlayBook;
import com.king.reading.model.RoleModel;
import com.king.reading.model.UnitDetail;
import com.king.reading.widget.drawer.util.DensityUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.king.reading.C.ROUTER_LEARN_ROLEPLAY_UNIT;

/**
 * Created by hu.yang on 2017/6/13.
 */

@Route(path = ROUTER_LEARN_ROLEPLAY_UNIT)
public class RolePlayingChoiceActivity extends RecyclerViewActivity<UnitDetail>{
    private List<UnitDetail> units = Lists.newArrayList();
    private Map<String, List<RoleModel>> mSelectedRoles = Maps.newHashMap();
    @Inject ResRepository resRepository;
    private List<PlayBook> mPlayBooks;
    @Autowired
    public long unitId;

    @Override
    public void onInitData(Bundle savedInstanceState) {
    }

    @Override
    public void onInitView() {
        ARouter.getInstance().inject(this);
        super.onInitView();
        getAppComponent().plus().inject(this);
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("Unit2");
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case C.ROLEPLAY_SELECTEDROLE:
                    EventMessagePlayBook eventMessagePlayBook = (EventMessagePlayBook) msg.obj;
                    EventBus.getDefault().postSticky(eventMessagePlayBook);
                    startActivity(new Intent(RolePlayingChoiceActivity.this,RolePlayingDetailActivity.class));
                    finish();
                    break;
            }
        }
    };

    @Override
    public BaseQuickAdapter<UnitDetail, BaseViewHolder> getAdapter() {
        return new BaseQuickAdapter<UnitDetail, BaseViewHolder>(R.layout.item_roleplaying_choice, units) {
            @Override
            protected void convert(BaseViewHolder helper, UnitDetail item) {
                helper.setText(R.id.tv_rolePlay_unitDetail_title, item.UnitName);
                ImageView imageView = helper.getView(R.id.image_rolePlay_unitDetail_intro);
                Glide.with(RolePlayingChoiceActivity.this).load(item.url).into(imageView);
                helper.addOnClickListener(R.id.image_rolePlay_unitDetail_intro);
            }
        };
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        UnitDetail detail = (UnitDetail) adapter.getItem(position);
        Toast.makeText(SysApplication.getApplication(),"选择了孩子"+position,Toast.LENGTH_SHORT).show();
        DialogUtils.chooseRoleDialog(this,mPlayBooks.get(position), mSelectedRoles.get(detail.UnitName),mHandler);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_role_play_choice;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getApplicationContext(), 2);
    }

    @Override
    public int getVerticalInterval() {
        return DensityUtils.dp2px(getApplicationContext(),16);
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                getResRepository().getPlayBooks(unitId).subscribe(new Consumer<PlayBooksEntity>() {
                    @Override
                    public void accept(@NonNull PlayBooksEntity playBooksEntity) throws Exception {
                        mPlayBooks = playBooksEntity.playBooks.playBooks;
                        for (PlayBook playBook : playBooksEntity.playBooks.playBooks) {
                            units.add(new UnitDetail(playBook.title, playBook.coverURL));
                            List<RoleModel> roleModels = Lists.newArrayList();
                            for (String role : playBook.getRoles()) {
                                RoleModel roleModel = new RoleModel(role, false);
                                roleModels.add(roleModel);
                            }
                            mSelectedRoles.put(playBook.title, roleModels);
                        }
                        refreshUI(units);
                        Logger.d(new Gson().toJson(playBooksEntity));
                    }
                });
            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }
        };
    }
}
