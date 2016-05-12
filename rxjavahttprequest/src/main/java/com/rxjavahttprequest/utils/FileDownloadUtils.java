/*
 * Copyright (c) 2015 LingoChamp Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rxjavahttprequest.utils;

import android.os.Build;
import android.os.StatFs;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class FileDownloadUtils {

    /**
     * 获取唯一的下载id
     * @param url
     * @param path
     * @return
     */
    public static int generateId(final String url, final String path) {
        return md5(String.format("%sp%s", url, path)).hashCode();
    }

    private static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 获取剩余空闲空间
     * @param path
     * @return
     */
    public static long getFreeSpaceBytes(final String path) {
        long freeSpaceBytes;
        final StatFs statFs = new StatFs(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            freeSpaceBytes = statFs.getAvailableBytes();
        } else {
            freeSpaceBytes = statFs.getAvailableBlocks() * (long) statFs.getBlockSize();
        }

        return freeSpaceBytes;
    }
}
