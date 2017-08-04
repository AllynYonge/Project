package com.king.reading.model;

import com.blankj.utilcode.util.EmptyUtils;
import com.google.common.collect.Lists;
import com.king.reading.data.entities.BookEntity;
import com.king.reading.data.entities.ModuleEntity;
import com.king.reading.data.entities.UnitEntity;

import java.util.List;

/**
 * Created by AllynYonge on 29/07/2017.
 */

public class ViewMappers {
    public static List<ReadModuleModel> mapperReadModule(BookEntity bookEntity){
        List<ReadModuleModel> moduleModels = Lists.newArrayList();
        for (ModuleEntity moduleEntity : bookEntity.modules){
            List<ReadUnit> units = Lists.newArrayList();
            ReadModuleModel moduleModel = new ReadModuleModel(false, moduleEntity.title + "  " + moduleEntity.name, getPageRange(moduleEntity.start, moduleEntity.end), units);
            for (UnitEntity unitEntity : moduleEntity.units){
                ReadUnit readUnit = new ReadUnit(unitEntity.coverURL, unitEntity.title + unitEntity.name, getPageRange(unitEntity.start, unitEntity.end), unitEntity.unitId);
                units.add(readUnit);
            }
            moduleModels.add(moduleModel);
        }
        return moduleModels;
    }

    public static List<ReadModuleModel> mapperRolePlayModule(BookEntity bookEntity){
        List<ReadModuleModel> moduleModels = Lists.newArrayList();
        for (ModuleEntity moduleEntity : bookEntity.modules){
            List<ReadUnit> units = Lists.newArrayList();
            ReadModuleModel moduleModel = new ReadModuleModel(false, moduleEntity.title + "  " + moduleEntity.name, getPageRange(moduleEntity.start, moduleEntity.end), units);
            for (UnitEntity unitEntity : moduleEntity.units){
                if (unitEntity.canRolePlay){
                    ReadUnit readUnit = new ReadUnit(unitEntity.coverURL, unitEntity.title + unitEntity.name, getPageRange(unitEntity.start, unitEntity.end), unitEntity.unitId);
                    units.add(readUnit);
                }
            }
            if (EmptyUtils.isNotEmpty(units))
                moduleModels.add(moduleModel);
        }
        return moduleModels;
    }

    private static String getPageRange(int start, int end){
        StringBuilder builder = new StringBuilder();
        return builder.append("P").append(start).append("-").append(end).toString();
    }

    public static List<ListenModule> mapperListenModule(BookEntity bookEntity){
        List<ListenModule> listenModels = Lists.newArrayList();
        for (ModuleEntity moduleEntity : bookEntity.modules){
            List<ListenUnit> units = Lists.newArrayList();
            ListenModule listenModule = new ListenModule(false, false, moduleEntity.title + "  " + moduleEntity.name, getPageRange(moduleEntity.start, moduleEntity.end), units);
            for (UnitEntity unitEntity : moduleEntity.units){
                ListenUnit listenUnit = new ListenUnit(false, false, unitEntity.title + unitEntity.name, getPageRange(unitEntity.start, unitEntity.end), unitEntity.start, unitEntity.end);
                units.add(listenUnit);
            }
            listenModels.add(listenModule);
        }
        return listenModels;
    }
}
