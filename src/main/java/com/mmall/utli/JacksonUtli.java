package com.mmall.utli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonUtli {
    /**
     * json 反序列化 字符串转化json数据
     *
     * @param json
     * @return
     */
    public static Object jsonToObject(String json, Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object o1 = mapper.readValue(json, o.getClass());
        return o1;
    }

    /**
     * java对象转化字符串json
     *
     * @param o
     * @return
     * @throws JsonProcessingException
     */
    public static String objectToJson(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(o);
        return s;
    }
}
