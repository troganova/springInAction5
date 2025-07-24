package tacos.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomePageBrowserTest {

    @LocalServerPort
    private int port;
    private static HtmlUnitDriver browser;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setup() {
        browser = new HtmlUnitDriver(true); // Включаем JavaScript
        wait = new WebDriverWait(browser, Duration.ofSeconds(10));
    }

    @AfterAll
    public static void teardown() {
        if (browser != null) {
            browser.quit();
        }
    }

    @Test
    public void testHomePage() {
        String homePage = "http://localhost:" + port;
        browser.get(homePage);

        // 1. Проверяем заголовок страницы
        wait.until(titleIs("Taco Cloud"));
        assertThat(browser.getTitle()).isEqualTo("Taco Cloud");

        // 2. Проверяем заголовок h1
        wait.until(visibilityOfElementLocated(By.tagName("h1")));
        assertThat(browser.findElement(By.tagName("h1")).getText())
                .isEqualTo("Welcome to...");

        // 3. Проверяем изображение
        wait.until(visibilityOfElementLocated(By.tagName("img")));
        assertThat(browser.findElement(By.tagName("img")).getAttribute("src"))
                .isEqualTo(homePage + "/images/TacoCloud.png");
    }
}
