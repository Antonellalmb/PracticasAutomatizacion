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

public class CheckoutFormNameValidationTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        // Configurar el driver de Chrome
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
    public void checkoutFormNameValidation() {
        try {
            // Agregar ítems al carrito
            WebElement firstItemAddButton = driver.findElement(By.className("btn_inventory"));
            firstItemAddButton.click();

            // Navegar al carrito
            WebElement cartButton = driver.findElement(By.id("shopping_cart_container"));
            cartButton.click();

            // Proceder al checkout
            WebElement checkoutButton = driver.findElement(By.id("checkout"));
            checkoutButton.click();

            // Complet  con nombre y apellido inválidos
            WebElement firstNameField = driver.findElement(By.id("first-name"));
            WebElement lastNameField = driver.findElement(By.id("last-name"));
            WebElement postalCodeField = driver.findElement(By.id("postal-code"));
            WebElement continueButton = driver.findElement(By.id("continue"));

            firstNameField.sendKeys("123");
            lastNameField.sendKeys("!@#");
            postalCodeField.sendKeys("5390");
            continueButton.click();

            // Validación 1: Verificar que no se permite continuar con nombres inválidos
            boolean isErrorMessageDisplayed = driver.findElements(By.xpath("//h3[@data-test='error']")).size() > 0;
            assert isErrorMessageDisplayed : "El formulario permitió continuar con nombres inválidos.";

            // Validación 2: Verificar que el mensaje de error es el correcto
            WebElement errorMessage = driver.findElement(By.xpath("//h3[@data-test='error']"));
            assert errorMessage.getText().contains("Error: First Name is required") : "El mensaje de error no es el esperado.";

            System.out.println("El formulario no permitió continuar con nombres inválidos, prueba fallida.");

        } catch (Exception e) {
            takeScreenshot("checkoutFormNameValidationError.png");
            e.printStackTrace();
            assert false : "Ocurrió un error durante la validación del formulario de compra.";
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
