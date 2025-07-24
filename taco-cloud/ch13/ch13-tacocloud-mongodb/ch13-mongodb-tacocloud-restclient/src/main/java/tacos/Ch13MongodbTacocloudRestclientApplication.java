package tacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootConfiguration
@ComponentScan
public class Ch13MongodbTacocloudRestclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ch13MongodbTacocloudRestclientApplication.class, args);
	}

	@Bean
	public WebClient webClient() {
		return WebClient.create("http://localhost:8080/api");
	}

	@Bean
	public CommandLineRunner fetchIngredients(TacoCloudClient tacoCloudClient) {
		return args -> {
			log.info("----------------------- GET -------------------------");
			log.info("GETTING INGREDIENT BY IDE");
			tacoCloudClient.getIngredientBySlug("CHED").doOnNext(ingredient -> log.info("Ingredient:  {}", ingredient)).block(Duration.ofSeconds(5));
			log.info("GETTING ALL INGREDIENTS");
			List<Ingredient> ingredients = tacoCloudClient.getAllIngredients()
					.collectList()
					.block(Duration.ofSeconds(5));
			ingredients.forEach(i -> log.info(" - {}", i));
		};
	}

	@Bean
	public CommandLineRunner putAnIngredient(TacoCloudClient tacoCloudClient) {
		return args -> {
			log.info("----------------------- PUT -------------------------");
			CountDownLatch latch = new CountDownLatch(1);

			// 1. Получаем текущий ингредиент
			// 2. Обновляем его
			// 3. Получаем обновлённую версию
			tacoCloudClient.getIngredientBySlug("LETC")
					.flatMap(current -> {
						log.info("BEFORE UPDATE: {}", current);

						Ingredient updated = new Ingredient("LETC",
								"Shredded Lettuce",  // Новое название
								Ingredient.Type.VEGGIES  // Новый тип
						);

						return tacoCloudClient.updateIngredient(updated)
								.then(tacoCloudClient.getIngredientBySlug("LETC"));
					})
					.subscribe(
							updated -> log.info("AFTER UPDATE: {}", updated),
							error -> log.error("UPDATE FAILED: ", error),
							() -> {
								log.info("UPDATE PROCESS COMPLETED");
								latch.countDown();
							}
					);

			latch.await(10, TimeUnit.SECONDS);  // Ожидаем завершения
		};
	}

	@Bean
	public CommandLineRunner addAnIngredient(TacoCloudClient tacoCloudClient) {
		return args -> {
			log.info("----------------------- POST -------------------------");
			CountDownLatch latch = new CountDownLatch(3); // Ожидаем 3 операции

			// 1. Создаем ингредиенты последовательно с логированием
			Mono<Ingredient> operations = Mono.just("CHIX")
					.flatMap(id -> createAndLogIngredient(tacoCloudClient,
							new Ingredient(id, "Shredded Chicken", Ingredient.Type.PROTEIN),
							latch))
					.then(Mono.just("BFFJ"))
					.flatMap(id -> createAndLogIngredient(tacoCloudClient,
							new Ingredient(id, "Beef Fajita", Ingredient.Type.PROTEIN),
							latch))
					.then(Mono.just("SHMP"))
					.flatMap(id -> createAndLogIngredient(tacoCloudClient,
							new Ingredient(id, "Shrimp", Ingredient.Type.PROTEIN),
							latch));

			operations.subscribe(
					null,
					error -> log.error("Error occurred: ", error),
					() -> log.info("All ingredients processed")
			);

			latch.await(10, TimeUnit.SECONDS); // Ожидаем завершения всех операций
		};
	}

	private Mono<Ingredient> createAndLogIngredient(TacoCloudClient client,
													Ingredient ingredient,
													CountDownLatch latch) {
		return client.createIngredient(Mono.just(ingredient))
				.doOnNext(created -> log.info("CREATED: {}", created))
				.doOnTerminate(latch::countDown)
				.doOnError(error -> log.error("Failed to create {}: {}", ingredient.getId(), error.getMessage()));
	}


	@Bean
	public CommandLineRunner deleteAnIngredient(TacoCloudClient tacoCloudClient) {
		return args -> {
			log.info("----------------------- DELETE -------------------------");
			CountDownLatch latch = new CountDownLatch(1);

			// 1. Сначала создаем ингредиенты для теста
			Flux.just(
							new Ingredient("BFFJ", "Beef Fajita", Ingredient.Type.PROTEIN),
							new Ingredient("SHMP", "Shrimp", Ingredient.Type.PROTEIN),
							new Ingredient("CHIX", "Shredded Chicken", Ingredient.Type.PROTEIN)
					)
					.flatMap(ingredient -> tacoCloudClient.createIngredient(Mono.just(ingredient)))
					.thenMany(Flux.just("CHIX", "BFFJ", "SHMP"))
					.flatMap(slug -> {
						// 2. Для каждого ингредиента: получаем, логируем, удаляем, проверяем
						return tacoCloudClient.getIngredientBySlug(slug)
								.flatMap(before -> {
									log.info("BEFORE DELETE {}: {}", slug, before);
									return tacoCloudClient.deleteIngredient(Mono.just(before))
											.then(tacoCloudClient.getIngredientBySlug(slug))
											.doOnNext(after -> log.info("AFTER DELETE {}: {}", slug, after))
											.onErrorResume(e -> {
												log.error("Error processing {}: {}", slug, e.getMessage());
												return Mono.empty();
											});
								});
					})
					.doOnComplete(() -> {
						log.info("All delete operations completed");
						latch.countDown();
					})
					.subscribe();

			latch.await(10, TimeUnit.SECONDS);
		};
	}

}
