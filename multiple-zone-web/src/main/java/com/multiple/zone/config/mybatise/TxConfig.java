package com.multiple.zone.config.mybatise;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 事物配置等同于<tx:annotation-driven/>
 * Created by zhangliewei on 2017/6/26.
 */
//@Aspect
@Configuration
@ImportResource("classpath:spring-tx.xml")
public class TxConfig {

}
