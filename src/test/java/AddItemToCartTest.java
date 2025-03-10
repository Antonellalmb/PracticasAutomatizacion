import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class AddItemToCartTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Navegar a la página de inicio de sesión
        driver.get("https://www.saucedemo.com/");

        // Iniciar sesión
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        usernameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
        loginButton.click();

        // Verificar que la página de inventario se ha cargado
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/inventory.html"));
    }

    @Test
    public void addItemToCart() {
        try {
            // Agrego el primer ítem al carrito
            WebElement firstItemAddButton = driver.findElement(By.className("btn_inventory"));
            firstItemAddButton.click();

            // Verificar que el ítem fue agregado al carrito
            WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
            assert cartBadge.getText().equals("1") : "El ítem no se agregó correctamente al carrito.";

            System.out.println("El ítem se agregó correctamente al carrito.");
        } catch (Exception e) {
            takeScreenshot("addItemToCartError.png");
            e.printStackTrace();
        }
    }

    private void takeScreenshot(String fileName) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            File destinationFile = new File("./src/test/resources/screenshots/" + fileName);
            FileUtils.copyFile(screenshot, destinationFile);
            System.out.println("Captura de pantalla guardada en: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al guardar la captura de pantalla");
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
