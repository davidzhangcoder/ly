package com.leyou.configuration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Configuration
public class OnSaleThreadPool {

    Logger logger = LoggerFactory.getLogger(OnSaleThreadPool.class);

//    @Bean
//    public RejectedExecutionHandler growPolicyRejectedExecutionHandler(){
//        RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
//            Lock lock = new ReentrantLock();
//            @Override
//            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//                try {
//
//                    int maximumPoolSize = executor.getMaximumPoolSize();
//                    lock.lock();
//                    if(maximumPoolSize>=executor.getMaximumPoolSize()) {
//
//                        executor.setMaximumPoolSize(executor.getMaximumPoolSize() + 10);
//
//                        logger.info("oldMaximumPoolSize={}, newMaximumPoolSize={}, lineing={}, remaingcapacity={}, lock={}" ,
//                                maximumPoolSize ,
//                                executor.getMaximumPoolSize(),
//                                executor.getQueue().size(),
//                                executor.getQueue().remainingCapacity(),
//                                lock);
//                    }
//
//                    executor.submit(r);
//                } finally {
//                    lock.unlock();
//                }
//            }
//        };
//        return rejectedExecutionHandler;
//    }

    @Bean(name="onSaleThreadPool_OnSaleService")
    @ConditionalOnProperty(name = "threadpool.onsale.enabled", havingValue = "true")
    public Executor onSaleThreadPool(RejectedExecutionHandler growPolicyRejectedExecutionHandler, OnSaleThreadPoolConfiguration configuration) {
        int corePoolSize = configuration.getCorePoolSize(); //CPU密集型(没有阻塞，CPU一直全速运行), 一般公式：CPU核+1个线程的线程池
        int maximumPoolSize = configuration.getMaximumPoolSize();
        int queueCapacity = configuration.getQueueCapacity(); //预计并发
        int keepAliveSeconds = configuration.getKeepAliveSeconds();

        logger.warn("onSaleThreadPool_OnSaleService :" +
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
        executor.setThreadNamePrefix("OnSaleThreadPool_OnSaleService_");

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
