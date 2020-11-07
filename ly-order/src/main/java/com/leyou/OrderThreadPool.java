package com.leyou;

import com.leyou.configuration.OnSaleThreadPoolConfiguration;
import com.leyou.configuration.OrderThreadPoolConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;

@Component
public class OrderThreadPool {

    Logger logger = LoggerFactory.getLogger(OrderThreadPool.class);

    @Bean(name="orderThreadPool_OrderService")
    @ConditionalOnProperty(name = "threadpool.order.enabled", havingValue = "true")
    public Executor orderThreadPool(RejectedExecutionHandler growPolicyRejectedExecutionHandler, OrderThreadPoolConfiguration configuration) {
        int corePoolSize = configuration.getCorePoolSize(); //IO密集型, 一般公式：CPU核*2
        int maximumPoolSize = configuration.getMaximumPoolSize();
        int queueCapacity = configuration.getQueueCapacity(); //预计并发
        int keepAliveSeconds = configuration.getKeepAliveSeconds();

        logger.warn("orderThreadPool_OrderService :" +
                        "corePoolSize={}," +
                        "maximumPoolSize={}," +
                        "queueCapacity={}," +
                        "keepAliveSeconds={}" ,
                corePoolSize,
                maximumPoolSize,
                queueCapacity,
                keepAliveSeconds);

//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
//                corePoolSize,
//                maximumPoolSize,
//                0L,
//                TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>());
//        return threadPoolExecutor;

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数目
        executor.setCorePoolSize(corePoolSize);
        //指定最大线程数
        executor.setMaxPoolSize(maximumPoolSize);
        //队列中最大的数目
        executor.setQueueCapacity(queueCapacity);
        //线程名称前缀
        executor.setThreadNamePrefix("OrderThreadPool_OrderService_");

        //rejection-policy：当pool已经达到max size的时候，如何处理新任务
        //CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        //对拒绝task的处理策略
        //executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        //对拒绝task的处理策略
        executor.setRejectedExecutionHandler(growPolicyRejectedExecutionHandler);

        //线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        //加载
        executor.initialize();

        return executor;

    }

}
