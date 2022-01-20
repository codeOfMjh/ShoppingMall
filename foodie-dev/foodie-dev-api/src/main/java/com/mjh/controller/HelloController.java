package com.mjh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @PackageName: com.mjh.controller
 * @ClassName: HelloController
 * @Author: majiahuan
 * @Date: 2020/10/6 21:23
 * @Description:
 */

@ApiIgnore // 在使用Swagger2生成API文档时忽略该接口
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String Hello() {
        return "hello world";
    }
}
