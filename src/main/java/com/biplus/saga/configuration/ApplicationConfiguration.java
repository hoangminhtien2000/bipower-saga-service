package com.biplus.saga.configuration;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.context.ApplicationContextProvider;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.feign.ClientFeignConfiguration;
import com.biplus.core.security.EnableSecurityService;
import com.biplus.core.swagger.EnableSwagger;
import com.biplus.core.trace.TraceInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Configuration
@Aspect
@EnableSecurityService
@EnableSwagger("com.biplus.saga.controller")
@EnableFeignClients("com.biplus.saga.service.feign")
@EnableJpaAuditing(auditorAwareRef="auditorAware")
@Import({ClientFeignConfiguration.class})
@EnableConfigurationProperties
public class ApplicationConfiguration {
    @Pointcut("execution(public * com.biplus.saga.service..*.*(..)) " +
            "|| execution(public * com.biplus.saga.controller..*.*(..))" +
            "|| execution(public * com.biplus.saga.tramsaga..*.*(..))" +
            "|| execution(public * com.biplus.saga.handler..*.*(..))")
    public void trace() {
    }
    @Bean
    public TraceInterceptor traceInterceptor() {
        return new TraceInterceptor();
    }

    @Bean
    public Advisor traceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("com.biplus.saga.configuration.ApplicationConfiguration.trace()");
        return new DefaultPointcutAdvisor(pointcut, traceInterceptor());
    }

    @Bean
    public ApplicationContextProvider applicationContextProvider() {
        return new ApplicationContextProvider();
    }


    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> ofNullable(ActionUserHolder.getActionUser())
                .map(ActionUserDTO::getUsername)
                .or(() -> of(""));
    }
}
