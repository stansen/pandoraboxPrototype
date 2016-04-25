package me.onionpie.pandorabox.Rx.Event;

/**
 * Created by jiudeng009 on 2016/3/24.
 */
public class ProductDetailCountEvent {
    public int mCount;

    public ProductDetailCountEvent(int oldCount) {
        this.mCount = oldCount;
    }
}
