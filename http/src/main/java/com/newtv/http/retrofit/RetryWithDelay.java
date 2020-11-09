package com.newtv.http.retrofit;



import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {

    private final int maxRetryCount;
    private final int retryDelay;
    private int retryCount;
    private TimeUnit timeUnit;

    public RetryWithDelay(final int maxRetryCount, final int retryDelay, final TimeUnit timeUnit) {
        this.maxRetryCount = maxRetryCount;
        this.retryDelay = retryDelay;
        this.timeUnit = timeUnit;
        this.retryCount = 0;
    }

    @Override
    public Observable<?> apply(final Observable<? extends Throwable> attempts) {
        return attempts.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
            if (++retryCount < maxRetryCount & canRetry(throwable)) {
                return Observable.timer(retryDelay, timeUnit);
            }
            return Observable.error(throwable);
        });
    }

    private boolean canRetry(Throwable throwable) {
        return throwable instanceof TimeoutException
                || throwable instanceof IOException;
    }
}
