package auditLogging;

import java.util.concurrent.*;

public class AuditLogProcessor {
    private static final int CORE_POOL_SIZE = 10; 
    private static final int MAX_POOL_SIZE = 10;
    private static final long KEEP_ALIVE_TIME = 60L;

    private final ExecutorService executorService;
    private final BlockingQueue<Runnable> queue;

    public AuditLogProcessor() {
        this.queue = new LinkedBlockingQueue<>();
        this.executorService = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            queue
        );
    }

    public void submitAuditLogTask(Runnable task) {
        executorService.submit(task);
    }

    public void shutdownAndAwaitTermination() {
        executorService.shutdown();
        try {
            // Wait for all tasks to finish
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

    
        processRemainingTasks();
    }

    private void processRemainingTasks() {
        while (!queue.isEmpty()) {
            try {
                Runnable task = queue.poll();
                if (task != null) {
                    task.run();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

