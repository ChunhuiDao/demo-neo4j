package com.konkera.demoneo4j.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 *
 * @author konkera
 * @date 2021/3/22
 */
public final class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    // 序列化和反序列化初始化配置
    static {
        /*
         * 序列化-是否序列对象的所有属性
         * - Include.ALWAYS 序列化所有(默认)
         * - Include.NON_NULL 属性为NULL不序列化
         * - Include.NON_ABSENT 同NON_NULL，但会有漏项
         * - Include.NON_EMPTY 属性为空或者为NULL都不序列化
         * - Include.NON_DEFAULT 属性为默认值不序列化
         * - Include.CUSTOM
         * - Include.USE_DEFAULTS
         */
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化-默认true属性名称带双引号
        OBJECT_MAPPER.configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), JsonWriteFeature.QUOTE_FIELD_NAMES.enabledByDefault());
        // 序列化-默认false不强制将所有Java数字写成字符串
        OBJECT_MAPPER.configure(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS.mappedFeature(), JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS.enabledByDefault());
        // 序列化-默认true自动关闭输出流
        OBJECT_MAPPER.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);
        // 序列化-默认true自动闭合json串首尾
        OBJECT_MAPPER.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, true);
        // 序列化-默认true自动强刷I/O流里面的数据
        OBJECT_MAPPER.configure(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM, true);
        // 序列化-默认false序列化BigDecimal时以科学计数方式输出(取决于BigDecimal是如何构造的)
        OBJECT_MAPPER.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        // 序列化-默认false不会按字母顺序排序属性
        OBJECT_MAPPER.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false);
        // 序列化-默认false不以类名作为根元素，可以通过@JsonRootName来自定义根元素名称
        OBJECT_MAPPER.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        // 序列化-默认false不缩放排列输出
        OBJECT_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, false);
        // 序列化-默认true将时间的转化格式为时间戳，与setDateFormat配合使用
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 序列化-date序列化格式
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(DATETIME_PATTERN));
        // 序列化-默认false枚举以name()来输出，true以toString()来输出
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        // 序列化-默认false枚举不以ordinal()来输出
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, false);
        // 序列化-默认false序列化Map时不对key进行排序操作
        OBJECT_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);
        // 序列化-默认false序列化char[]时不以json数组输出
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
        /*
         * 序列化-键名策略
         * -SNAKE_CASE 驼峰转“_”返回
         * -UPPER_CAMEL_CASE 驼峰首字母全大写返回
         * -LOWER_CAMEL_CASE 驼峰第一个首字母不大写，其余大写返回
         * -LOWER_CASE 全部小写返回
         * -KEBAB_CASE 驼峰转“-”返回
         * -LOWER_DOT_CASE 驼峰转“.”返回
         */
        OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        // 序列化-默认true时ObjectMapper序列化没有属性的空对象时会抛异常
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 反序列化-默认true启用自动关闭源，解析json字符串后自动关闭输入流
        OBJECT_MAPPER.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        // 反序列化-默认false不解析含有Java/C++注释样式的JSON串(如：/*或//的注释符)
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, false);
        // 反序列化-默认false不允许属性名称不带双引号
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 反序列化-默认false不允许单引号来包住属性名称和字符串值
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 反序列化-默认false不能解析JSON字符串包含非引号控制字符(值小于32的ASCII字符，包含制表符\t、换行符\n和回车\r)
        OBJECT_MAPPER.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        // 反序列化-默认false不允许JSON整数以多个0开始
        OBJECT_MAPPER.configure(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature(), true);
        // 反序列化-默认false不允许支持JSON数组中缺失值
        OBJECT_MAPPER.configure(JsonReadFeature.ALLOW_MISSING_VALUES.mappedFeature(), true);
        // 反序列化-默认true反序列化的时候如果多了其他属性会抛异常
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 反序列化-默认false不解析含有以"#"开头并直到一行结束的注释样式
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, false);
        // 反序列化-默认false不检测JSON对象重复的字段名
        OBJECT_MAPPER.configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, false);
        // 反序列化-默认false时底层的数据流(二进制数据持久化，如：图片，视频等)全部被output，若读取一个位置的字段，则抛出异常
        OBJECT_MAPPER.configure(JsonParser.Feature.IGNORE_UNDEFINED, false);

        // LocalDateTime、LocalDate序列化和反序列化配置
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATETIME_PATTERN)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_PATTERN)));
        OBJECT_MAPPER.registerModule(javaTimeModule);
    }

    /**
     * 获取模板副本
     *
     * @return {@link ObjectMapper}
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER.copy();
    }

    /**
     * 序列化为字符串，不输出null
     *
     * @param obj {@link Object} 被序列化对象
     * @return {@link String} 目标字符串
     */
    public static String toJsonString(Object obj) {
        if (null == obj) return null;
        try {
            OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JsonUtil.toJsonString()异常：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 序列化为字符串，输出null
     *
     * @param obj      {@link Object} 被序列化对象
     * @param withNull {@link boolean} 是否输出null
     * @return {@link String} 目标字符串
     */
    public static String toJsonString(Object obj, boolean withNull) {
        if (null == obj) return null;
        try {
            if (withNull) {
                OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            }
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JsonUtil.toJsonString()异常：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 反序列化为对象(包括数组)
     *
     * @param str    {@link String} 被反序列化字符串
     * @param tClass {@link Class<T>} 目标类型
     * @param <T>    返回类型
     * @return {@link T} 目标对象
     */
    public static <T> T parse2Object(String str, Class<T> tClass) {
        if (null == str || str.length() == 0) return null;
        try {
            return OBJECT_MAPPER.readValue(str, tClass);
        } catch (JsonProcessingException e) {
            log.error("JsonUtil.parse2Object()异常：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 反序列化为列表
     *
     * @param jsonString     {@link String} 被反序列化字符串
     * @param elementClasses {@link Class<T>} 列表元素类型
     * @param <T>            返回元素类型
     * @return {@link Collection <T>} 目标列表
     */
    public static <T> List<T> parse2List(String jsonString, Class<T> elementClasses) {
        if (null == jsonString || jsonString.length() == 0) return null;
        try {
            return OBJECT_MAPPER.readValue(jsonString, new TypeReference<List<T>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("JsonUtil.parse2List()异常：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 反序列化为元素是map的列表
     *
     * @param jsonString {@link String} 被反序列化字符串
     * @param kClass     {@link Class<K>} 集合键类型
     * @param vClass     {@link Class<V>} 集合值类型
     * @param <K>        键类型
     * @param <V>        值类型
     * @return {@link List<Map>} 目标列表
     */
    public static <K, V> List<Map<K, V>> parse2ListMap(String jsonString, Class<K> kClass, Class<V> vClass) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, new TypeReference<List<Map<K, V>>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("JsonUtil.parse2ListMap()异常：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 反序列化为集合
     *
     * @param jsonString {@link String} 被反序列化字符串
     * @param kClass     {@link Class<K>} 集合键类型
     * @param vClass     {@link Class<V>} 集合值类型
     * @param <K>        键类型
     * @param <V>        值类型
     * @return {@link Map} 目标集合
     */
    public static <K, V> Map<K, V> parse2Map(String jsonString, Class<K> kClass, Class<V> vClass) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, new TypeReference<Map<K, V>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("JsonUtil.parse2Map()异常：{}", e.getMessage());
            return null;
        }
    }
}
