package com.sven.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.sven.interceptor.MockRegionInterceptor;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter
{

    @Bean
    public MockRegionInterceptor mockRegionInterceptor()
    {
        return new MockRegionInterceptor();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry)
    {
        registry.addInterceptor(mockRegionInterceptor()).excludePathPatterns("/error");

    }

}
