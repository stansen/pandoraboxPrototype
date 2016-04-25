package me.onionpie.pandorabox.Utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by wang on 2016/1/27.
 */
public class DownloadUtil {

    private DownloadManager mDownloadManager;
    private DownloadManager.Request mRequest;
    private Context mContext;
    private Handler handler;
    private Map<Long, ScheduledFuture> futureCache;
    private ScheduledExecutorService service;

    public DownloadUtil(Context context, Handler handler){
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        this.mContext = context;
        this.handler = handler;
        futureCache = new WeakHashMap<>();
        service = Executors.newScheduledThreadPool(3);
    }

    public DownloadUtil getRequest(String uri){
        mRequest = new DownloadManager.Request(Uri.parse(uri));
        return this;
    }

    public DownloadUtil setAllowedNetwork(boolean wifi, boolean mobile){
        if (wifi && mobile){
            mRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        }
        else if (wifi){
            mRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        }
        else if (mobile){
            mRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        }
        return this;
    }

    public DownloadUtil setSaveFileDir(String dirName, String fileName, boolean root){
        if (root){
            File file_dir = Environment.getExternalStoragePublicDirectory(dirName);
            if (!file_dir.exists()){
                file_dir.mkdir();
            }
            File file = new File(file_dir, fileName);
            if (file.exists()){
                file.delete();
            }
            mRequest.setDestinationInExternalPublicDir(dirName, fileName);
        }
        else {
            File file = new File(mContext.getExternalFilesDir(dirName), fileName);
            if (file.exists()){
                file.delete();
            }
            mRequest.setDestinationInExternalFilesDir(mContext, dirName, fileName);
        }
        return this;
    }




    public DownloadUtil setNotification(boolean visible, boolean completed){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (visible && completed){
                mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            else if (!visible){
                mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            }
        }
        return this;
    }

    public long start(){
        final long downloadId = mDownloadManager.enqueue(mRequest);
        if (handler != null){
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    updateProgress(downloadId);
                }
            };
            ScheduledFuture future = service.scheduleAtFixedRate(task, 0, 100, TimeUnit.MILLISECONDS);
            futureCache.put(downloadId, future);
        }
        return downloadId;
    }

    public void cancel(long downloadId){
        ScheduledFuture future = futureCache.get(downloadId);
        if (future != null){
            Log.d("test", "good");
            future.cancel(true);
        }

        mDownloadManager.remove(downloadId);
    }

    public static boolean isDownloading(int downloadManagerStatus) {
        return downloadManagerStatus == DownloadManager.STATUS_RUNNING
                || downloadManagerStatus == DownloadManager.STATUS_PAUSED
                || downloadManagerStatus == DownloadManager.STATUS_PENDING;
    }





        private void updateProgress(long downloadId){
            int[] bytesAndStatus = getBytesAndStatus(downloadId);
            handler.sendMessage(handler.obtainMessage(0, bytesAndStatus[0], bytesAndStatus[1], bytesAndStatus[2]));
        }

        private int[] getBytesAndStatus(long downloadId) {
            int[] bytesAndStatus = new int[] { -1, -1, 0 };
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
            Cursor c = null;
            try {
                c = mDownloadManager.query(query);
                if (c != null && c.moveToFirst()) {
                    bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (bytesAndStatus[2] == DownloadManager.STATUS_SUCCESSFUL){
//                        mContext.getContentResolver().unregisterContentObserver(observer);
                        ScheduledFuture future = futureCache.get(downloadId);
                        if (future != null){
                            Log.d("test", "good");
                            future.cancel(true);
                        }
                    }
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }
            return bytesAndStatus;
        }

}
