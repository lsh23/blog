package com.blog.demo.config;

import com.blog.demo.auth.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthenticationConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    public AuthenticationConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> pathsToExclude = List.of(
                "/api/v1/auth/github"
        );

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns()
                .excludePathPatterns(pathsToExclude);
    }
}
