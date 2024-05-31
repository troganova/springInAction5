package tacos.ingredients.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;
import tacos.ingredients.Ingredient;

@Component
public class IngredientMetrics extends AbstractRepositoryEventListener<Ingredient> {

    private MeterRegistry meterRegistry;

    @Autowired
    public IngredientMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    @Override
    protected void onAfterCreate(Ingredient ingredient) {
        super.onAfterCreate(ingredient);
        meterRegistry.counter("tacocloud", "ingredient", ingredient.getId()).increment();
    }
}
