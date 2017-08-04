package com.king.reading.module.user;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.king.reading.C;
import com.king.reading.Navigation;
import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.Check;
import com.king.reading.common.utils.DialogUtils;
import com.king.reading.common.utils.ToastUtils;
import com.king.reading.data.entities.CityEntity;
import com.king.reading.data.entities.DistrictEntity;
import com.king.reading.data.entities.ProvinceEntity;
import com.king.reading.data.entities.SchoolEntity;
import com.king.reading.data.entities.SchoolEntity_Table;
import com.king.reading.data.repository.UserRepository;
import com.king.reading.widget.pickerview.OptionsPickerView;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function4;

/**
 * Created by AllynYonge on 21/06/2017.
 */

@Route(path = C.ROUTER_COMPLETION_PROFILE)
public class SupplementProfileActivity extends BaseActivity {
    @Inject UserRepository repository;
    @Inject Navigation navigation;
    private OptionsPickerView pvOptions;
    @BindView(R.id.tv_supplement_next)
    public TextView next;
    @BindView(R.id.tv_supplement_area)
    public TextView areaView;
    @BindView(R.id.tv_supplement_school)
    public TextView schoolView;
    @BindView(R.id.tv_supplement_name)
    public TextView studentView;
    @BindView(R.id.tv_supplement_class)
    public TextView classView;

    private List<String> provinces = new ArrayList<>();
    private List<List<String>> cities = new ArrayList<>();
    private List<List<List<DistrictEntity>>> areas = new ArrayList<>();
    private List<String> schools = new ArrayList<>();
    private int districtCode;
    private long schoolId;

    @Override
    public void onInitData(Bundle savedInstanceState) {
        getAppComponent().plus().inject(this);
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                Logger.d(areas.get(options1).get(options2).get(options3).areaCode);
                districtCode = areas.get(options1).get(options2).get(options3).areaCode;
                areaView.setText(provinces.get(options1)
                        + cities.get(options1).get(options2)
                        + areas.get(options1).get(options2).get(options3).getPickerViewText());
                repository.getSchools(districtCode).subscribe(new Consumer<List<SchoolEntity>>() {
                    @Override
                    public void accept(@NonNull List<SchoolEntity> schoolEntities) throws Exception {
                        schools.clear();
                        for (SchoolEntity entity : schoolEntities) {
                            schools.add(entity.name);
                        }
                    }
                });
            }
        })
                .setTitleText("城市地区")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(R.color.gray_30)//设置分割线的颜色
                .setSelectOptions(0, 0, 0)//默认选中项
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();

        Observable.combineLatest(RxTextView.afterTextChangeEvents(studentView), RxTextView.afterTextChangeEvents(areaView),
                RxTextView.afterTextChangeEvents(schoolView), RxTextView.afterTextChangeEvents(classView),
                new Function4<TextViewAfterTextChangeEvent, TextViewAfterTextChangeEvent, TextViewAfterTextChangeEvent, TextViewAfterTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull TextViewAfterTextChangeEvent studentName, @NonNull TextViewAfterTextChangeEvent areaName, @NonNull TextViewAfterTextChangeEvent schoolName, @NonNull TextViewAfterTextChangeEvent className) throws Exception {
                        return !(areaName.view().getText().toString().isEmpty() ||
                                schoolName.view().getText().toString().isEmpty() ||
                                studentName.view().getText().toString().isEmpty() ||
                                className.view().getText().toString().isEmpty());
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                next.setEnabled(aBoolean);
            }
        });
    }

    @Override
    public void onInitView() {
        setCenterTitle("完善资料");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_supplement_profile;
    }

    @OnClick(R.id.tv_supplement_area)
    public void supplementArea(View view) {
        showProgressDialog("正在加载地区数据，请稍后...");
        repository.getAreaName().subscribe(new Consumer<List<ProvinceEntity>>() {
            @Override
            public void accept(@NonNull List<ProvinceEntity> provinceEntities) throws Exception {
                dismissProgressDialog();
                for (ProvinceEntity province : provinceEntities) {
                    List<String> cityList = new ArrayList<String>();
                    List<List<DistrictEntity>> test = new ArrayList<>();
                    for (CityEntity city : province.citys) {
                        List<DistrictEntity> districtList = new ArrayList<>();
                        for (DistrictEntity district : city.districts) {
                            districtList.add(district);
                        }
                        cityList.add(city.name);
                        test.add(districtList);
                    }
                    provinces.add(province.name);
                    cities.add(cityList);
                    areas.add(test);
                }
                pvOptions.setPicker(provinces, cities, areas);//二级选择器
                pvOptions.show();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                dismissProgressDialog();
                Logger.e(throwable.getMessage());
            }
        });
    }

    @OnClick(R.id.tv_supplement_school)
    public void supplementSchool(View view) {
        if (areaView.getText().toString().isEmpty()) {
            ToastUtils.show("请先选择地区");
            return;
        }

        if (Check.isEmpty(schools)){
            ToastUtils.show("");
            return;
        }

        DialogUtils.showListDialog(this, schools, "选择学校", new DialogUtils.IListDialogItemCallback() {
            @Override
            public void onListItemSelected(CharSequence text, int which) {
                schoolId = SQLite.select(SchoolEntity_Table.schoolId).from(SchoolEntity.class).where(SchoolEntity_Table.name.eq(text.toString())).querySingle().schoolId;
                schoolView.setText(text);
            }
        });
    }

    @OnClick(R.id.tv_supplement_next)
    public void next(View view) {
        showProgressDialog("正在更新信息，请稍后...");
        repository.updateUserInfo(studentView.getText().toString().trim(),
                classView.getText().toString().trim(),
                0l, schoolId, districtCode, "")
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        dismissProgressDialog();
                        navigation.routerUploadAvatarAct();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissProgressDialog();
                        ToastUtils.show("更新信息失败，请重试");
                        Logger.e(throwable.getMessage());
                    }
                });
    }

}
