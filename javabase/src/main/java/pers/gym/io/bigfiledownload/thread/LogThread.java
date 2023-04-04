package pers.gym.io.bigfiledownload.thread;

import jdk.management.resource.internal.inst.SocketOutputStreamRMHooks;
import pers.gym.io.bigfiledownload.DownloadMain;
import pers.gym.io.bigfiledownload.utils.ThreadPoolUtil;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>日志线程
 *
 * @author gym on 2023-04-03 14:26
 */
public class LogThread implements Callable<Boolean> {

    // 下载文件大小
    private final Long downloadFileSize;
    // 已下载文件大小
    public static AtomicLong LOCAL_DOWNLOAD_SIZE = new AtomicLong();
    // 本地文件写入大小
    public static AtomicLong LOCAL_FINISH_SIZE = new AtomicLong();
    // 下载完成线程
    public static AtomicLong DOWNLOAD_FINISH_THREAD = new AtomicLong();
    // 默认下载线程数量5
    private static final int[] DOWNLOAD_THREAD_ARR = new int[5];
    // 1MB大小
    private static final double mb = 1024d * 1024d;
    // 1KB大小
    private static final double kb = 1024d;

    public LogThread(Long downloadFileSize) {
        this.downloadFileSize = downloadFileSize;
    }

    @Override
    public Boolean call() throws Exception {

        // 计算剩余下载时间，已下载大小，当前速度

        int indexOf = 0;
        long size = 0;
        // 文件总大小
        String downloadFileMBSize = String.format("%.1f", downloadFileSize / mb);
        // 下载完成线程数 >= 线程池设定的核心线程数
        while (DOWNLOAD_FINISH_THREAD.get() >= DownloadMain.POOL_SIZE) {
            ++indexOf;
            /*// 当前线程下载大小 = 当前已下载大小 - 之前的下载大小
            long downloadSize = LOCAL_DOWNLOAD_SIZE.get();
            DOWNLOAD_THREAD_ARR[++indexOf % DownloadMain.POOL_SIZE] = Long.valueOf(downloadSize - size).intValue();
            size = downloadSize;*/
            // 每线程平均速度
            String speed = String.format("%.1f", longConvertDouble(LOCAL_DOWNLOAD_SIZE.get()) / kb / indexOf);
            // 剩余时间
            // long surplusSize = downloadFileSize - LOCAL_DOWNLOAD_SIZE.get() - LOCAL_FINISH_SIZE.get();
            long surplusSize = downloadFileSize - LOCAL_DOWNLOAD_SIZE.get();
            String surplusTime = String.format("%.1f", surplusSize / kb / Double.parseDouble(speed));
            // 已下载大小
            String currentFileSize = String.format("%.2f",
                    longConvertDouble(LOCAL_DOWNLOAD_SIZE.get()) / mb
                            + longConvertDouble(LOCAL_FINISH_SIZE.get()) / mb);
            String speedLog = String.format("已下载 %sMB/%sMB,速度 %sKB/s,剩余时间 %ss"
                    , currentFileSize, downloadFileMBSize, speed, surplusTime);
            System.out.print("\r");
            System.out.print(speedLog);
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println();
        return true;
    }

    /**
     * long转double
     *
     * @param val 转换数值
     * @return double
     */
    protected double longConvertDouble(long val) {
        return Double.parseDouble(String.valueOf(val));
    }
}