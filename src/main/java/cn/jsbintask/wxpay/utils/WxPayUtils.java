package cn.jsbintask.wxpay.utils;

import cn.jsbintask.wxpay.annotation.WrapperPrefix;
import cn.jsbintask.wxpay.response.WxPayResponse;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/6 10:02
 */
@UtilityClass
public class WxPayUtils {
    private static final String RANDOM_RANGE = "0123456789abcdefghijklmopqrstuvwsyzABCDEFGHIJKLMOPQRSTUVWSYZ";

    public String nonceStr() {
        return RandomStringUtils.random(30, RANDOM_RANGE);
    }


    public boolean isWxPayXml(Object rawData) {
        if (rawData instanceof String) {
            String str = (String) rawData;
            return StringUtils.startsWithIgnoreCase(str, "<xml>") && StringUtils.endsWithIgnoreCase(str, "</xml>")
                    && StringUtils.contains(str, "<return_code>")
                    && StringUtils.contains(str, "</return_code>");
        }

        return false;

    }

    @SuppressWarnings("all")
    public <T extends WxPayResponse> void setWrapPrefixValue(T response, HashMap<String, ?> responseMap) {
        Objects.requireNonNull(response, "please init response.");
        Objects.requireNonNull(responseMap, "response map can`t be empty.");

        Class<? extends WxPayResponse> aClass = response.getClass();
        Field[] fields = aClass.getDeclaredFields();
        Set<String> keySet = responseMap.keySet();
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                WrapperPrefix annotation = field.getAnnotation(WrapperPrefix.class);
                if (annotation != null) {
                    String prefix = annotation.value();
                    int varNum = annotation.variables();
                    Class<?> fieldType = field.getType();
                    Object fieldValue = fieldType.newInstance();

                    if (fieldValue instanceof Map) {
                        Map map = (Map) fieldValue;
                        Class<?> actualTypeArgument = ((Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1]);
                        keySet.stream().filter(key -> {
                            if (key.startsWith(prefix)) {
                                return key.substring(prefix.length()).split("_").length == varNum;
                            }

                            return false;
                        }).collect(Collectors.toList()).forEach(key -> {
                            Object value = responseMap.get(key);
                            if (Integer.class.equals(actualTypeArgument) || Long.class.equals(actualTypeArgument)) {
                                map.put(key, Integer.parseInt(value.toString()));
                            } else {
                                map.put(key, value);
                            }
                        });
                    } else if (fieldValue instanceof Collection) {
                        Collection collection = (Collection) fieldValue;
                        Class<?> actualTypeArgument = ((Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
                        keySet.stream().filter(key -> {
                            if (key.startsWith(prefix)) {
                                return key.substring(prefix.length()).split("_").length == varNum;
                            }

                            return false;
                        }).collect(Collectors.toList()).forEach(key -> {
                            Object value = responseMap.get(key);
                            if (Integer.class.equals(actualTypeArgument) || Long.class.equals(actualTypeArgument)) {
                                collection.add(Integer.parseInt(value.toString()));
                            } else {
                                collection.add(value);
                            }
                        });
                    }
                    field.setAccessible(true);
                    field.set(response, fieldValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File writeTextFile(Object rawData) {
        File temp = null;
        if (rawData instanceof byte[]) {
            try {
                temp = FileUtils.createFile("temp/" + UUID.randomUUID().toString() + ".gzip");
                FileOutputStream fos = new FileOutputStream(temp);
                fos.write((byte[]) rawData);
            } catch (IOException ignored) {
            }
        } else if (rawData instanceof CharSequence) {
            try {
                temp = FileUtils.createFile("temp/" + UUID.randomUUID().toString() + ".txt");
                FileOutputStream fos = new FileOutputStream(temp);
                fos.write(((String) rawData).getBytes(StandardCharsets.UTF_8));
            } catch (IOException ignored) {
            }
        } else {
            throw new RuntimeException("unsupported raw data type: " + rawData.getClass().getName());
        }
        return temp;
    }

    /**
     * 生成 HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac instance = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        instance.init(secretKeySpec);
        byte[] array = instance.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString().toUpperCase();
    }

    public static String MD5(String data) throws NoSuchAlgorithmException {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString().toUpperCase();
    }
}
