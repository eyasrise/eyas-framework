package com.eyas.framework;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAsync
@SpringBootApplication
@MapperScan(basePackages = {
        "com.eyas.framework.dao",
        "com.eyas.framework.annotation",
        "com.eyas.framework.config",
        "com.eyas.framework.mapper",
        "com.eyas.framework.mpdao"
})
@EnableSwagger2
public class FrameworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrameworkApplication.class, args);
    }

//    @Bean
//    public MybatisPlusInterceptor paginationInnerInterceptor(){
//        MybatisPlusInterceptor mybatisPlusInterceptor =new MybatisPlusInterceptor();
//        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//        return mybatisPlusInterceptor;
//    }

}
