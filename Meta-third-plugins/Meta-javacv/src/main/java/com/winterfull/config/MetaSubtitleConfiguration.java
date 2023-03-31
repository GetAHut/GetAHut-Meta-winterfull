package com.winterfull.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : ytxu5
 * @date: 2023/3/31
 */
@Configuration
public class MetaSubtitleConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "meta.project.javacv.config")
    public MetaSubtitleProperties metaSubtitleProperties(){
        return new MetaSubtitleProperties();
    }
}
