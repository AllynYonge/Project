/*
 * Copyright (C) 2014 Li Cong, forlong401@163.com http://www.360qihoo.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.king.reading.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.DecimalFormat;

/**
 * File utility.
 */
public final class FileUtils {
    private static final String CAPTURE_FILE_NAME = "_capture.jpg";
    private static final String STORE_FILE_NAME = "_store.jpg";

    /**
     * 创建文件路径
     *
     * @param filePath
     */
    public static void createFileDirectory(String filePath) {
        if (hasSdcard()) {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println(file.mkdirs());
            }
        } else {
            Log.e("FileUtils", "SD卡不存在..");
        }
    }

    public static void delete(String path) {
        File file = new File(path);
        delete(file);
    }

    public static void delete(File file) {
        if (file == null) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }

        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                delete(f);
            } else {
                f.delete();
            }
        }
        file.delete();
    }

    /**
     * delete all files of folder
     * @param folder directory
     */
    public static void deleteChildrenFile(File folder) {
        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                deleteChildrenFile(f);
            } else {
                f.delete();
            }
        }
    }

    /**
     * get simple file name
     * @param Path /storage/sd/aa.jpg
     * @return aa.jpg
     */
    public static String getSimpleName(String Path) {
        return Path.substring(Path.lastIndexOf("/"), Path.length());
    }

    //create sdcard/package name/subName folder
    public static File getAppCacheDir(Context context, String subName) {
        if (AppUtils.getAvailMem(context) < 0) {
            return null;
        }
        File sd = Environment.getExternalStorageDirectory();
        File dir = new File(sd, context.getPackageName());
        File sub = new File(dir, subName);
        sub.mkdirs();
        if (subName.equals("capture") ||
               subName.equals("temp") ||
               subName.equals("audio") ||
               subName.equals("Avatar")) {
            createMediaScannerIgnoreFile(sub);
        }
        return sub;
    }

    /**
     * 判断文件是否存在
     * @param path
     * @param FileName
     * @return
     */
    public static boolean getFileIsExits(String path,String FileName){
        File file = new File(path,FileName);
        return file.exists();
    }

    /**
     * 得到保存图片所存的位置
     */
    public static File getStoreFile(Context context) {
        return new File(FileUtils.getAppCacheDir(context, "store"), SystemClock.elapsedRealtime() + STORE_FILE_NAME);
    }

    /**
     * 得到拍照图片所存的位置
     */
    public static File getCaptureFile(Context context) {
        return new File(FileUtils.getAppCacheDir(context, "capture"), SystemClock.elapsedRealtime() + CAPTURE_FILE_NAME);
    }

    public static File getLogFolder(Context context){
        return getAppCacheDir(context,"log");
    }

    /**
     * 判断是否挂在了外置内存卡
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 判断SD卡是否可用
     */
    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static String FormatFileSize(long size) {
        if (size <= 0) {
            size = 0;
        }
        DecimalFormat formater = new DecimalFormat("######.#");
        if (size < 1024 * 1024 * 1024) {
            float mSize = size / 1024f / 1024f;
            return formater.format(mSize) + "MB";
        } else if (size < 1024L * 1024 * 1024 * 1024) {
            float gSize = size / 1024f / 1024f / 1024f;
            return formater.format(gSize) + "GB";
        } else {
            return "size: error";
        }
    }

    /**
     * 获取SD卡剩余空间
     */
    @SuppressLint("NewApi")
    public static long getSDFreeSize() {
        if (isSDCardAvailable()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                return new File(getSDCardPath()).getUsableSpace();
            }
            StatFs statFs = new StatFs(getSDCardPath());
            long blockSize = statFs.getBlockSize();
            long freeBlocks = statFs.getAvailableBlocks();
            return freeBlocks * blockSize;
        }
        return 0;
    }

    // 递归
    public static long getFileSize(File f) throws Exception//取得文件夹大小
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * judge the file exists
     * @param filePath
     * @return {@code true}如果文件存在 ,否则返回{@code false}
     */
    public static boolean isExists(String filePath) {
        if (Check.isEmpty(filePath)) {
            return false;
        }
        return new File(filePath).exists();
    }

    /**
     * copy single file
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, length);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    // copy a file from srcFile to destFile, return true if succeed, return
    // false if fail
    public static boolean copyFile(File srcFile, File destFile) {
        boolean result = false;
        try {
            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally {
                in.close();
            }
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    /**
     * Copy data from a source stream to destFile.
     * Return true if succeed, return false if failed.
     */
    public static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            if (destFile.exists()) {
                destFile.delete();
            }
            FileOutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.flush();
                try {
                    out.getFD().sync();
                } catch (IOException e) {
                }
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 根据文件绝对路径，创建文件，若父文件夹不存在，则会创建父文件夹，若文件不存在，则也会创建文件
     *
     * @param filePath 文件路径
     * @return 创建的文件对象
     */
    public static File getFileFromPath(String filePath) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * don't want our storage directory scanned
     *
     * @param directory
     * @return
     */
    public static boolean createMediaScannerIgnoreFile(File directory) {
        String filename;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            filename = MediaStore.MEDIA_IGNORE_FILENAME;
        } else {
            filename = ".nomedia";
        }
        try {
            new File(directory, filename).createNewFile(); // prevent media scanner
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void writeStringToFile(File file, String content, String charset) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
            bw.write(content);
        } catch (Throwable t) {
            throw new RuntimeException("FileUtil|write error", t);
        } finally {
            try {
                bw.close();
            } catch (Throwable tt) {
                tt.printStackTrace();
            }
        }
    }

    public byte[] toByteArray(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeWithTransferTo(byte[] datas, String fileName) throws IOException {
        int DATA_CHUNK = 1024 * 1024;
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileChannel toFileChannel = raf.getChannel();

        long len = datas.length;
        byte[] data = null;
        ByteArrayInputStream bais = null;
        ReadableByteChannel fromByteChannel = null;
        long position = 0;
        while (len >= DATA_CHUNK) {

            data = new byte[DATA_CHUNK];
            bais = new ByteArrayInputStream(data);
            fromByteChannel = Channels.newChannel(bais);

            long count = DATA_CHUNK;
            toFileChannel.transferFrom(fromByteChannel, position, count);

            data = null;
            position += DATA_CHUNK;
            len -= DATA_CHUNK;
        }

        if (len > 0) {
            data = new byte[(int) len];
            bais = new ByteArrayInputStream(data);
            fromByteChannel = Channels.newChannel(bais);

            long count = len;
            toFileChannel.transferFrom(fromByteChannel, position, count);
        }

        data = null;
        toFileChannel.close();
        fromByteChannel.close();
    }

    /**
     * 编码
     * @param bstr
     * @return String
     */
    /***
     * encode by Base64
     */
    public static String encodeBase64(byte[]input) throws Exception{
        return Base64.encodeToString(input, Base64.DEFAULT);
    }
    /***
     * decode by Base64
     */
    public static byte[] decodeBase64(String input) throws Exception{
        return Base64.decode(input.getBytes("utf-8"), Base64.DEFAULT);
    }
}
