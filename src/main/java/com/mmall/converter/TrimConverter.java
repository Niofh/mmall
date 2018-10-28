package com.mmall.converter;


import org.springframework.core.convert.converter.Converter;

/**
 * @Description: 自定义去掉前后空格 1：页面传递参数的类型 2：转换后的类型
 * @Author: Nicofh
 * @Date: 2018/6/25 15:08
 */
public class TrimConverter implements Converter<String, String> {

    public String convert(String source) {
        try {
            if (null != source) {
                source = source.trim();
                return source;
            }
        } catch (Exception e) {
        }
        return null;
    }
}
