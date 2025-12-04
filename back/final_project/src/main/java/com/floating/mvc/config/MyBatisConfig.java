package com.floating.mvc.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.floating.mvc.dao")
public class MyBatisConfig {

}
