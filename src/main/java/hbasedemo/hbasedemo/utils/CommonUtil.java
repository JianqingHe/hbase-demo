package hbasedemo.hbasedemo.utils;

import hbasedemo.hbasedemo.constant.Constant;

/**
 * 公共方法
 *
 * @author hejq
 * @date 2019/5/31 17:11
 */
public class CommonUtil {
    /**
     * 过滤特殊用于分隔作用的特殊字符
     * @param value
     * @return
     */
    public static String filterSpecChar(String value){
        return value.replaceAll(Constant.COLLECTION_SPLIT_CH,"").replaceAll(Constant.MAP_KV_CH,"");
    }
}
