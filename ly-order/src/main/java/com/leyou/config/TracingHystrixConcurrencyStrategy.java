package com.leyou.config;

import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
public class TracingHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {
    @Autowired
    private Tracing tracing;

    public TracingHystrixConcurrencyStrategy(){
        HystrixPlugins.reset();
        HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        CurrentTraceContext context = tracing.currentTraceContext();
        if(context!=null)
            return context.wrap(callable);
        else
            return super.wrapCallable(callable);
    }

    public void setTracing(Tracing tracing) {
        this.tracing = tracing;
    }
}