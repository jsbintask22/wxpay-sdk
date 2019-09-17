package cn.jsbintask.wxpay.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/5 17:25
 */
@UtilityClass
@Slf4j
public class XmlUtils {
    private static final XmlMapper MAPPER = new XmlMapper();

    static {
        MAPPER.setDefaultUseWrapper(true);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MAPPER.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public String obj2Xml(Object object) {
        if (object == null) {
            return null;
        }

        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("write xml error. {}", e.getMessage());
            return null;
        }
    }

    public String obj2PrettyXml(Object object) {
        if (object == null) {
            return null;
        }

        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("write xml error. {}", e.getMessage());
            return null;
        }
    }

    public <T> T xml2Obj(String xml, Class<T> tClass) {
        if (xml == null) return null;

        try {
            return MAPPER.readValue(xml, tClass);
        } catch (IOException e) {
            log.error("parse xml {} error. {}", xml, e.getMessage());
            return null;
        }
    }

    public <T, V> T xml2CollectionObj(String xml, Class<T> collCls, Class<V> eleCls) {
        if (xml == null) return null;
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(collCls, eleCls);

        try {
            return MAPPER.readValue(xml, javaType);
        } catch (IOException e) {
            log.error("parse xml {} error. {}", xml, e.getMessage());
            return null;
        }
    }
}
