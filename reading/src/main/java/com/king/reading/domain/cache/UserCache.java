package com.king.reading.domain.cache;

import com.google.common.base.Optional;
import com.king.reading.data.entities.UserEntity;
import com.king.reading.data.entities.UserEntity_Table;
import com.king.reading.exception.UserNotFoundException;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by AllynYonge on 24/07/2017.
 */

@Singleton
public class UserCache extends GuavaAbstractLoadingCache<Long, UserEntity>{

    @Inject
    public UserCache() {
        setExpireAfterWriteDuration(Integer.MAX_VALUE);
    }

    @Override
    protected UserEntity fetchData(Long key) {
        return getLoginUserFromDb();
    }

    private UserEntity getLoginUserFromDb(){
        return select().from(UserEntity.class).where(UserEntity_Table.token.notEq("")).querySingle();
    }

    public Optional<UserEntity> getLoginUser() {
        try {
            return Optional.of(getValue(0l));
        } catch (ExecutionException e) {
            e.printStackTrace();
            new UserNotFoundException("请重新登录");
        }
        return null;
    }

}
