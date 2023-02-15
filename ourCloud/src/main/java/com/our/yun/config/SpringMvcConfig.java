package com.our.yun.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan({"com.our.yun.controller","com.our.yun.config"})
@EnableWebMvc
public class SpringMvcConfig {

}
