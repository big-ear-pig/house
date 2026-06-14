package org.bigearpig.base.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class MyAsyncConfig implements AsyncConfigurer {
	@Value("${spring.task.execution.pool.core-size:8}")
	private int coreSize;
	@Value("${spring.task.execution.pool.max-size:16}")
	private int maxSize;
	@Value("${spring.task.execution.pool.queue-capacity:8}")
	private int queueCapacity;
	@Value("${spring.task.execution.pool.keep-alive:60}")
	private int keepAlive;
	@Value("${spring.task.execution.thread-name-prefix:taskExecutor-}")
	private String threadNamePrefix;
	
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> {
			log.error(" 异步方法 " + method.getName() + " 执行失败");
			ex.printStackTrace();
		};
	}

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 基础配置
        executor.setCorePoolSize(coreSize);                     // 核心线程数
        executor.setMaxPoolSize(maxSize);                       // 最大线程数
        executor.setQueueCapacity(queueCapacity);               // 任务队列容量
        executor.setKeepAliveSeconds(keepAlive);                // 线程空闲存活时间（秒）
        executor.setThreadNamePrefix(threadNamePrefix);         // 线程名前缀

        // 高级配置（可选）
        executor.setRejectedExecutionHandler(
            new ThreadPoolExecutor.CallerRunsPolicy()          // 拒绝策略（默认AbortPolicy）
        );
        
        executor.setTaskDecorator(new ContextCopyingTaskDecorator()); //装饰器 在异步方法中可以调用security上下文
        
        executor.setAllowCoreThreadTimeOut(true);              // 允许核心线程超时释放
        executor.setWaitForTasksToCompleteOnShutdown(true);     // 优雅停机等待任务完成
        executor.setAwaitTerminationSeconds(60);               // 停机等待超时时间（秒）

        // 初始化线程池
        executor.initialize();
        return executor;
    }
}
