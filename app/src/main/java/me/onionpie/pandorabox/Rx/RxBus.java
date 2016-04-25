package me.onionpie.pandorabox.Rx;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by jiudeng007 on 2016/1/18.
 */
public class RxBus {
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());
    private static RxBus mRxBus = null;

    public static RxBus getInstance() {

        synchronized (RxBus.class) {
            {
                if (mRxBus == null) {
                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;
    }

    public void send(Object o) {
        if (hasObservers()){
            _bus.onNext(o);
        }
    }
//    public Observable<Object> toObserverable() {
//        return _bus;
//    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T extends Object> Observable<T> toObserverable(final Class<T> eventType) {
        return _bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return eventType.isInstance(o);
            }
        }).cast(eventType);
    }
}
