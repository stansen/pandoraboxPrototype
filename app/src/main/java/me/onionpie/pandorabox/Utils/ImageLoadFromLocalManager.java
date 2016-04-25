package me.onionpie.pandorabox.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by wang on 2016/1/22.
 */
public class ImageLoadFromLocalManager {


    private static ImageLoadFromLocalManager sInstance;

    private LruBitmapCache mLruBitmapCache;

    private Context context;

    public static ImageLoadFromLocalManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ImageLoadFromLocalManager(context.getApplicationContext());
        }
        return sInstance;
    }

    private ImageLoadFromLocalManager(Context context) {
        this.context = context;
        mLruBitmapCache = new LruBitmapCache(context);
    }

    public <T> void loadBitmap(T url, ImageView imageView, CallBack<Bitmap> callBack){
        if (url != null){
            Bitmap bitmap = mLruBitmapCache.getBitmap(url instanceof String ? (String)url : ((Uri)url).getPath());
            if (bitmap == null){
                BitmapWorkerTask<T> task = new BitmapWorkerTask<>(imageView, callBack);
                task.execute(url);
            }
            else {
                imageView.setImageBitmap(bitmap);
                if (callBack != null){
                    callBack.success(bitmap);
                }
            }
        }
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

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

    public static Bitmap decodeSampledBitmapFromLocal(String url, int reqWidth, int reqHeight){
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

    public static Bitmap decodeSampledBitmapFromLocal(Uri uri, ContentResolver cr, int reqWidth, int reqHeight){

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
            e.printStackTrace();
        }

        return bitmap;
    }


    /**
     * 按质量压缩图片
     * @param bitmap 要压缩的图片
     * @param mega 压缩后的大小单位kb
     * **/
    public Bitmap compressWithQuality(Bitmap bitmap , int mega, Object url) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024 > mega  && options > 0) {
            Log.d("test baos", baos.toByteArray().length / 1024 + "  " + options);
            baos.reset();//重置baos即清空baos
            options -= 1;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        FileUtil.save(baos.toByteArray(), url, context);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (reqWidth == -1 && height > reqHeight){
            inSampleSize = (int)Math.ceil((double) height / (double) reqHeight);
        }
        else if (reqHeight == -1 && width > reqWidth){

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

    class BitmapWorkerTask<T> extends AsyncTask<T, Void, Bitmap> {

        WeakReference<ImageView> imageViewReference;

        CallBack<Bitmap> callBack;


        public BitmapWorkerTask(ImageView imageView, CallBack<Bitmap> callBack) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
            this.callBack = callBack;
        }

        // Decode image in background.

        @SafeVarargs
        @Override
        protected final Bitmap doInBackground(T... params) {
            Bitmap bitmap;
            int scale = 1;
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (params[0] instanceof Uri){
                bitmap = decodeSampledBitmapFromLocal((Uri) params[0], context.getContentResolver(), 1000, 1000);
                bitmap = compressWithQuality(bitmap, 100, params[0]);
                options.outHeight = bitmap.getHeight();
                options.outWidth = bitmap.getWidth();
                scale  = calculateInSampleSize(options, Dip2PxUtil.Dip2Px(context, 80), Dip2PxUtil.Dip2Px(context, 80));
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth() / scale, bitmap.getHeight() / scale);
                mLruBitmapCache.putBitmap(((Uri)params[0]).getPath(), bitmap);
            }
            else {
                bitmap = decodeSampledBitmapFromLocal((String) params[0], 1000, 1000);
                bitmap = compressWithQuality(bitmap, 100, params[0]);
                options.outHeight = bitmap.getHeight();
                options.outWidth = bitmap.getWidth();
                scale  = calculateInSampleSize(options, 192, 144);
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth() / scale, bitmap.getHeight() / scale);
                mLruBitmapCache.putBitmap((String) params[0], bitmap);
            }

            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (imageViewReference != null && bitmap != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    callBack.success(bitmap);
                }
                else {
                    callBack.success(bitmap);
                }
            }
            else {
                callBack.error();
            }
        }

    }

    public interface CallBack<T> {

        void success(T t);

        void error();
    }
}
