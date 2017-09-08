package com.multiple.zone;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @author lichao
 * @date  2017年9月8日11:17:55
 */
@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ComponentScan
public class Application implements EmbeddedServletContainerCustomizer{
	
	private static Integer SERVER_PORT = 8080;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		if (args!=null&&args.length>0){
            SERVER_PORT = Integer.valueOf(args[0]);
        }
        app.run(args);
	}
	
	@Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(SERVER_PORT);
    }

}
