package com.springCloud.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2//开启swagger
public class SwaggerConfig {

    //http://localhost:8999/swagger-ui.html 访问ui地址

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.springCloud.test.controller")).paths(PathSelectors.any()).build();
    }

    //创建api扫包范围
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("springCloud game")
                .description("分布式架构springCloud")
                .termsOfServiceUrl("http://www.springCloudGame.com")
                //.contact(contact)
                .version("1.0").build();
    }
}
