package tacos.ingredients;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
