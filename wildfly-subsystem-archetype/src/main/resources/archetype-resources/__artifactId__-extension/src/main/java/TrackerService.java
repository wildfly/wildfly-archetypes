package ${package};

import static ${package}._private.SubsystemLogger.LOGGER;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;

/**
 * Service that tracks the number of deployments
 */
public class TrackerService {

    public final AtomicInteger deployments = new AtomicInteger(0);
    private final ScheduledFuture<?> future;

    public TrackerService(ManagedScheduledExecutorService executor, long tick) {
        LOGGER.checkTick(tick);
        future = executor.scheduleAtFixedRate(() -> {
            LOGGER.numberOfDeployments(deployments.get());
        }, tick, tick, TimeUnit.SECONDS);
    }


    public void stop() {
        future.cancel(true);
    }
}
