/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This is a lightly edited version from StableKernel/RxEspresso, https://github.com/stablekernel/RxEspresso.
 * Give them all the love.
 */

package com.bitfarmsoftware.starwars.util.rx;

import android.support.test.espresso.IdlingResource;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;
import rx.plugins.RxJavaObservableExecutionHook;
import rx.plugins.RxJavaPlugins;


/**
 * Provides the hooks for both RxJava and Espresso so that Espresso knows when to wait
 * until RxJava subscriptions have completed.
 */

public final class RxIdlingResource extends RxJavaObservableExecutionHook implements IdlingResource {
    public static final String TAG = "RxIdlingResource";

    private final AtomicInteger subscriptions = new AtomicInteger(0);

    private static RxIdlingResource INSTANCE;

    private ResourceCallback resourceCallback;

    private RxIdlingResource() {
        //private
    }

    public static RxIdlingResource get() {
        if (INSTANCE == null) {
            Log.d(TAG, "CREATING NEW INSTANCE OF RXIDLINGRESOURCE");
            INSTANCE = new RxIdlingResource();
        }
        return INSTANCE;
    }

    /* ======================== */
    /* IdlingResource Overrides */
    /* ======================== */

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public boolean isIdleNow() {
        int activeSubscriptionCount = subscriptions.get();
        boolean isIdle = activeSubscriptionCount == 0;

        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    /* ======================================= */
    /* RxJavaObservableExecutionHook Overrides */
    /* ======================================= */

    @Override
    public <T> Observable.OnSubscribe<T> onSubscribeStart(Observable<? extends T> observableInstance,
                                                          final Observable.OnSubscribe<T> onSubscribe) {
        int activeSubscriptionCount = subscriptions.incrementAndGet();

        return new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                onSubscribe.call(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                        onFinally(onSubscribe, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                        onFinally(onSubscribe, "onError");
                    }

                    @Override
                    public void onNext(T t) {
                        subscriber.onNext(t);
                    }
                });
            }
        };
    }

    private <T> void onFinally(Observable.OnSubscribe<T> onSubscribe, final String finalizeCaller) {
        int activeSubscriptionCount = subscriptions.decrementAndGet();
        if (activeSubscriptionCount == 0) {
            Log.d(TAG, "onTransitionToIdle");
            if(resourceCallback != null) {
                resourceCallback.onTransitionToIdle();
            }
        }
    }
}