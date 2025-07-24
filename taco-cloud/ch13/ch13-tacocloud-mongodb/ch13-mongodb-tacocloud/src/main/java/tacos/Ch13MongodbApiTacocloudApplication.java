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

import java.util.Collections;
import java.util.Map;

@SpringBootApplication
public class Ch13MongodbApiTacocloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ch13MongodbApiTacocloudApplication.class, args);
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
			IngredientRepository repo,
			UserRepository userRepo,
			PasswordEncoder encoder,
			TacoRepository tacoRepo) {
		return args -> {
			Ingredient flourTortilla = new Ingredient(
					"FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
			Ingredient cornTortilla = new Ingredient(
					"COTO", "Corn Tortilla", Ingredient.Type.WRAP);
			Ingredient groundBeef = new Ingredient(
					"GRBF", "Ground Beef", Ingredient.Type.PROTEIN);
			Ingredient carnitas = new Ingredient(
					"CARN", "Carnitas", Ingredient.Type.PROTEIN);
			Ingredient tomatoes = new Ingredient(
					"TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES);
			Ingredient lettuce = new Ingredient(
					"LETC", "Lettuce", Ingredient.Type.VEGGIES);
			Ingredient cheddar = new Ingredient(
					"CHED", "Cheddar", Ingredient.Type.CHEESE);
			Ingredient jack = new Ingredient(
					"JACK", "Monterrey Jack", Ingredient.Type.CHEESE);
			Ingredient salsa = new Ingredient(
					"SLSA", "Salsa", Ingredient.Type.SAUCE);
			Ingredient sourCream = new Ingredient(
					"SRCR", "Sour Cream", Ingredient.Type.SAUCE);
			repo.save(flourTortilla).subscribe();
			repo.save(cornTortilla).subscribe();
			repo.save(groundBeef).subscribe();
			repo.save(carnitas).subscribe();
			repo.save(tomatoes).subscribe();
			repo.save(lettuce).subscribe();
			repo.save(cheddar).subscribe();
			repo.save(jack).subscribe();
			repo.save(salsa).subscribe();
			repo.save(sourCream).subscribe();

			userRepo.save(new User("habuma", encoder.encode("password"),
					"Craig Walls", "123 North Street", "Cross Roads", "TX",
					"76227", "123-123-1234", "tanya-pvt@yandex.ru"));

			Taco taco1 = new Taco();
			taco1.setId("TACO1");
			taco1.setName("Carnivore");
			taco1.addIngredient(flourTortilla);
			taco1.addIngredient(groundBeef);
			taco1.addIngredient(carnitas);
			taco1.addIngredient(sourCream);
			taco1.addIngredient(salsa);
			taco1.addIngredient(cheddar);
			tacoRepo.save(taco1).subscribe();

			Taco taco2 = new Taco();
			taco2.setId("TACO2");
			taco2.setName("Bovine Bounty");
			taco2.addIngredient(cornTortilla);
			taco2.addIngredient(groundBeef);
			taco2.addIngredient(cheddar);
			taco2.addIngredient(jack);
			taco2.addIngredient(sourCream);
			tacoRepo.save(taco2).subscribe();

			Taco taco3 = new Taco();
			taco3.setId("TACO3");
			taco3.setName("Veg-Out");
			taco3.addIngredient(flourTortilla);
			taco3.addIngredient(cornTortilla);
			taco3.addIngredient(tomatoes);
			taco3.addIngredient(lettuce);
			taco3.addIngredient(salsa);
			tacoRepo.save(taco3).subscribe();
		};
	}
}
