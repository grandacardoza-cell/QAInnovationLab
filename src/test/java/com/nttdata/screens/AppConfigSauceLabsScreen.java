
package com.nttdata.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.nttdata.screens.AppConfigScreen.getDriver;

public class AppConfigSauceLabsScreen {

    private AndroidDriver driver;
    private static final Duration TIMEOUT = Duration.ofSeconds(20);

    private final By txtUsuario =
            AppiumBy.xpath("(//android.widget.EditText)[1]");

    private final By txtClave =
            AppiumBy.accessibilityId("test-Password");

    private final By btnLogin =
            AppiumBy.accessibilityId("test-LOGIN");

    private final By tituloProductos =
            AppiumBy.accessibilityId("test-PRODUCTS");

    public void escribirUsuario(String usuario) {
        driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        WebElement campo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(txtUsuario)
        );

        campo.clear();
        campo.sendKeys(usuario);

        System.out.println("Usuario ingresado");
    }

    public void ingresarClave(String clave) {
        driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        WebElement campo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(txtClave)
        );

        campo.sendKeys(clave);
        System.out.println("Clave ingresada");
    }

    public void ingresar() {
        driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        wait.until(
                ExpectedConditions.elementToBeClickable(btnLogin)
        ).click();

        System.out.println("Clic en LOGIN ejecutado");
    }

    public boolean validarLogin() {
        driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        boolean resultado = wait.until(
                ExpectedConditions.visibilityOfElementLocated(tituloProductos)
        ).isDisplayed();

        System.out.println("Login exitoso: " + resultado);

        return resultado;
    }
}
