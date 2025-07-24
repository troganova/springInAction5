package tacos;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@SpringBootApplication
public class Ch13CassandraApiTacocloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ch13CassandraApiTacocloudApplication.class, args);
	}

	// To avoid 404s when using Angular HTML 5 routing
	@Bean
	ErrorViewResolver supportPathBasedLocationStrategyWithoutHashes() {
		return new ErrorViewResolver() {
			@Override
			public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
				return status == HttpStatus.NOT_FOUND
						? new ModelAndView("index.html", Collections.<String, Object>emptyMap(), HttpStatus.OK)
						: null;
			}
		};
	}

	@Bean
	public CommandLineRunner dataLoader(
			PaymentMethodRepository paymentMethodRepo,
			IngredientRepository ingredientRepository,
			UserRepository userRepo,
			PasswordEncoder encoder,
			TacoRepository tacoRepo) {
		return args -> {
			IngredientUDT flourTortilla = saveAnIngredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP, ingredientRepository);
			IngredientUDT cornTortilla = saveAnIngredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP, ingredientRepository);
			IngredientUDT groundBeef = saveAnIngredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN, ingredientRepository);
			IngredientUDT carnitas = saveAnIngredient("CARN", "Carnitas", Ingredient.Type.PROTEIN, ingredientRepository);
			IngredientUDT tomatoes = saveAnIngredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES, ingredientRepository);
			IngredientUDT lettuce = saveAnIngredient("LETC", "Lettuce", Ingredient.Type.VEGGIES, ingredientRepository);
			IngredientUDT cheddar = saveAnIngredient("CHED", "Cheddar", Ingredient.Type.CHEESE, ingredientRepository);
			IngredientUDT jack = saveAnIngredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE, ingredientRepository);
			IngredientUDT salsa = saveAnIngredient("SLSA", "Salsa", Ingredient.Type.SAUCE, ingredientRepository);
			IngredientUDT sourCream = saveAnIngredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE, ingredientRepository);

//        UserUDT u = new UserUDT(username, fullname, phoneNumber)

			userRepo.save(new User("habuma", encoder.encode("password"),
							"Craig Walls", "123 North Street", "Cross Roads", "TX",
							"76227", "123-123-1234", "tanya-pvt@yandex.ru"))
					.map(user -> new UserUDT(user.getUsername(), user.getFullname(), user.getPhoneNumber()))
					.subscribe(user -> {
						paymentMethodRepo.save(new PaymentMethod(user, "4111111111111111", "321", "10/25")).subscribe();
					});

			Taco taco1 = new Taco();
			taco1.setName("Carnivore");
			taco1.setIngredients(Arrays.asList(flourTortilla, groundBeef, carnitas, sourCream, salsa, cheddar));
			tacoRepo.save(taco1).subscribe();

			Taco taco2 = new Taco();
			taco2.setName("Bovine Bounty");
			taco2.setIngredients(Arrays.asList(cornTortilla, groundBeef, cheddar, jack, sourCream));
			tacoRepo.save(taco2).subscribe();

			Taco taco3 = new Taco();
			taco3.setName("Veg-Out");
			taco3.setIngredients(Arrays.asList(flourTortilla, cornTortilla, tomatoes, lettuce, salsa));
			tacoRepo.save(taco3).subscribe();
		};


	}

	private IngredientUDT saveAnIngredient(String id, String name, Ingredient.Type type, IngredientRepository repo) {
		Ingredient ingredient = new Ingredient(id, name, type);
		repo.save(ingredient).subscribe();
		return new IngredientUDT(name, type);
	}


}
