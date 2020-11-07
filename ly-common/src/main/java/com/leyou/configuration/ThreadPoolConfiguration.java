package com.leyou.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Configuration
public class ThreadPoolConfiguration {

    Logger logger = LoggerFactory.getLogger(ThreadPoolConfiguration.class);

    @Bean
    public RejectedExecutionHandler growPolicyRejectedExecutionHandler(){
        RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
            Lock lock = new ReentrantLock();
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                try {

                    int maximumPoolSize = executor.getMaximumPoolSize();
                    lock.lock();
                    if(maximumPoolSize>=executor.getMaximumPoolSize()) {

                        executor.setMaximumPoolSize(executor.getMaximumPoolSize() + 10);

                        logger.info("oldMaximumPoolSize={}, newMaximumPoolSize={}, lineing={}, remaingcapacity={}, lock={}" ,
                                maximumPoolSize ,
                                executor.getMaximumPoolSize(),
                                executor.getQueue().size(),
                                executor.getQueue().remainingCapacity(),
                                lock);
                    }

                    executor.submit(r);
                } finally {
                    lock.unlock();
                }
            }
        };
        return rejectedExecutionHandler;
    }

}
