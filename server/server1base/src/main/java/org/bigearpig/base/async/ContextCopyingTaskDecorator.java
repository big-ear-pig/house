package org.bigearpig.base.async;

import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextCopyingTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		// 在主线程中捕获当前上下文
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 还可以捕获其他上下文信息，如MDC中的日志追踪ID等

		return () -> {
			try {
				// 在新线程中恢复上下文
				SecurityContextHolder.getContext().setAuthentication(authentication);
				// 恢复其他上下文...

				runnable.run();
			} finally {
				// 清理线程上下文，避免内存泄漏
				SecurityContextHolder.clearContext();
			}
		};
	}
}