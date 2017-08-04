package com.king.reading.injector.modules;

import com.king.reading.data.repository.ResRepository;
import com.king.reading.domain.cache.PageCache;
import com.king.reading.domain.usecase.GetPage;
import com.king.reading.domain.usecase.UseCase;
import com.king.reading.injector.scope.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

/**
 * Created by AllynYonge on 03/07/2017.
 */

@Module
public class ResModule {

    private int resourceId;

    public ResModule() {
    }

    public ResModule(int resourceId) {
        this.resourceId = resourceId;
    }

    @Provides
    @PerActivity
    @Named("getPage")
    UseCase provideGetPageCase(ResRepository repository, @Named("io") Scheduler threadExecutor, @Named("main") Scheduler postExecutionThread, PageCache cache){
        return new GetPage(resourceId, repository, threadExecutor, postExecutionThread, cache);
    }
}
