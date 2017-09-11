package com.multiple.zone.config.mybatise;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 事物配置等同于<tx:annotation-driven/>
 * @author lichao 
 * @Date 2017年9月11日17:42:52
 */
//@Aspect
@Configuration
@ImportResource("classpath:spring-tx.xml")
public class TxConfig {

}
