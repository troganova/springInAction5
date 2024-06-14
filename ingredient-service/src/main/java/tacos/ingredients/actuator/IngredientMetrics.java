package tacos.ingredients.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.stereotype.Service;
import tacos.ingredients.Ingredient;
import tacos.ingredients.IngredientRepository;

import javax.management.Notification;
import java.util.concurrent.atomic.AtomicLong;

@Service
@ManagedResource
public class IngredientMetrics extends AbstractRepositoryEventListener<Ingredient> implements NotificationPublisherAware {

    private MeterRegistry meterRegistry;
    private AtomicLong counter;
    private NotificationPublisher notificationPublisher;

    @Autowired
    public IngredientMetrics(MeterRegistry meterRegistry, IngredientRepository ingredientRepository) {
        this.meterRegistry = meterRegistry;
        long initialCount = ingredientRepository.count();
        this.counter = new AtomicLong(initialCount);
    }
    @Override
    protected void onAfterCreate(Ingredient ingredient) {
        super.onAfterCreate(ingredient);
        counter.incrementAndGet();
        meterRegistry.counter("tacocloud", "ingredient", ingredient.getId()).increment();
    }

    @ManagedAttribute
    public long getCount() {
        return counter.get();
    }

    @ManagedOperation
    public long increment(long delta) {
        long before = counter.get();
        long after = counter.addAndGet(delta);
        if((after/100) > (before /100)) {
            Notification notification = new Notification(
                    "ingredient.count", this, before, after, "th ingredient created!");
            notificationPublisher.sendNotification(notification);
        }
        return after;
    }

    @Override
    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }
}
