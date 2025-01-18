package com.taufeeq.web.auditlog;

import com.taufeeq.web.logger.CustomLogger;

import java.util.concurrent.*;

public class AuditLogProcessor {
	private static final int CORE_POOL_SIZE = 10;
	private static final int MAX_POOL_SIZE = 10;
	private static final long KEEP_ALIVE_TIME = 60L;
	private static final CustomLogger logger = CustomLogger.getInstance();

	private final ExecutorService executorService;
	private final BlockingQueue<Runnable> queue;

	public AuditLogProcessor() {
		this.queue = new LinkedBlockingQueue<>();
		this.executorService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				queue);
	}

	public void submitAuditLogTask(Runnable task) {
		executorService.submit(task);
		logger.infoApp("Audit log task submitted to executor service. Queue size: " + queue.size());
	}

	public void shutdownAndAwaitTermination() {
		logger.infoApp("AuditLogProcessor shutdown initiated.");
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
				logger.warnApp("AuditLogProcessor shutdown: Forced shutdown after timeout.");
			} else {
				logger.infoApp("AuditLogProcessor shutdown completed normally.");
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
			logger.errorApp("AuditLogProcessor shutdown interrupted: " + e.getMessage());
		}
	}
}