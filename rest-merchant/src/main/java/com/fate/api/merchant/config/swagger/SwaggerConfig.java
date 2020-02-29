package com.fate.api.merchant.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: parent
 * @description: 接口文档配置
 * @author: chenyixin
 * @create: 2019-05-07 17:36
 **/
@EnableSwagger2
@Configuration
@Profile({"dev","test"})
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        List<Parameter> pars = new ArrayList<Parameter>();

        ParameterBuilder ticketPar2= new ParameterBuilder();
        ticketPar2.name("token").description("后端分给当前用户的身份凭证TOKEN")
                .modelRef(new ModelRef("String")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar2.build());

        return new Docket(DocumentationType.SWAGGER_2).globalOperationParameters(pars)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fate.api.merchant.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("商户端小程序文档")
                .description("简单优雅的restful风格")
                .termsOfServiceUrl("https://wwyll.com/api")
                .version("基础版")
                .build();
    }
}
