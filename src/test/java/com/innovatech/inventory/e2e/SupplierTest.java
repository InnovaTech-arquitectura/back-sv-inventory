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
public class SupplierTest {
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
    public void SystemTest_Supplier_Complete() {
        //Busca pagina y la maximiza
        driver.get(BASE_URL + "login");
        driver.manage().window().maximize();

        //Ingresa los datos e inicia sessión
        WebElement inputMail = driver.findElement(By.id("email"));
        WebElement inputPassword = driver.findElement(By.id("password"));
        WebElement inputUser = driver.findElement(By.id("tipoUser"));

        inputMail.sendKeys("prueba@mail.com");
        inputPassword.sendKeys("1234");
        Select selectUser = new Select(inputUser);
        selectUser.selectByIndex(1);

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-inicio-sesion/div/div[2]/form/button")));
        loginButton.click();

        // Navega a la sección de agregar proveedores
        WebElement suppliersButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("linkSupplier")));
        suppliersButton.click();

        WebElement addSupplierButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("addSupplier")));
        addSupplierButton.click();

        // Ingresa los datos de un proveedor
        WebElement inputName = wait.until(ExpectedConditions.elementToBeClickable(By.id("nombre")));
        WebElement inputContact = wait.until(ExpectedConditions.elementToBeClickable(By.id("contacto")));
        WebElement inputDesc = wait.until(ExpectedConditions.elementToBeClickable(By.id("descripcion")));

        inputName.sendKeys("Proveedor 1");
        inputContact.sendKeys("3213228515");
        inputDesc.sendKeys("Proveedor de coca cola");

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-add-supplier/body/section/div[2]/div/a")));
        saveButton.click();

        // Edita el proveedor
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-all-suppliers/body/section/div[2]/table/tbody/tr/td[4]/a[1]")));
        editButton.click();

        WebElement inputDescEdit = wait.until(ExpectedConditions.elementToBeClickable(By.id("descripcion")));
        inputDescEdit.sendKeys(" editado");

        WebElement saveButtonEdit = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-info-supplier/body/section/div[2]/div[2]/button")));
        saveButtonEdit.click();

        WebElement confirmEdit = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[1]")));
        confirmEdit.click();

        // Elimina el proveedor
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-all-suppliers/body/section/div[2]/table/tbody/tr/td[4]/a[2]")));
        deleteButton.click();

        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[3]")));
        confirmButton.click();

        WebElement confirmDelete = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[1]")));
        confirmDelete.click();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
