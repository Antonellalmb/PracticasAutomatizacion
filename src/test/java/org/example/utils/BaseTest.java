package org.example.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;  // Driver accesible para pruebas hijas
    protected WebDriverWait wait; // Manejo de esperas

    public void setUp() {
        // Configurar WebDriverManager para que maneje el driver de Chrome
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        // Definir el tiempo de espera para los elementos
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Maximiza ventana del navegador
        driver.manage().window().maximize();
    }

    public void tearDown() {
        // Cerrar el navegador después de la prueba
        if (driver != null) {
            driver.quit();
        }
    }
}