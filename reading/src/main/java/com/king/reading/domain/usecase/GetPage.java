package com.king.reading.domain.usecase;

import com.google.common.base.Optional;
import com.king.reading.data.entities.PageEntity;
import com.king.reading.data.repository.ResRepository;
import com.king.reading.domain.cache.PageCache;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Maybe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;

/**
 * Created by AllynYonge on 20/07/2017.
 */

public class GetPage extends UseCase<GetPage.RequestValues> {

    private final ResRepository repository;
    private int resourceId;
    private PageCache cache;

    public GetPage(int resourceId, ResRepository repository, Scheduler threadExecutor, Scheduler postExecutionThread, PageCache cache) {
        super(threadExecutor, postExecutionThread);
        this.resourceId = resourceId;
        this.repository = repository;
        this.cache = cache;
    }

    @Override
    protected Maybe<List<PageEntity>> buildUseCaseObservable(final RequestValues requestValues) {
        requestValues.setResourceId(resourceId);
        try {
            return Maybe.concat(Maybe.just(cache.getValue(requestValues))
                    .filter(new AppendOnlyLinkedArrayList.NonThrowingPredicate<Optional<List<PageEntity>>>() {
                        @Override
                        public boolean test(Optional<List<PageEntity>> listOptional) {
                            return listOptional.isPresent() && !listOptional.get().isEmpty();
                        }
                    }).map(new Function<Optional<List<PageEntity>>, List<PageEntity>>() {
                        @Override
                        public List<PageEntity> apply(@NonNull Optional<List<PageEntity>> listOptional) throws Exception {
                            return listOptional.get();
                        }
                    }), repository.getPagesFromNet(resourceId, requestValues.getStart(), requestValues.getEnd()))
                    .firstElement();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return Maybe.empty();
        }
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final int mStart;
        private final int mEnd;
        private int resourceId;

        public RequestValues(int mStart, int mEnd) {
            this.mStart = mStart;
            this.mEnd = mEnd;
        }

        public int getStart() {
            return mStart;
        }

        public int getEnd() {
            return mEnd;
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RequestValues)) return false;

            RequestValues that = (RequestValues) o;

            if (mStart != that.mStart) return false;
            if (mEnd != that.mEnd) return false;
            return resourceId == that.resourceId;

        }

        @Override
        public int hashCode() {
            int result = mStart;
            result = 31 * result + mEnd;
            result = 31 * result + resourceId;
            return result;
        }
    }
}
