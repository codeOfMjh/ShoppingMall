package com.mjh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @PackageName: com.mjh
 * @ClassName: Application
 * @Author: majiahuan
 * @Date: 2020/10/6 21:19
 * @Description:
 */

@SpringBootApplication
// 扫描mybatis通用Mapper所在的包
@MapperScan(basePackages = "com.mjh.mapper")
// 扫描所有包，以及相关组件包
@ComponentScan(basePackages = {"com.mjh","org.n3r.idworker"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
