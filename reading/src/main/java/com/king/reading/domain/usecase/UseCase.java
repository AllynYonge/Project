package com.king.reading.domain.usecase;


import io.reactivex.Maybe;
import io.reactivex.Scheduler;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;

public abstract class UseCase <Q extends UseCase.RequestValues>{

    private Q mRequestValues;
    private final Scheduler threadExecutor;
    private final Scheduler postExecutionThread;

    protected UseCase(Scheduler threadExecutor,
                      Scheduler postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    public void setRequestValues(Q requestValues) {
        mRequestValues = requestValues;
    }

    public Q getRequestValues() {
        return mRequestValues;
    }

    protected abstract Maybe buildUseCaseObservable(Q requestValues);

    @SuppressWarnings("unchecked")
    public void execute(UseCase.RequestValues requestValues, Consumer onSuccess, Consumer<? super Throwable> onError,
                        Action onComplete) {
        this.mRequestValues = (Q) requestValues;
        this.buildUseCaseObservable(mRequestValues)
            .subscribeOn(threadExecutor)
            .observeOn(postExecutionThread)
            .subscribe(onSuccess, onError, onComplete);
    }

    @SuppressWarnings("unchecked")
    public void execute(UseCase.RequestValues requestValues, Consumer onSuccess, Consumer<? super Throwable> onError) {
        this.execute(requestValues,onSuccess, onError, Functions.EMPTY_ACTION);
    }

    @SuppressWarnings("unchecked")
    public void execute(UseCase.RequestValues requestValues, Consumer onSuccess) {
        this.execute(requestValues,onSuccess, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @SuppressWarnings("unchecked")
    public void execute(Consumer onSuccess, Consumer<? super Throwable> onError) {
        this.execute(null,onSuccess, onError);
    }

    @SuppressWarnings("unchecked")
    public void execute(Consumer onSuccess) {
        this.execute(onSuccess, Functions.ON_ERROR_MISSING);
    }

    /**
     * Data passed to a request.
     */
    public interface RequestValues {
    }
}

