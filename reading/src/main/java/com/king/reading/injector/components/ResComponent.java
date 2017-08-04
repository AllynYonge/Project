package com.king.reading.injector.components;

import com.king.reading.injector.AppComponent;
import com.king.reading.injector.modules.ResModule;
import com.king.reading.injector.scope.PerActivity;
import com.king.reading.module.read.ReadFragment;

import dagger.Component;

/**
 * Created by AllynYonge on 20/07/2017.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = ResModule.class)
public interface ResComponent {
    void inject(ReadFragment fragment);
}
