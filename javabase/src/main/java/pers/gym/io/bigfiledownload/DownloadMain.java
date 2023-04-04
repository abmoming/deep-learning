package pers.gym.io.bigfiledownload;

import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import jdk.management.resource.internal.inst.SocketOutputStreamRMHooks;
import pers.gym.io.bigfiledownload.thread.DownloadThread;
import pers.gym.io.bigfiledownload.thread.LogThread;
import pers.gym.io.bigfiledownload.utils.HttpUtil;
import pers.gym.io.bigfiledownload.utils.LogUtil;
import pers.gym.io.bigfiledownload.utils.ThreadPoolUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>DownloadMain
 *
 * @author gym on 2023-03-31 19:43
 */
public class DownloadMain {

    /**
     * 线程池大小
     */
    public static final int POOL_SIZE = 5;

    /**
     * 日志线程(占用线程池一个线程)
     */
    private static final  int LOG_THREAD = 1;

    /**
     * 创建线程池
     */
    private static final ThreadPoolExecutor THREAD_POOL =
            ThreadPoolUtil.getThreadPool(POOL_SIZE + LOG_THREAD
                    , POOL_SIZE + LOG_THREAD, 1L, 100, "下载任务分片");


    public static void main(String[] args) throws IOException {

        // 226363319
        final String testUrl = "https://cdn.mysql.com/archives/mysql-8.0/mysql-8.0.30-winx64.zip";
        final String localAddress = "E:\\downloadBigFile\\";
        new DownloadMain().download(testUrl, localAddress);
        // new DownloadMain().downloadTaskFragment(null,0L);
    }

    /**
     * 资源下载
     * 1.获取链接上的文件名
     *
     * @param url          下载链接
     * @param localAddress 本地地址
     */
    public void download(String url, String localAddress) throws IOException {
        // 这里可以增加一个下载地址
        String fileName = localAddress + HttpUtil.getHttpFileName(url);
        System.out.println(fileName);
        long localFileSize = HttpUtil.getLocalFileSize(fileName);
        long downloadFileSize = HttpUtil.getDownloadFileSize(url);
        System.out.println(downloadFileSize);
        if (localFileSize >= downloadFileSize) {
            LogUtil.info("{}已下载完毕，无需重新下载.", fileName);
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
        downloadTaskFragment(url, fileName, futures, downloadFileSize);
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
        long fragmentSize = divisionResult(String.valueOf(downloadFileSize), String.valueOf(downloadThreadNum));
        long size;
        // 到达最后一个线程时进行特殊处理
        int lastDownloadThread = downloadThreadNum - 1;
        // 最后剩余大小 = 总大小 - 前几个线程下载的大小
        long lastSize = downloadFileSize - (fragmentSize * (lastDownloadThread));
        for (int i = 0; i < downloadThreadNum; i++) {
            size = fragmentSize * i;
            // 下载范围：size-lastSize；如果i与最后一个线程匹配，则范围选用剩余的大小
            lastSize = size + (i == lastDownloadThread ? lastSize : fragmentSize);
            // 续写的起始范围需要+1
            if (size != 0) {
                size++;
            }
            DownloadThread downloadTask = new DownloadThread(url, fileName, size, lastSize, i, downloadFileSize);
            futures.add(THREAD_POOL.submit(downloadTask));
        }
    }

    /**
     * 两数相除
     * 采用远离零方向舍入的舍入模式
     *
     * @param val1 变量1
     * @param val2 变量2
     * @return long
     */
    public long divisionResult(String val1, String val2) {

        if (val1 == null
                || val2 == null
                || Integer.parseInt(val1) == 0
                || Integer.parseInt(val2) == 0) {
            return 0;
        }
        // 采用远离零方向舍入的舍入模式
        return new BigDecimal(val1).divide(new BigDecimal(val2), RoundingMode.UP).longValue();
    }
}