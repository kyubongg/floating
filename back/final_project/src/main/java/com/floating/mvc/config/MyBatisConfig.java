package com.floating.mvc.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.floating.mvc.model.dao")
public class MyBatisConfig {

}
