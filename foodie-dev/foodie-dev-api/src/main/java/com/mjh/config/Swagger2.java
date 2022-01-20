package com.mjh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @PackageName: com.mjh.config
 * @ClassName: Swagger2
 * @Author: majiahuan
 * @Date: 2020/10/21 22:07
 * @Description:
 */

//表示该类是一个Spring的配置类，用于生成API文档
@Configuration
//开启Swagger2配置
@EnableSwagger2
public class Swagger2 {
    // http://localhost:8088/swagger-ui.html 官方API文档访问链接
    // http://localhost:8088/doc.html 第三方API文档访问链接
    @Bean // 表示是一个Spring的Bean实例
    //配置Swagger2核心配置，docket
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)              // 用于指定api类型为Swagger2
                    .apiInfo(apiInfo())                             // 用于定义api文档汇总信息
                    .select()
                    .apis(RequestHandlerSelectors
                            .basePackage("com.mjh.controller"))     // 指定Controller所在包路径
                    .paths(PathSelectors.any())                     // 所有Controller
                    .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("乐购商城 平台接口API")
                .contact(new Contact("mjh",                     // 文档页标题
                        "http://www.mjhcode.com",
                        "mjh@aliyun.com"))                      // 联系人信息
                .description("乐购商城的api文档")                  // 详细信息
                .version("1.0.1")                                     // 文档版本号
                .termsOfServiceUrl("http://www.mjhcode.com")          // 网站网址
                .build();
    }
}
