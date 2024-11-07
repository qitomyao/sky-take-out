package com.sky.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.interceptor.JwtTokenUserInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
@EnableKnife4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    public WebMvcConfiguration(OpenApiExtensionResolver openApiExtensionResolver)
    {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }
    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status");
    }

    /**
     * 通过knife4j生成swagger接口文档
     * @return
     */
    @Bean
    public Docket docket1() { //Docket
        log.info("准备生成接口文档");
        String groupName = "管理端接口";
        //创建ApiInfo对象，用于定义API文档的基础信息，如标题、版本、描述等
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("苍穹外卖项目接口文档")
                .version("2.0")
                .description("苍穹外卖项目接口文档")
                .build();
        //创建Docket对象，用于配置Swagger的主要接口
        Docket docket = new Docket(DocumentationType.SWAGGER_2) //指定接口文档的类型为swagger，版本为2
                .apiInfo(apiInfo) //设置API文档的基本信息
                .groupName(groupName)
                .select() //返回ApiSelectorBuilder实例，用于控制哪些接口暴露给Swagger
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.admin")) //指定生成的接口需要扫描的包（子包会一起扫描）
                .paths(PathSelectors.any()) //指定路径选择器
                .build()
                .extensions(openApiExtensionResolver.buildExtensions(groupName));
        return docket;
    }

    /**
     * 通过knife4j生成swagger接口文档
     * @return
     */
    @Bean
    public Docket docket2() { //Docket
        log.info("准备生成接口文档");
        String groupName = "用户端接口";
        //创建ApiInfo对象，用于定义API文档的基础信息，如标题、版本、描述等
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("苍穹外卖项目接口文档")
                .version("2.0")
                .description("苍穹外卖项目接口文档")
                .build();
        //创建Docket对象，用于配置Swagger的主要接口
        Docket docket = new Docket(DocumentationType.SWAGGER_2) //指定接口文档的类型为swagger，版本为2
                .apiInfo(apiInfo) //设置API文档的基本信息
                .groupName(groupName)
                .select() //返回ApiSelectorBuilder实例，用于控制哪些接口暴露给Swagger
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.user")) //指定生成的接口需要扫描的包（子包会一起扫描）
                .paths(PathSelectors.any()) //指定路径选择器
                .build()
                .extensions(openApiExtensionResolver.buildExtensions(groupName));
        return docket;
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始设置静态资源映射...");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展Spring MVC框架的消息转化器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //需要为消息转换器设置一个对象转换器，对象转换器可以将Java对象序列化为json数据
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自己的消息转换器加入到容器中
        converters.add(0, converter); //消息转换器按集合顺序排列，加载前面代表优先使用
    }
}
