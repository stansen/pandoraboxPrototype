package com.rxjavahttprequest.imagecompressload;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jiudeng009 on 2016/4/20.
 */
public class LoadLocalImageWithCompressManager {

    private static LoadLocalImageWithCompressManager sInstance;

    private LruBitmapCache mLruBitmapCache;

    private Context context;

    public static LoadLocalImageWithCompressManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LoadLocalImageWithCompressManager.class){
                if (sInstance == null){
                    sInstance = new LoadLocalImageWithCompressManager(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private LoadLocalImageWithCompressManager(Context context) {
        this.context = context;
        mLruBitmapCache = new LruBitmapCache(context);
    }

    public <T> Subscription loadBitmap(final T url, final ImageView imageView, final CallBack<Bitmap> callBack){

        return Observable.just(url)
                .subscribeOn(Schedulers.io())
                .map(new Func1<T, Bitmap>() {
                    @Override
                    public Bitmap call(T t) {
                        Bitmap bitmap = mLruBitmapCache.getBitmap(url instanceof String ? (String) url : ((Uri) url).getPath());
                        if (bitmap == null) {
                            bitmap = getBitmap(url, imageView.getWidth(), imageView.getHeight());
                            Logger.t("from").d("disk");
                        } else {
                            Logger.t("from").d("lru");
                        }
                        return bitmap;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callBack != null){
                            callBack.error();
                        }
                        Logger.t("error").e(e.getMessage());
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        if (bitmap != null){
                            if (imageView != null){
                                imageView.setImageBitmap(bitmap);
                            }
                            if (callBack != null){
                                callBack.success(bitmap);
                            }
                        }
                        else {
                            Logger.t("error").e("error");
                            if (callBack != null){
                                callBack.error();
                            }
                        }

                    }
                });
    }

    /**
     * 获取图片并压缩
     * @param url
     * @param width 缩略图的宽
     * @param height 缩略图的高
     * @return
     */
    private Bitmap getBitmap(Object url, int width, int height){
        Bitmap bitmap;
        int scale = 1;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (url instanceof Uri){
                bitmap = decodeSampledBitmapFromLocal((Uri) url, context.getContentResolver(), 1000, 1000);
                bitmap = compressWithQuality(bitmap, 100, url);
                options.outHeight = bitmap.getHeight();
                options.outWidth = bitmap.getWidth();
                scale  = calculateInSampleSize(options, width, height);
//                Logger.d(width + "  " + height + " " + Dip2PxUtil.Dip2Px(context, 80));
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth() / scale, bitmap.getHeight() / scale);
                mLruBitmapCache.putBitmap(((Uri)url).getPath(), bitmap);
            }
            else {
                bitmap = decodeSampledBitmapFromLocal((String) url, 1000, 1000);
                bitmap = compressWithQuality(bitmap, 100, url);
                options.outHeight = bitmap.getHeight();
                options.outWidth = bitmap.getWidth();
//                Logger.d(width + "  " + height + " " + Dip2PxUtil.Dip2Px(context, 80));
                scale  = calculateInSampleSize(options, width, height);
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth() / scale, bitmap.getHeight() / scale);
                mLruBitmapCache.putBitmap((String) url, bitmap);
            }
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }

    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private Bitmap decodeSampledBitmapFromLocal(String url, int reqWidth, int reqHeight){
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;


        return BitmapFactory.decodeFile(url, options);
    }

    private Bitmap decodeSampledBitmapFromLocal(Uri uri, ContentResolver cr, int reqWidth, int reqHeight){

        Bitmap bitmap = null;
        try {
            InputStream input = cr.openInputStream(uri);
//            ParcelFileDescriptor fileDescriptor = cr.openFileDescriptor(uri, "r");
            if (input != null){
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(input, null, options);
                input.close();

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                input = cr.openInputStream(uri);
                if (input != null){
                    // Decode bitmap with inSampleSize set
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(input, null, options);
                    input.close();
                }
            }

        } catch (Exception e) {
            bitmap = null;
            e.printStackTrace();
        }

        return bitmap;
    }


    /**
     * 按质量压缩图片
     * @param bitmap 要压缩的图片
     * @param mega 压缩后的大小单位kb
     * **/
    private Bitmap compressWithQuality(Bitmap bitmap , int mega, Object url) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while ( baos.toByteArray().length / 1024 > mega  && options > 0) {
                baos.reset();//重置baos即清空baos
                options -= 1;//每次都减少1
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            }
            Logger.t("compress").d(baos.toByteArray().length / 1024 + "  " + options);
//            保存图片到本地
//            FileUtil.save(baos.toByteArray(), url, context);
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            return BitmapFactory.decodeStream(isBm, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (reqWidth == 0 && reqHeight == 0){
            inSampleSize = 1;
        }
        else if (reqWidth == 0 && height > reqHeight){
            inSampleSize = (int)Math.ceil((double) height / (double) reqHeight);
        }
        else if (reqHeight == 0 && width > reqWidth){

            inSampleSize = (int)Math.ceil((double) width / (double) reqWidth);
        }
        else if (height > reqHeight || width > reqWidth) {

            final int heightRatio = (int)Math.ceil((double) height / (double) reqHeight);
            final int widthRatio = (int)Math.ceil((double) width / (double) reqWidth);

            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    //    普通图片lru缓存图片加载技术
    public static class LruBitmapCache extends LruCache<String, Bitmap> {
        /**
         * @param maxSize for caches that do not override {@link #sizeOf}, this is
         *                the maximum number of entries in the cache. For all other caches,
         *                this is the maximum sum of the sizes of the entries in this cache.
         */

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        public LruBitmapCache(int maxSize) {
            super(maxSize);
        }

        public LruBitmapCache(Context ctx) {
            this(getCacheSize(ctx));
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }


        public Bitmap getBitmap(String url) {
            return get(url);
        }

        public void putBitmap(String url, Bitmap bitmap) {
            if (get(url) == null){
                put(url, bitmap);
            }
        }

        public static int getCacheSize(Context ctx) {
            final DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
            final int screenWidth = displayMetrics.widthPixels;
            final int screenHeight = displayMetrics.heightPixels;
            final int screenBytes = screenHeight * screenWidth;

            return screenBytes * 3;
        }

    }

    public interface CallBack<T> {

        void success(T t);

        void error();
    }
}
