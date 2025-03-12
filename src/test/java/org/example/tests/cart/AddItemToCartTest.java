package org.example.tests.cart;

import org.example.utils.BaseTest;
import org.example.pages.LoginPage;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import static org.junit.Assert.*;

public class AddItemToCartTest extends BaseTest {
    private LoginPage loginPage; // Página de login

    @Before
    public void setUpTest() {
        super.setUp(); // Llamamos al setup de BaseTest
        loginPage = new LoginPage(driver);
        loginPage.navigateToLogin();
        loginPage.login("standard_user", "secret_sauce"); // Credenciales de prueba
    }

    @Test
    public void addItemToCart() {
        // Agregar el primer ítem al carrito
        WebElement firstItemAddButton = driver.findElement(By.className("btn_inventory"));
        firstItemAddButton.click();

        // Verificar que el ítem fue agregado al carrito
        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
        assertEquals("1", cartBadge.getText());

        System.out.println("El ítem se agregó correctamente al carrito.");
    }

    @After
    public void tearDownTest() {
        super.tearDown(); // Llamamos al teardown de BaseTest
    }
}