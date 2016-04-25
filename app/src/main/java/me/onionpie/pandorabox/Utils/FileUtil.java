package me.onionpie.pandorabox.Utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.SyncStateContract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.onionpie.pandorabox.Constans;


/**
 * Created by jiudeng007 on 2016/1/6.
 */
public class FileUtil {

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getHeadImgFileName(Context context, String dirName, boolean temp){
        return context.getExternalFilesDir(dirName).getAbsolutePath() + File.separator + "headImg" + (temp ? "_temp" : "") +  ".jpg";
    }

    public static String getFileName(String dirName){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss", Locale.getDefault());
        return getSavePath(dirName) + File.separator + sdf.format(new Date()) + ".jpg";
    }

    public static File getSaveFolder(String folderName) {
        File file = new File(getSDCardPath() + File.separator + folderName + File.separator);
        file.mkdirs();
        return file;
    }
    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }

    public static File getSaveFile(String folderPath, String fileNmae) {
        File file = new File(getSavePath(folderPath) + File.separator + fileNmae);

        try {
            file.createNewFile();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return file;
    }

    public static String save(byte[] buffer, Object url, Context context) {
        if (url == null) {
            return "";
        }
        String fileName = url instanceof String ? (String)url : getPath(context, (Uri) url);
        if (!fileName.startsWith(getSavePath(Constans.SAVE_IMG_PATH))){
            String[] values = fileName.split(File.separator);
            fileName = getSavePath(Constans.SAVE_IMG_PATH) + File.separator + values[values.length - 1];
        }
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(buffer);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

//    public static String getFilePathFromContentUri(Uri selectedVideoUri, ContentResolver contentResolver) {
//        String filePath;
//        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
//
//        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
////      也可用下面的方法拿到cursor
////      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);
//
//        cursor.moveToFirst();
//
//        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//        filePath = cursor.getString(columnIndex);
//        cursor.close();
//        return filePath;
//    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        else if (!isKitKat){
            String filename = "";
            if (uri.getScheme().compareTo("content") == 0) {
                Cursor cursor = context.getContentResolver().query(uri, new String[] {MediaStore.Audio.Media.DATA}, null, null, null);
                if (cursor.moveToFirst()) {
                    filename = cursor.getString(0);
                }
            }
            else {
                if (uri.getScheme().toString().compareTo("file") == 0) {         //file:///开头的uri{
                    filename = uri.toString().replace("file://", "");
                    //替换file://
                    if (!filename.startsWith("/mnt")) {
                        //加上"/mnt"头
                        filename += "/mnt";
                    }
                }
            }
            return filename;
        }

        return "";
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

    public static long FileSize(File directory) {
        long length = 0;
        File[] files = directory.listFiles();
        if (files == null){
            return length;
        }
        for (File file : files) {
            if (file.isFile())
                length += file.length();
            else
                length += FileSize(file);
        }
        return length;
    }
}
