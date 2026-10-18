package com.catalogue.product_catalogue.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "ADMIN_EMAIL=admin@test.local",
                "ADMIN_PASSWORD=AdminTest#123",
                "USER_EMAIL=user@test.local",
                "USER_PASSWORD=UserTest#123"
        })
class CatalogueSeleniumTests {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @LocalServerPort
    private int port;

    @BeforeAll
    static void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1280,900",
                "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    static void stopBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    void clearSession() {
        driver.manage().deleteAllCookies();
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private void login(String email, String password) {
        driver.get(url("/login"));
        driver.findElement(By.id("username")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    private void loginAsUser() {
        login("user@test.local", "UserTest#123");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("browse-link")));
    }

    private void loginAsAdmin() {
        login("admin@test.local", "AdminTest#123");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("admin-link")));
    }

    private WebElement waitFor(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private List<WebElement> rows() {
        waitFor(By.id("item-table"));
        return driver.findElements(By.cssSelector(".item-row"));
    }

    @Test
    void unauthenticatedUserIsRedirectedToLogin() {
        driver.get(url("/items"));
        assertTrue(driver.getCurrentUrl().endsWith("/login"));
    }

    @Test
    void loginWithWrongPasswordShowsError() {
        login("user@test.local", "wrong-password");
        assertTrue(waitFor(By.id("login-error")).isDisplayed());
    }

    @Test
    void userCanLoginAndSeesCatalogue() {
        loginAsUser();
        driver.findElement(By.id("browse-link")).click();
        assertTrue(rows().size() >= 8);
        assertEquals("user@test.local", waitFor(By.id("current-user")).getText());
    }

    @Test
    void newVisitorCanRegisterThenSeesConfirmation() {
        driver.get(url("/register"));
        driver.findElement(By.name("name")).sendKeys("Selenium Tester");
        driver.findElement(By.name("email")).sendKeys("selenium@test.local");
        driver.findElement(By.name("password")).sendKeys("Selenium#123");
        driver.findElement(By.id("register-button")).click();
        assertTrue(waitFor(By.id("registered-message")).isDisplayed());
    }

    @Test
    void searchByNameFiltersItems() {
        loginAsUser();
        driver.get(url("/items"));
        driver.findElement(By.id("search-input")).sendKeys("Mouse");
        driver.findElement(By.id("search-button")).click();
        assertEquals(1, rows().size());
        assertEquals("Wireless Mouse", rows().get(0).findElement(By.className("item-name")).getText());
    }

    @Test
    void searchWithNoMatchShowsEmptyMessage() {
        loginAsUser();
        driver.get(url("/items?q=doesnotexist"));
        assertEquals("No products found", waitFor(By.id("empty-message")).getText());
    }

    @Test
    void categoryFilterShowsOnlyThatCategory() {
        loginAsUser();
        driver.get(url("/items?category=Stationery"));
        assertEquals(2, rows().size());
    }

    @Test
    void stockStatusBadgesAreShown() {
        loginAsUser();
        driver.get(url("/items"));
        String page = waitFor(By.id("item-table")).getText();
        assertTrue(page.contains("In stock"));
        assertTrue(page.contains("Low stock"));
        assertTrue(page.contains("Out of stock"));
    }

    @Test
    void normalUserCannotSeeAdminControlsOrOpenAdminPages() {
        loginAsUser();
        assertTrue(driver.findElements(By.id("admin-link")).isEmpty());
        driver.get(url("/admin/items/new"));
        assertEquals("Access denied", waitFor(By.id("access-denied")).getText());
    }

    @Test
    void adminCanAddItemAndItAppearsInCatalogue() {
        loginAsAdmin();
        driver.get(url("/admin/items/new"));
        driver.findElement(By.id("name")).sendKeys("Selenium Keyboard");
        driver.findElement(By.id("category")).sendKeys("Electronics");
        driver.findElement(By.id("price")).sendKeys("1999.00");
        driver.findElement(By.id("quantity")).sendKeys("3");
        driver.findElement(By.id("save-button")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("flash-message")));
        assertTrue(driver.findElement(By.id("flash-message")).getText().contains("Item added"));
        assertTrue(waitFor(By.id("item-table")).getText().contains("Selenium Keyboard"));
    }

    @Test
    void adminFormRejectsEmptyName() {
        loginAsAdmin();
        driver.get(url("/admin/items/new"));
        driver.findElement(By.id("save-button")).click();
        waitFor(By.cssSelector(".error"));
        assertEquals("Add item", waitFor(By.id("form-title")).getText());
    }

    @Test
    void logoutReturnsToLoginPage() {
        loginAsUser();
        driver.findElement(By.id("logout-button")).click();
        assertTrue(waitFor(By.id("logout-message")).isDisplayed());
    }
}
