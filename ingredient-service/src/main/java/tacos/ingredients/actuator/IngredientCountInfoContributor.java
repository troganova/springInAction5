package tacos.ingredients.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tacos.ingredients.IngredientRepository;

import java.util.HashMap;
import java.util.Map;

@Profile("actuator")
@Component
public class IngredientCountInfoContributor implements InfoContributor {

    private IngredientRepository ingredientRepository;

    public IngredientCountInfoContributor(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public void contribute(Info.Builder builder) {
        long ingredientCount = ingredientRepository.count();
        Map<String, Object> ingredientMap = new HashMap<>();
        ingredientMap.put("count", ingredientCount);
        builder.withDetail("ingredient-stats", ingredientMap);
    }
}
