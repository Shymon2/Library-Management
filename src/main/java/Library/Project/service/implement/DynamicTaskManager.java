package Library.Project.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Component
public class DynamicTaskManager {

    @Autowired
    private TaskScheduler taskScheduler;

    private ScheduledFuture<?> emailTask;
    private ScheduledFuture<?> reportTask;

    // Start email task
    public void startEmailTask() {
        if (emailTask == null || emailTask.isCancelled()) {
            emailTask = taskScheduler.scheduleAtFixedRate(() -> {
                System.out.println("Email task running on: " + Thread.currentThread().getName());
            }, Duration.ofSeconds(5));
        }
    }

    // Start report task
    public void startReportTask() {
        if (reportTask == null || reportTask.isCancelled()) {
            reportTask = taskScheduler.scheduleAtFixedRate(() -> {
                System.out.println("Report task running on: " + Thread.currentThread().getName());
            }, Duration.ofSeconds(10));
        }
    }

    // Pause email task
    public void pauseEmailTask() {
        if (emailTask != null && !emailTask.isCancelled()) {
            emailTask.cancel(false); // false = không interrupt thread nếu đang chạy
            System.out.println("Email task paused");
        }
    }

    // Pause report task
    public void pauseReportTask() {
        if (reportTask != null && !reportTask.isCancelled()) {
            reportTask.cancel(false);
            System.out.println("Report task paused");
        }
    }
}
