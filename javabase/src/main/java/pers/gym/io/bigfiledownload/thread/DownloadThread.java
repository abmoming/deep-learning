package pers.gym.io.bigfiledownload.thread;

import pers.gym.io.bigfiledownload.utils.HttpUtil;
import pers.gym.io.bigfiledownload.utils.LogUtil;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;

/**
 * <p>下载线程
 *
 * @author gym on 2023-04-03 14:00
 */
public class DownloadThread implements Callable<Boolean> {

    // 下载文件地址
    private String url;
    // 下载文件名称
    private String fileName;
    // 下载范围初始值大小
    private Long size;
    // 下载范围结束值大小
    private Long lastSize;
    // 下载部分
    private Integer part;
    // 下载文件大小
    private Long downloadFileSize;
    // 临时文件后缀
    private static final String TEMP_FILE_SUFFIX = ".temp";
    // 每次读取数据块大小
    private static final byte[] BUFFER = new byte[1024 * 100];

    public DownloadThread() {
    }

    public DownloadThread(String url, String fileName, long size, long lastSize, int part, long downloadFileSize) {
        this.url = url;
        this.fileName = fileName;
        this.size = size;
        this.lastSize = lastSize;
        this.part = part;
        this.downloadFileSize = downloadFileSize;
    }

    @Override
    public Boolean call() throws Exception {

        if (url == null || "".equals(url.trim())) {
            throw new RuntimeException("文件下载路径有误!");
        }
        // 临时文件名
        String tempFileName = fileName + TEMP_FILE_SUFFIX + part;
        // 临时文件大小
        long tempFileSize = HttpUtil.getLocalFileSize(tempFileName);
        if (lastSize.compareTo(downloadFileSize) >= 0) {
            lastSize = 0L;
        }
        // size + tempFileSize：防止其中一个线程写临时文件时断掉了，重启任务时候能续写当前临时文件
        HttpURLConnection conn = HttpUtil.getHttpUrlConn(url, size + tempFileSize, lastSize);
        try(BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            RandomAccessFile savedFile = new RandomAccessFile(tempFileName, "rw")) {
            int len;
            while ((len = bis.read(BUFFER)) != -1) {
                savedFile.write(BUFFER, 0, len);
            }
        } catch (FileNotFoundException e) {
            LogUtil.error("要下载的文件路径不存在: {}", url);
            return Boolean.FALSE;
        } catch (Exception e) {
            LogUtil.error("下载出现异常: {}", e.getMessage());
            e.printStackTrace();
            return Boolean.FALSE;
        } finally {
            conn.disconnect();
        }

        return true;
    }
}