package com.king.reading.module.user;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.EmptyUtils;
import com.king.reading.R;
import com.king.reading.base.fragment.RecyclerFragment;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.data.entities.ActivityEntity;
import com.king.reading.model.Amusement;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 20/06/2017.
 */

public class AmusementFragment extends RecyclerFragment<Amusement>{
    private List<Amusement> amusements;
    @Override
    public void onInitData(Bundle savedInstanceState) {
        amusements = new ArrayList<>();
        amusements.add(new Amusement("","深圳市南山区第三届小学生电影配音大赛","2017.06.03 11:00"));
        amusements.add(new Amusement("","“金太阳杯”全国小学生英语朗读大赛","2017.01.03 18:29"));
    }

    @Override
    public void onInitView() {
        super.onInitView();
        TitleBuilder.build(this)
                .setShowTitleBar(false)
                .init();
    }

    @Override
    public BaseQuickAdapter<Amusement, BaseViewHolder> getAdapter() {
        return new BaseQuickAdapter<Amusement, BaseViewHolder>(R.layout.item_amusement, amusements) {
            @Override
            protected void convert(BaseViewHolder helper, Amusement item) {

            }
        };
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                if (EmptyUtils.isNotEmpty(getActivity())){
                    NoticeAndAmusementActivity.class.cast(getActivity()).getOtherRepository().getActivities(true).subscribe(new Consumer<List<ActivityEntity>>() {
                        @Override
                        public void accept(@NonNull List<ActivityEntity> activityEntities) throws Exception {
                            Logger.d(activityEntities);
                        }
                    });
                }
            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }
        };
    }

}
