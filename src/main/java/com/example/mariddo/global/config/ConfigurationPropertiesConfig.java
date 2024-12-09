package com.example.mariddo.global.config;

import com.example.mariddo.global.properties.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
        JwtProperties.class
})
@Configuration
public class ConfigurationPropertiesConfig {
}