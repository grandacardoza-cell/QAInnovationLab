
package com.nttdata.stepsdefinitions;

import com.nttdata.screens.AppConfigScreen;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CarritoStepsDefs {

    private final AppConfigScreen app = new AppConfigScreen();
    private AndroidDriver driver;
    private WebDriverWait wait;
    private int cantidadEsperada;
    private String productoEsperado;

    private static final String PKG =
            "com.saucelabs.mydemoapp.android:id/";

    @Given("estoy en la aplicación de SauceLabs")
    public void iniciar() {
        app.iniciarAplicacion();
        driver = AppConfigScreen.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @And("valido que carguen correctamente los productos en la galería")
    public void validarGaleria() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id(PKG + "productRV")
        ));
    }

    @When("agrego {int} del producto {string}")
    public void agregarProducto(int unidades, String producto) {

        cantidadEsperada = unidades;
        productoEsperado = producto;

        // Seleccionar el producto por su nombre accesible
        WebElement imagen = wait.until(
                ExpectedConditions.elementToBeClickable(
                        AppiumBy.accessibilityId(producto)
                )
        );
        imagen.click();

        imagen.click();

        System.out.println("PAQUETE ACTUAL: " + driver.getCurrentPackage());
        System.out.println("ACTIVIDAD ACTUAL: " + driver.currentActivity());
        System.out.println("PRODUCTO: " + producto);

        System.out.println("Producto seleccionado: " + producto);
        System.out.println("Pantalla actual: " + driver.currentActivity());
        System.out.println("XML actual: " + driver.getPageSource());
        // Verificar que se abrió el producto correcto
        WebElement titulo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id(PKG + "productTV")
                )
        );

        if (!titulo.getText().equals(producto)) {
            throw new AssertionError(
                    "Producto incorrecto: " + titulo.getText()
            );
        }

        // La cantidad inicial observada es 1
        WebElement cantidad = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id(PKG + "noTV")
                )
        );

        int actual = Integer.parseInt(cantidad.getText());

        while (actual < unidades) {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id(PKG + "plusIV")
            )).click();

            int anterior = actual;
            wait.until(d ->
                    Integer.parseInt(
                            d.findElement(By.id(PKG + "noTV")).getText()
                    ) > anterior
            );

            actual = Integer.parseInt(
                    driver.findElement(By.id(PKG + "noTV")).getText()
            );
        }

        if (actual != unidades) {
            throw new AssertionError(
                    "Cantidad incorrecta: " + actual
            );
        }

        // Agregar producto
        wait.until(ExpectedConditions.elementToBeClickable(
                By.id(PKG + "cartBt")
        )).click();
    }



    @Then("valido que el carrito actualice correctamente")
    public void validarCarrito() {

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id(PKG + "cartRL")
        )).click();


        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id(PKG + "productRV")
        ));

        System.out.println("===== XML DEL CARRITO =====");
        System.out.println(driver.getPageSource());
        System.out.println("===== FIN XML =====");

        // Localizar el título del producto en el carrito
        WebElement producto = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@resource-id='" + PKG + "titleTV' " +
                                "and @text='" + productoEsperado + "']")
                )
        );

        // Buscar la cantidad dentro del contenedor del producto
        WebElement cantidad = producto.findElement(
                By.xpath("./ancestor::*[.//*[@resource-id='" +
                        PKG + "noTV']][1]//*[@resource-id='" +
                        PKG + "noTV']")
        );

        int cantidadObtenida =
                Integer.parseInt(cantidad.getText().trim());

        if (cantidadObtenida != cantidadEsperada) {
            throw new AssertionError(
                    "Cantidad incorrecta para " + productoEsperado +
                            ". Esperada: " + cantidadEsperada +
                            ", obtenida: " + cantidadObtenida
            );
        }

        System.out.println(
                "VALIDACIÓN CORRECTA: " + productoEsperado +
                        " x " + cantidadObtenida
        );
    }


    @After("@Carrito")
    public void finalizar(Scenario scenario) {
        if (driver != null) {
            try {
                scenario.attach(
                        driver.getScreenshotAs(OutputType.BYTES),
                        "image/png",
                        "Evidencia - " + scenario.getName()
                );
            } finally {
                driver.quit();
                driver = null;
            }
        }
    }
}
