package com.sarkisian.gh.util.rxbus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class RxBus {

    val bus: PublishSubject<Any> = PublishSubject.create<Any>()

    fun post(any: Any) = bus.onNext(any)

    fun observable(): Observable<Any> = bus

}