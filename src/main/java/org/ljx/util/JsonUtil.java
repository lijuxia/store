package org.ljx.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * json工具类
 * Created by zhengke on 2017/2/3.
 */
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    /**
     * 对象转成json排除空值
     */
    private static ObjectMapper writeExcludeNull = new ObjectMapper();
    /**
     * 对象专程json
     */
    private static ObjectMapper write = new ObjectMapper();
    /**
     * json字符串专成对象
     */
    private static ObjectMapper read = new ObjectMapper();

    static {
        /**
         * 设置参数
         * 1.设置过滤对象为类的属性
         * 2.对象空不报错
         * 3.排除空值
         */
        writeExcludeNull.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
        writeExcludeNull.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        writeExcludeNull.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        /**
         * 设置参数
         * 1.设置过滤对象为类的属性
         * 2.对象空不报错
         */
        write.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
        write.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        /**
         * 设置参数
         * 1.允许不带引号
         * 2.允许单引号
         * 3.字符串属性不存在于类中不报错
         */
        read.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        read.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        read.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 不输出空的属性
     *
     * @param object 需要转换的类
     * @return 返回
     */
    public static String writeValuesAsStringExcludeNull(Object object) {
        try {
            return writeExcludeNull.writeValueAsString(object);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * 输出json
     *
     * @param object 需要转换的类
     * @return json
     */
    public static String writeValuesAsString(Object object) {
        try {
            return write.writeValueAsString(object);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * 读取json转换成类
     *
     * @param json      json字符串
     * @param valueType 类型
     * @param <T>       类型
     * @return 实例
     */
    public static <T> T readValue(String json, Class<T> valueType) {
        try {
            return read.readValue(json, valueType);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * 将字符串转list对象
     *
     * @param <T>     指定转换后类型
     * @param jsonStr json
     * @param cls     类型
     * @return list
     */
    public static <T> List<T> str2list(String jsonStr, Class<T> cls) {
        List<T> objList;
        try {
            JavaType t = read.getTypeFactory().constructCollectionType(List.class, cls);
            objList = read.readValue(jsonStr, t);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return new ArrayList<T>();
        }
        return objList;
    }
}