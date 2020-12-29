package com.macro.mall.mail.config;

import com.macro.mall.common.config.BaseSwaggerConfig;
import com.macro.mall.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2API文档的配置
 * Created by jeason on 2018/4/26.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.macro.mall.mail.controller")
                .title("mall邮件系统")
                .description("mall邮件相关接口文档")
                .contactName("jeason")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
