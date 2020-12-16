//package com.tophousekeeper;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
///**
// * @auther: NiceBin
// * @description:
// * Swagger2 配置类
// * 在与spring boot 集成时，放在与application.java 同级的目录下
// * 通过@Configuration注解，让spring来加载该配置
// * 再通过@EnableSwagger2注解来启动Swagger2
// *
// * @date: 2020/5/6 18:13
// */
//@Configuration
//@EnableSwagger2
//public class Swagger2Config {
//    /**
//     * 创建API应用
//     * appinfo()增加API相关信息
//     * 通过select()函数返回一个ApiSelectorBuilder实例，用来控制那些接口暴露给Swagger来展现
//     * 本例采用置顶扫描的包路径来定义指定要建立API的目录
//     *
//     * @return
//     */
//    @Bean
//    public Docket createRestApi() {
//        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.tophousekeeper"))
//                .paths(PathSelectors.any()).build();
//        return docket;
//    }
//
//
//    /**
//     * 创建改API的基本信息（这些基本信息会展示在文档页面中）
//     * 访问地址： http://项目实际地址/swagger-ui.html
//     * @return
//     */
//    public ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("这是标题")
//                .description("这是描述")
//                .termsOfServiceUrl("http://www.baidu.com")
//                .version("1.0")
//                .build();
//    }
//}
