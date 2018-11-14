package com.mmall.controller.test;

import com.mmall.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/apis")
public class TestController {
    private String token = UUID.randomUUID().toString();

    @RequestMapping("/login")
    @ResponseBody
    public ServerResponse login() {
        Map<String, Object> map = new HashMap<>();
        ArrayList<String> strings = new ArrayList<>();
        strings.add("admin");
        map.put("token", token);

        return ServerResponse.createBySuccess(map);
    }


    @RequestMapping("/info")
    @ResponseBody
    public ServerResponse info() {
        Map<String, Object> map = new HashMap<>();
        ArrayList<String> strings = new ArrayList<>();
        strings.add("admin");
        map.put("roles", strings);
        map.put("name", "ofh");
        map.put("avatar", "http://39.108.113.143:80/group1/M00/00/00/rBKS5VvgD8yANXRlAAI4t8j7QgQ135.jpg");
        return ServerResponse.createBySuccess(map);
    }
}
