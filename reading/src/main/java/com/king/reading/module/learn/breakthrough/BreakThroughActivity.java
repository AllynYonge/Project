package com.king.reading.module.learn.breakthrough;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.EmptyUtils;
import com.google.common.collect.Lists;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseLinearGridAdapter;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.data.entities.ReadAfterMeEntity;
import com.king.reading.ddb.Mission;
import com.king.reading.ddb.Part;
import com.king.reading.model.BreakThroughUnit;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 15/06/2017.
 */

@Route(path = C.ROUTER_LEARN_BREAK)
public class BreakThroughActivity extends RecyclerViewActivity<BreakThroughUnit>{
    private List<BreakThroughUnit> units;
    private TextView appraise;
    private TextView totalStar;
    private TextView todayStar;
    private TextView completedMission;

    @Override
    public void onInitData(Bundle savedInstanceState) {
        units = new ArrayList<>();

    }

    @Override
    public void onInitView() {
        super.onInitView();
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("跟读闯关");
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.item_header_breakthrough, null);
        getRecyclerAdapter().addHeaderView(headerView);
        appraise = (TextView) headerView.findViewById(R.id.tv_breakthrough_header_appraise);
        todayStar = (TextView) headerView.findViewById(R.id.tv_breakthrough_header_todayStar);
        completedMission = (TextView) headerView.findViewById(R.id.tv_breakthrough_header_completedMission);
        totalStar = (TextView) headerView.findViewById(R.id.tv_breakthrough_header_totalStar);
        appraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNavigation().routerBreakSort();
            }
        });
    }

    @Override
    public BaseQuickAdapter<BreakThroughUnit, BaseViewHolder> getAdapter() {
        return new BaseLinearGridAdapter<BreakThroughUnit>(R.layout.item_breakthrough, units, BaseLinearGridAdapter.HORIZONTAL, 2) {
            @Override
            protected int getChildLayoutRes() {
                return R.layout.item_breakthrough_subitem;
            }

            @Override
            protected void bindChildData(ViewGroup childHolder, BreakThroughUnit group, int index) {
                BreakThroughUnit.SubUnit subUnit = group.getItems().get(index);
                TextView mission = (TextView) childHolder.findViewById(R.id.tv_breakthrough_sub_mission);
                TextView star = (TextView) childHolder.findViewById(R.id.tv_breakthrough_sub_star);
                ProgressBar progressBar = (ProgressBar) childHolder.findViewById(R.id.progress_breakthrough_score);
                mission.setText(subUnit.SubUnitName);
                star.setText(subUnit.starNum+"");
                progressBar.setMax(subUnit.maxStarNum);
                progressBar.setProgress(subUnit.starNum);
            }

            @Override
            protected void convert(BaseViewHolder helper, BreakThroughUnit item) {
                super.convert(helper, item);
                helper.setText(R.id.tv_breakthrough_item_unit, item.unitName);
            }
        };
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                getUserRepository().getReadAfterMe().subscribe(new Consumer<ReadAfterMeEntity>() {
                    @Override
                    public void accept(@NonNull ReadAfterMeEntity readAfterMeEntity) throws Exception {
                        if (EmptyUtils.isNotEmpty(appraise)){
                            appraise.setText(readAfterMeEntity.readAfterMe.appraise);
                            totalStar.setText(readAfterMeEntity.readAfterMe.totalStar + "");
                            todayStar.setText(readAfterMeEntity.readAfterMe.todayStar + "");
                            completedMission.setText(readAfterMeEntity.readAfterMe.completedMission + "");
                        }

                        units.clear();
                        for (Part part : readAfterMeEntity.readAfterMe.parts) {
                            List<BreakThroughUnit.SubUnit> subUnits = Lists.newArrayList();
                            for (Mission mission : part.getMissions()) {
                                subUnits.add(new BreakThroughUnit.SubUnit(mission.title, mission.otainedStar, mission.totalStar, mission.missionID));
                            }
                            BreakThroughUnit throughUnit = new BreakThroughUnit(part.title, subUnits);
                            units.add(throughUnit);
                        }
                        refreshUI(units);
                    }
                });
            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }
        };
    }

    @Override
    public int getContentView() {
        return R.layout.activity_breakthrough;
    }
}
