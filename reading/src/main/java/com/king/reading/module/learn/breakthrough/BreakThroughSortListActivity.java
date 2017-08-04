package com.king.reading.module.learn.breakthrough;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseMultiItemQuickAdapter;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.data.entities.PlayerEntities;
import com.king.reading.ddb.Player;
import com.king.reading.model.SortList;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 14/06/2017.
 */

@Route(path = C.ROUTER_LEARN_BREAK_SORT)
public class BreakThroughSortListActivity extends RecyclerViewActivity<SortList>{
    private List<SortList> list;
    @Override
    public BaseQuickAdapter<SortList, BaseViewHolder> getAdapter() {
        BaseQuickAdapter adapter = new ExamineSortAdapter(list);
        return adapter;
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                getUserRepository().getReadAfterMeGameBoard().subscribe(new Consumer<PlayerEntities>() {
                    @Override
                    public void accept(@NonNull PlayerEntities playerEntities) throws Exception {
                        list.clear();
                        for (int i = 0; i < playerEntities.gameBoard.getPlayers().size(); i++) {
                            Player player = playerEntities.gameBoard.getPlayers().get(i);
                            int type = SortList.DEFAULT;
                            if (i == 0){
                                type = SortList.MINE;
                            } else if (i == 1){
                                type = SortList.FIRST;
                            } else if (i == 2){
                                type = SortList.THIRD;
                            }
                            list.add(new SortList(player.getRank()+"",player.getName(), player.getOtainedStar()+"", player.completedMission+"",type));
                        }
                        refreshUI(list);
                    }
                });
            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }
        };
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        list = new ArrayList<>();
        list.add(new SortList("1023","杨虎", "200" , "23", SortList.MINE));
        list.add(new SortList("1","杨虎", "200" , "23", SortList.FIRST));
        list.add(new SortList("2","杨虎", "200" , "23", SortList.SECOND));
        list.add(new SortList("3","杨虎", "200" , "23", SortList.THIRD));
        list.add(new SortList("4","杨虎", "200" , "23", SortList.DEFAULT));
        list.add(new SortList("5","杨虎", "200" , "23", SortList.DEFAULT));
        list.add(new SortList("6","杨虎", "200" , "23", SortList.DEFAULT));
        list.add(new SortList("7","杨虎", "200" , "23", SortList.DEFAULT));
        list.add(new SortList("8","杨虎", "200" , "23", SortList.DEFAULT));
        list.add(new SortList("9","杨虎", "200" , "23", SortList.DEFAULT));
        list.add(new SortList("10","杨虎", "200" , "23", SortList.DEFAULT));
        list.add(new SortList("11","杨虎", "200" , "23", SortList.DEFAULT));
        list.add(new SortList("12","杨虎", "200" , "23", SortList.DEFAULT));
        list.add(new SortList("13","杨虎", "200" , "23", SortList.DEFAULT));

    }

    @Override
    public void onInitView() {
        super.onInitView();
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("星星排行榜");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_breakthrough_sort;
    }

    @Override
    public int getVerticalInterval() {
        return 0;
    }

    private class ExamineSortAdapter extends BaseMultiItemQuickAdapter<SortList, BaseViewHolder>{

        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param data A new list is created out of this one to avoid mutable list
         */
        public ExamineSortAdapter(List<SortList> data) {
            super(data);
            addItemType(SortList.MINE,R.layout.item_breakthrough_sort_mine);
            addItemType(SortList.FIRST,R.layout.item_breakthrough_sort_topthree);
            addItemType(SortList.SECOND,R.layout.item_breakthrough_sort_topthree);
            addItemType(SortList.THIRD,R.layout.item_breakthrough_sort_topthree);
            addItemType(SortList.DEFAULT,R.layout.item_breakthrough_sort_default);
        }

        @Override
        protected void convert(BaseViewHolder helper, SortList item) {
            if (helper.getAdapterPosition() == 0){
                helper.setBackgroundRes(R.id.ll_breakthrough_sort_item, R.color.cyan_70);
            } else {
                int color = helper.getAdapterPosition() % 2 == 0 ? R.color.cyan : R.color.cyan_65;
                helper.setBackgroundRes(R.id.ll_breakthrough_sort_item, color);
            }
            helper.setText(R.id.tv_breakthrough_sort_num, item.passNum);
            helper.setText(R.id.tv_breakthrough_sort_star, item.starNum);
            switch (item.getItemType()){
                case SortList.MINE:
                    helper.setText(R.id.tv_breakthrough_sort_ranking, item.number);
                    break;
                case SortList.FIRST:
                    helper.setImageResource(R.id.image_breakthrough_sort_top, R.mipmap.ic_ranking_one);
                    helper.setText(R.id.tv_breakthrough_sort_name, item.name);
                    break;
                case SortList.SECOND:
                    helper.setImageResource(R.id.image_breakthrough_sort_top, R.mipmap.ic_ranking_two);
                    helper.setText(R.id.tv_breakthrough_sort_name, item.name);
                    break;
                case SortList.THIRD:
                    helper.setImageResource(R.id.image_breakthrough_sort_top, R.mipmap.ic_ranking_three);
                    helper.setText(R.id.tv_breakthrough_sort_name, item.name);
                    break;
                case SortList.DEFAULT:
                    helper.setText(R.id.tv_breakthrough_sort_name, item.name);
                    helper.setText(R.id.tv_breakthrough_sort_ranking, item.number);
                    break;
            }
        }
    }
}
