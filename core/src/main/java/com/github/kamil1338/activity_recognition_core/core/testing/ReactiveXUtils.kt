package com.github.kamil1338.activity_recognition_core.core.testing

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.functions.Action
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.rules.ExternalResource

fun <T> testObserver(doOnDispose: Action): Observable<T> =
    PublishSubject.create<T>().doOnDispose(doOnDispose)

fun <T> testObserverMaybe(doOnDispose: Action): Maybe<T> =
    testObserver<T>(doOnDispose).firstElement()

class ReactiveXUtils {

    private fun setupJavaSchedulers(scheduler: Scheduler) {
        RxJavaPlugins.setIoSchedulerHandler { scheduler }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
        RxJavaPlugins.setSingleSchedulerHandler { scheduler }
    }

    private fun setupAndroidSchedulers(scheduler: Scheduler) {
        RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }
    }

    private fun resetSchedulers() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    fun getTestRule(scheduler: Scheduler = Schedulers.trampoline()) = object : ExternalResource() {
        override fun before() {
            setupAndroidSchedulers(scheduler)
            setupJavaSchedulers(scheduler)
        }

        override fun after() {
            resetSchedulers()
        }
    }
}