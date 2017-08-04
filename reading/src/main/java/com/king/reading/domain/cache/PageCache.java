package com.king.reading.domain.cache;

import com.google.common.base.Optional;
import com.king.reading.data.entities.PageEntity;
import com.king.reading.data.repository.ResRepository;
import com.king.reading.domain.usecase.GetPage;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by AllynYonge on 25/07/2017.
 */

@Singleton
public class PageCache extends GuavaAbstractLoadingCache<GetPage.RequestValues,Optional<List<PageEntity>>> {

    private final ResRepository repository;

    @Inject
    public PageCache(ResRepository repository) {
        setMaximumSize(10);
        setExpireAfterWriteDuration(60);
        this.repository = repository;
    }


    @Override
    protected Optional<List<PageEntity>> fetchData(GetPage.RequestValues key) {
        boolean isValid = true;
        Optional<List<PageEntity>> optional = Optional.absent();
        if (isValid){
            optional = Optional.fromNullable(repository.queryPagesFromDb(key.getResourceId(), key.getStart(), key.getEnd()));
        }
        return optional;
    }
}
