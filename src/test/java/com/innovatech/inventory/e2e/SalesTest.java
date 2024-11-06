package com.innovatech.inventory.e2e;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SalesTest {
    private final String BASE_URL = "http://10.43.101.180/";

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void init() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("--disable-extensions");

        this.driver = new ChromeDriver(chromeOptions);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @Test
    public void SystemTest_Sales_Complete() {
        // Busca pagina y la maximiza
        driver.get(BASE_URL + "login");
        driver.manage().window().maximize();

        // Ingresa los datos e inicia sessión
        WebElement inputMail = driver.findElement(By.id("email"));
        WebElement inputPassword = driver.findElement(By.id("password"));
        WebElement inputUser = driver.findElement(By.id("tipoUser"));

        inputMail.sendKeys("prueba1@mail.com");
        inputPassword.sendKeys("1234");
        Select selectUser = new Select(inputUser);
        selectUser.selectByIndex(1);

        WebElement loginButton = wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("/html/body/app-root/app-inicio-sesion/div/div[2]/form/button")));
        loginButton.click();

        // Navega a la sección de agregar proveedores
        WebElement suppliersButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("linkVentas")));
        suppliersButton.click();

        // Navega a la sección de agregar ventas
        WebElement addSaleButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-list-sales/body/section/div[3]/a")));
        addSaleButton.click();

        WebElement inputProduct = wait.until(ExpectedConditions.elementToBeClickable(By.id("IDproducto")));
        WebElement inputCantidad = wait.until(ExpectedConditions.elementToBeClickable(By.id("cantidad")));
        WebElement inputNumeroVenta = wait.until(ExpectedConditions.elementToBeClickable(By.id("numeroVenta")));

        inputProduct.sendKeys("6");
        inputCantidad.sendKeys("1");
        inputNumeroVenta.sendKeys("Sale-113");

        WebElement addSalesButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("agregarVenta")));
        addSalesButton.click();

        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[1]")));
        confirmButton.click();

    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
