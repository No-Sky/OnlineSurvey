package cn.xiaofangwei.onlinesurvey.utils;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CommonUtil {

    /**
     * 生成32位编码,不含横线
     *
     * @return uuid串
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid.toUpperCase();
    }

    /**
     * 得到当前日期格式化后的字符串，格式：yyyy-MM-dd(年-月-日)
     * @return 当前日期格式化后的字符串
     */
    public static String getTodayStr(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date()) ;
    }

    /**
     * 将对象转化成json
     *
     * @param t
     * @return
     * @throws JsonProcessingException
     */
//    public static <T> String toJson(T t) throws JsonProcessingException {
//        return OBJECT_MAPPER.get().writeValueAsString(t);
//    }

}
