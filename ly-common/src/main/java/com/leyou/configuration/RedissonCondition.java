package com.leyou.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class RedissonCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return propertyIsTrue(context, "spring.redisson.enabled");
    }

    private boolean propertyIsTrue(ConditionContext context, String key) {
        return context.getEnvironment().getProperty(key,Boolean.TYPE,Boolean.FALSE);
    }
}
