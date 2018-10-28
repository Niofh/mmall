package com.mmall.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 时间转换器
 * @Author: Nicofh
 * @Date: 2018/6/25 15:17
 */
public class DateConverter implements Converter<String, Date> {
    private static final List<String> formarts = new ArrayList<String>();

    static {
        formarts.add("yyyy-MM");
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd hh:mm");
        formarts.add("yyyy-MM-dd hh:mm:ss");
    }


    public Date convert(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        try {
            String formatter = "";
            if (s.matches("^\\d{4}-\\d{1,2}$")) {
                formatter = formarts.get(0);
            } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                formatter = formarts.get(1);
            } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
                formatter = formarts.get(2);
            } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                formatter = formarts.get(3);
            } else {
                throw new IllegalArgumentException("日期模板错误，没有找到适合的模板！");
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter);
            return simpleDateFormat.parse(s);
        } catch (Exception e) {
            return null;
        }
    }
}
