package com.king.reading.injector;

import dagger.MapKey;

/**
 * Created by AllynYonge on 29/06/2017.
 */

@MapKey(unwrapValue = true)
public @interface StoreKey {
    String value();
}
