package pers.gym.io.bigfiledownload;

import pers.gym.io.bigfiledownload.utils.LogUtil;

import java.io.IOException;

/**
 * <p>DownloadMain
 *
 * @author gym on 2023-03-31 19:43
 */
public class DownloadMain {

    /**
     * 主函数
     * arg1：下载链接
     * arg2：下载地址
     * arg3：开启开发模式
     *
     * @param args 参数
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            LogUtil.info("没有传入任何下载链接");
            return;
        }
        String url, localAddress;
        switch (args.length) {
            case 1:
                url = args[0];
                localAddress = null;
                break;
            case 2:
                url = args[0];
                localAddress = args[1];
                break;
            case 3:
                url = args[0];
                localAddress = args[1];
                LogUtil.debug = Boolean.parseBoolean(args[2]);
                break;
            default:
                LogUtil.info("args数组长度小于等于0或大于3");
                return;
        }

        if (url == null || "".equals(url.trim()) || localAddress == null || "".equals(localAddress)) {
            LogUtil.info("请填写正确的下载链接和下载文件地址");
            return;
        }

        /*// final String url = "https://cdn.mysql.com/archives/mysql-8.0/mysql-8.0.30-winx64.zip";
        final String url = "https://down.sandai.net/thunder11/XunLeiWebSetup11.4.3.2054dl.exe";
        final String localAddress = "E://downloadBigFile//";*/
        DownloadService service = new DownloadService();
        service.download(url, localAddress);
    }
}