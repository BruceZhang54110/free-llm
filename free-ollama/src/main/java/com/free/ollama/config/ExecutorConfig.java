package com.free.ollama.config;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class ExecutorConfig {

    static class ContextAwareCallable<T> implements Callable<T> {
        private Callable<T> task;
        Map<String, String> contextMap;

        public ContextAwareCallable(Callable<T> task, Map<String, String> contextMap) {
            this.task = task;
            this.contextMap = contextMap;
        }

        @Override
        public T call() throws Exception {
            MDC.setContextMap(contextMap);
            try {
                return task.call();
            } finally {
                MDC.clear();
            }
        }
    }

    class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {
        @Override
        public  <T> Future<T> submit(Callable<T> task) {
            return super.submit(new ContextAwareCallable(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
            return super.submitListenable(new ContextAwareCallable(task, MDC.getCopyOfContextMap()));
        }
    }


    /**
     * 带有日志上下文的线程池
     * @return
     */
    @Bean("taskExecutor1")
    public ThreadPoolTaskExecutor getAsyncExecutor1(){
        ThreadPoolTaskExecutor executor = new ContextAwarePoolExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(10);
        executor.setThreadNamePrefix("Async-1-Service-");
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }


    /**
     * 带有日志上下文的线程池
     * @return
     */
    @Bean("taskExecutor2")
    public ThreadPoolTaskExecutor getAsyncExecutor2(){
        ThreadPoolTaskExecutor executor = new ContextAwarePoolExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(5000);
        executor.setKeepAliveSeconds(10);
        executor.setThreadNamePrefix("Async-2-Service-");
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}
