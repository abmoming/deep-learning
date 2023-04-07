package pers.gym.io.bigfiledownload;

import pers.gym.io.bigfiledownload.thread.DownloadThread;
import pers.gym.io.bigfiledownload.thread.LogThread;
import pers.gym.io.bigfiledownload.utils.CommonUtil;
import pers.gym.io.bigfiledownload.utils.HttpUtil;
import pers.gym.io.bigfiledownload.utils.LogUtil;
import pers.gym.io.bigfiledownload.utils.ThreadPoolUtil;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>下载业务类
 *
 * @author gym on 2023-04-07 16:45
 */
public class DownloadService {

    /**
     * 线程池大小
     */
    public static final int POOL_SIZE = 5;

    /**
     * 日志线程(占用线程池一个线程)
     */
    private static final int LOG_THREAD = 1;

    /**
     * 创建线程池
     */
    private static final ThreadPoolExecutor THREAD_POOL =
            ThreadPoolUtil.getThreadPool(POOL_SIZE + LOG_THREAD
                    , POOL_SIZE + LOG_THREAD, 1L, 100, "下载任务分片");

    /**
     * 资源下载
     * 1.获取链接上的文件名
     *
     * @param url          下载链接
     * @param localAddress 本地地址
     */
    public void download(String url, String localAddress) throws IOException {
        // 这里可以增加一个下载地址
        final String fileName = HttpUtil.getHttpFileName(url);
        final String fullAddress = localAddress + fileName;
        mkDirs(localAddress);
        long localFileSize = HttpUtil.getLocalFileSize(fullAddress);
        long downloadFileSize = HttpUtil.getDownloadFileSize(url);
        LogUtil.info("下载文件: {}, 大小: {}", fileName, downloadFileSize);
        if (localFileSize >= downloadFileSize) {
            LogUtil.info("{} 已下载完毕，无需重新下载.", fileName);
            return;
        }
        if (localFileSize > 0) {
            LogUtil.info("开始断点续传: {}", fileName);
        } else {
            LogUtil.info("开始下载文件: {}", fileName);
        }
        List<Future<Boolean>> futures = new ArrayList<>();
        LogUtil.info("开始下载时间: {}", LocalDateTime.now().format(LogUtil.TIME_MATTER));
        long startTime = System.currentTimeMillis();
        // 下载任务分片
        downloadTaskFragment(url, fullAddress, futures, downloadFileSize);
        LogThread logThread = new LogThread(downloadFileSize);
        futures.add(THREAD_POOL.submit(logThread));
        for (Future<Boolean> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                LogUtil.error("线程中断异常: {}", e.getMessage());
            } catch (ExecutionException e) {
                LogUtil.error("线程执行异常: {}", e.getMessage());
            }
        }
        THREAD_POOL.shutdown();
        boolean merge = mergeFile(fullAddress);
        if (merge) {
            // 删除文件 TODO
        }
    }

    /**
     * 下载任务分片
     *
     * @param url              下载文件连接
     * @param fileName         文件名称
     * @param futures          future集合
     * @param downloadFileSize 下载文件大小
     */
    public void downloadTaskFragment(String url, String fileName, List<Future<Boolean>> futures, long downloadFileSize) {

        // 执行下载任务的只需要5个线程，额外的1个线程是负责打印日志
        int downloadThreadNum = THREAD_POOL.getCorePoolSize() - LOG_THREAD;
        long fragmentSize = CommonUtil.divisionResult(String.valueOf(downloadFileSize), String.valueOf(downloadThreadNum));
        // long fragmentSize = downloadFileSize / downloadThreadNum;
        long size;
        // 到达最后一个线程时进行特殊处理
        int lastDownloadThread = downloadThreadNum - 1;
        // 最后剩余大小 = 总大小 - 前几个线程下载的大小
        long lastSize;
        for (int i = 0; i < downloadThreadNum; i++) {
            size = fragmentSize * i;
            lastSize = i == lastDownloadThread ? downloadFileSize : size + fragmentSize;
            // 续写的起始范围需要+1
            if (size != 0) {
                size++;
            }
            LogUtil.debug("当前线程>>>>>>{}, 起始范围: {}, 最后范围:{}", i, size, lastSize);
            DownloadThread downloadTask = new DownloadThread(url, fileName, size, lastSize, i, downloadFileSize);
            futures.add(THREAD_POOL.submit(downloadTask));
        }
    }

    /**
     * 合并分片文件
     *
     * @param fileName 写入的文件名
     * @return boolean
     */
    public static boolean mergeFile(String fileName) {

        LogUtil.info("开始合并分片文件, 文件名称: {}", fileName);
        int len;
        byte[] bytes = new byte[1024 * 10];
        try (RandomAccessFile saveFile = new RandomAccessFile(fileName, "rw")) {
            for (int i = 0; i < THREAD_POOL.getCorePoolSize() - LOG_THREAD; i++) {
                try (BufferedInputStream bis = new BufferedInputStream(
                        new FileInputStream(CommonUtil.createTempFileName(fileName, i)))) {
                    while ((len = bis.read(bytes)) != -1) {
                        saveFile.write(bytes, 0, len);
                    }
                }
            }
            LogUtil.info("合并分片文件完毕");
        } catch (FileNotFoundException e) {
            LogUtil.error("合并分片文件失败, 文件未找到异常: {}", e.getMessage());
            return false;
        } catch (IOException e) {
            LogUtil.error("合并分片文件发生异常");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断文件目录是否存在
     * 不存在则创建
     *
     * @param localAddress 本地地址
     */
    @SuppressWarnings("all")
    public void mkDirs(String localAddress) {
        File file = new File(localAddress);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}