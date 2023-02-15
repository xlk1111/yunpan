package com.our.yun.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.our.yun.service")
@PropertySource("classpath:jdbc.properties")
@MapperScan("com.wang.yun.mapper")
@Import({JdbcConfig.class, MybatisConfig.class})
public class SpringConfig {

}
