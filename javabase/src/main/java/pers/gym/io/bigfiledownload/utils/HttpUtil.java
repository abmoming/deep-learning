package pers.gym.io.bigfiledownload.utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * <p>HttpUtil
 *
 * @author gym on 2023-03-31 19:53
 */
public class HttpUtil {

    /**
     * 获取HttpUrl链接
     * 用于文件下载，请求下载字节范围
     *
     * @param url   下载链接
     * @param start 范围起始值
     * @param end   范围结束值
     * @return HttpURLConnection
     */
    public static HttpURLConnection getHttpUrlConn(String url, long start, long end) throws IOException {

        HttpURLConnection conn = getHttpUrlConn(url);
        LogUtil.debug("{}线程下载内容区间: {}-{}", Thread.currentThread().getName(), start, end);
        conn.setRequestProperty("RANGE", "bytes=" + start + "-" + (end == 0L ? "" : end));
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        if (LogUtil.DEBUG) {
            headerFields.forEach((key, value) ->
                    LogUtil.debug("{}线程相应请求头: {}-{}", Thread.currentThread().getName(), key, value));
        }
        return conn;
    }

    /**
     * 获取HttpUrl链接
     *
     * @param url 访问的url
     * @return HttpURLConnection
     */
    public static HttpURLConnection getHttpUrlConn(String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36");
        return conn;
    }

    /**
     * 获取下载文件大小
     *
     * @param url 下载链接
     * @return long
     */
    public static long getDownloadFileSize(String url) throws IOException {
        HttpURLConnection conn = getHttpUrlConn(url);
        long downloadFileSize = conn.getContentLengthLong();
        conn.disconnect();
        return downloadFileSize;
    }

    /**
     * 通过文件名称，获取本地文件的大小（避免重复下载）
     *
     * @param fileName 文件名称
     * @return 文件大小
     */
    public static long getLocalFileSize(String fileName) {
        File file = new File(fileName);
        return file.exists() && file.isFile() ? file.length() : 0;
    }

    /**
     * 获取下载链接url的文件名
     *
     * @param url 下载链接
     * @return 文件名称
     */
    public static String getHttpFileName(String url) {
        int addOne = 1;
        int index = url.lastIndexOf("/");
        return url.substring(index + addOne);
    }
}