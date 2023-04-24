package pers.gym.io.bigfiledownload.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>公共工具类
 *
 * @author gym on 2023-04-06 16:00
 */
public class CommonUtil {

    /**
     * 创建临时文件名称
     *
     * @param fileName 文件名称
     * @param index    序号
     * @return String
     */
    public static String createTempFileName(String fileName, int index) {
        final String TEMP_FILE_SUFFIX = ".temp";
        return fileName + TEMP_FILE_SUFFIX + index;
    }

    /**
     * 两数相除
     * 采用远离零方向舍入的舍入模式
     *
     * @param val1 变量1
     * @param val2 变量2
     * @return long
     */
    public static long divisionResult(String val1, String val2) {

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