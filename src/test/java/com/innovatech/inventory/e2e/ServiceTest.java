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

public class ServiceTest {

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
    public void SystemTest_Service_Complete() {
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

        // Navega a la sección de agregar servicios
        WebElement servicesButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("linkInventario")));
        servicesButton.click();

        WebElement addServiceButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("addService")));
        addServiceButton.click();

        // Ingresa los datos del servicio
        WebElement inputName = wait.until(ExpectedConditions.elementToBeClickable(By.id("nombre")));
        WebElement inputPrice = wait.until(ExpectedConditions.elementToBeClickable(By.id("ValorUnidad")));
        WebElement inputInitialDate = wait.until(ExpectedConditions.elementToBeClickable(By.id("initialDate")));
        WebElement inputFinalDate = wait.until(ExpectedConditions.elementToBeClickable(By.id("finalDate")));
        WebElement inputDesc = wait.until(ExpectedConditions.elementToBeClickable(By.id("descripcion")));

        WebElement uploadElement = driver.findElement(By.id("imagenUpload"));

        inputName.sendKeys("Servicio 1");
        inputPrice.sendKeys("100");
        inputInitialDate.sendKeys("01-01-2022");
        inputFinalDate.sendKeys("01-01-2023");
        inputDesc.sendKeys("Primer servicio");

        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "/src/test/java/com/innovatech/demo/e2e/productTest.jpg";

        uploadElement.sendKeys(filePath);

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-add-service/body/section/div[2]/div/a")));
        saveButton.click();

        // Edita el servicio
        WebElement editServiceButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-all-services/body/section/div[2]/table/tbody/tr/td[5]/a[1]")));
        editServiceButton.click();

        WebElement inputEditName = wait.until(ExpectedConditions.elementToBeClickable(By.id("nombre")));
        WebElement inputEditDescription = wait.until(ExpectedConditions.elementToBeClickable(By.id("descripcion")));

        WebElement uploadEditElement = driver.findElement(By.id("imagenUpload"));

        inputEditName.sendKeys(" editado");
        inputEditDescription.sendKeys(" Este es un servicio editado");

        String projectPathEdit = System.getProperty("user.dir");
        String filePathEdit = projectPathEdit + "/src/test/java/com/innovatech/demo/e2e/productTestEdit.jpg";

        uploadEditElement.sendKeys(filePathEdit);

        WebElement saveEditButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-info-service/body/section/div[2]/div[2]/button")));
        saveEditButton.click();

        // Elimina el servicio
        WebElement deleteServiceButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-all-services/body/section/div[2]/table/tbody/tr/td[5]/a[2]")));
        deleteServiceButton.click();

        WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-delete-service/body/section/div[2]/div[2]/button[2]")));
        confirmDeleteButton.click();

        WebElement confirmDelete = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[1]")));
        confirmDelete.click();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
    
}
