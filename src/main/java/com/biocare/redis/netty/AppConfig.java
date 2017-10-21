package com.biocare.redis.netty;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * spring context 的配置文件
 *
 * @author mariston
 * @since 2017/10/11
 */
@Configuration
@EnableWebMvc
@ImportResource({"classpath*:/applicationContext.xml"})
public class AppConfig {
}
