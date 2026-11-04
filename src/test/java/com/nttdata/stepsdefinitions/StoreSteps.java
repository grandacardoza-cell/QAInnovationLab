
package com.nttdata.stepsdefinitions;

import com.nttdata.core.DriverManager;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

public class StoreSteps {

    private static final String URL =
            "https://qalab.bensg.com/store";

    private BigDecimal precioUnitario;
    private BigDecimal totalEsperado;

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    private WebDriverWait espera() {
        return new WebDriverWait(
                driver(), Duration.ofSeconds(20)
        );
    }

    private WebElement visible(By selector) {
        return espera().until(
                ExpectedConditions.visibilityOfElementLocated(selector)
        );
    }

    private WebElement clicable(By selector) {
        return espera().until(
                ExpectedConditions.elementToBeClickable(selector)
        );
    }

    private void evidencia() {
        DriverManager.screenShot();
    }

    private BigDecimal precio(String texto) {
        String limpio = texto.replaceAll("[^0-9,.]", "");

        if (limpio.contains(",")) {
            limpio = limpio.replace(".", "")
                    .replace(",", ".");
        }

        return new BigDecimal(limpio);
    }

    // ========================================
    // CP01 - COMPRA CON USUARIO VALIDO
    // ========================================

    @Dado("estoy en la página de la tienda")
    public void abrirTienda() {
        driver().get(URL);
        visible(By.cssSelector("body"));
        evidencia();
    }

    private void iniciarSesion(String correo, String clave) {

        clicable(By.cssSelector(
                "a[href*='iniciar-sesion']"
        )).click();

        visible(By.name("email")).sendKeys(correo);

        driver().findElement(
                By.name("password")
        ).sendKeys(clave);

        clicable(By.cssSelector(
                "button[data-link-action='sign-in']"
        )).click();
    }

    @Cuando("inicio sesión con credenciales válidas")
    public void loginValido() {

        String correo = System.getenv("STORE_EMAIL");
        String clave = System.getenv("STORE_PASSWORD");

        Assert.assertNotNull("Falta STORE_EMAIL", correo);
        Assert.assertNotNull("Falta STORE_PASSWORD", clave);

        iniciarSesion(correo, clave);

        visible(By.cssSelector("a.logout"));

        evidencia();
    }

    @Cuando("navego a la categoría Clothes y subcategoría Men")
    public void navegarCategoria() {

        driver().get(URL + "/es/4-men");

        Assert.assertTrue(
                "No se encontró la categoría Men",
                visible(By.cssSelector("h1"))
                        .getText()
                        .toUpperCase()
                        .contains("MEN")
        );

        evidencia();
    }

    // Vaciar carrito antes de agregar productos
    private void vaciarCarrito() {

        driver().get(URL + "/es/carrito?action=show");

        for (int i = 0; i < 30; i++) {

            List<WebElement> eliminar =
                    driver().findElements(
                            By.cssSelector("a.remove-from-cart")
                    );

            if (eliminar.isEmpty()) {
                break;
            }

            int cantidadAnterior = eliminar.size();

            clicable(By.cssSelector(
                    "a.remove-from-cart"
            )).click();

            espera().until(d ->
                    d.findElements(
                            By.cssSelector("a.remove-from-cart")
                    ).size() < cantidadAnterior
            );
        }

        Assert.assertTrue(
                "No se pudo vaciar el carrito",
                driver().findElements(
                        By.cssSelector("a.remove-from-cart")
                ).isEmpty()
        );
    }


    @Cuando("agrego 2 unidades del primer producto al carrito")
    public void agregarProducto() {
        // Abrir el primer producto
        clicable(By.cssSelector(
                ".products .product-miniature a.product-thumbnail"
        )).click();

        // Obtener precio unitario
        WebElement elementoPrecio =
                visible(By.cssSelector(".current-price span"));

        precioUnitario = precio(elementoPrecio.getText());

        // Establecer exactamente 2 unidades
        WebElement cantidad = visible(By.name("qty"));

        cantidad.click();
        cantidad.sendKeys(
                org.openqa.selenium.Keys.chord(
                        org.openqa.selenium.Keys.CONTROL, "a"
                )
        );
        cantidad.sendKeys("2");
        cantidad.sendKeys(org.openqa.selenium.Keys.TAB);

        // Confirmar cantidad antes de agregar
        espera().until(d ->
                "2".equals(d.findElement(By.name("qty"))
                        .getAttribute("value"))
        );

        Assert.assertEquals(
                "La cantidad seleccionada debe ser 2",
                "2",
                driver().findElement(By.name("qty"))
                        .getAttribute("value")
        );

        totalEsperado =
                precioUnitario.multiply(BigDecimal.valueOf(2));

        System.out.println("Cantidad seleccionada: 2");
        System.out.println("Precio unitario: " + precioUnitario);
        System.out.println("Subtotal esperado: " + totalEsperado);

        evidencia();

        // Agregar al carrito
        clicable(By.cssSelector(
                "button[data-button-action='add-to-cart']"
        )).click();
    }

    @Entonces("valido la confirmación y el precio del producto agregado")
    public void validarConfirmacion() {

        WebElement modal = visible(
                By.id("blockcart-modal")
        );

        Assert.assertTrue(
                "No apareció la confirmación del producto",
                modal.isDisplayed()
        );

        WebElement cantidad = visible(By.cssSelector(
                "#blockcart-modal .product-quantity"
        ));

        Assert.assertTrue(
                "La cantidad del popup no es 2",
                cantidad.getText().contains("2")
        );

        WebElement total = visible(By.cssSelector(
                "#blockcart-modal .cart-content .value"
        ));

        BigDecimal totalPopup = precio(
                total.getText()
        );

        System.out.println(
                "Total esperado popup: " + totalEsperado
        );

        System.out.println(
                "Total obtenido popup: " + totalPopup
        );

        evidencia();

        Assert.assertEquals(
                "El total del popup no coincide",
                0,
                totalEsperado.compareTo(totalPopup)
        );
    }

    @Cuando("finalizo la compra desde la confirmación")
    public void irAlCarrito() {

        clicable(By.cssSelector(
                "#blockcart-modal a.btn.btn-primary"
        )).click();
    }

    @Entonces("valido el título de la página del carrito")
    public void validarTituloCarrito() {

        WebElement titulo = visible(
                By.cssSelector("h1")
        );

        String texto = titulo.getText().toLowerCase();

        Assert.assertTrue(
                "El título del carrito no es correcto",
                texto.contains("carrito")
                        || texto.contains("cart")
        );

        evidencia();
    }

    @Entonces("valido el cálculo de precios en el carrito")
    public void validarPreciosCarrito() {

        WebElement total = visible(By.cssSelector(
                ".cart-summary-line.cart-total .value"
        ));

        BigDecimal totalObtenido = precio(
                total.getText()
        );

        System.out.println(
                "Total esperado carrito: " + totalEsperado
        );

        System.out.println(
                "Total obtenido carrito: " + totalObtenido
        );

        evidencia();

        Assert.assertEquals(
                "El total del carrito no coincide",
                0,
                totalEsperado.compareTo(totalObtenido)
        );
    }

    // ========================================
    // CP02 - LOGIN INVALIDO
    // ========================================

    @Cuando("inicio sesión con credenciales inválidas")
    public void loginInvalido() {

        iniciarSesion(
                "usuario_invalido@example.com",
                "ClaveIncorrecta123!"
        );
    }


    @Entonces("verifico que no se permite el acceso")
    public void verificarLoginInvalido() {

        WebElement error = visible(
                By.cssSelector(".alert-danger")
        );

        Assert.assertTrue(
                "No se mostró el mensaje de autenticación rechazada",
                error.isDisplayed()
        );

        evidencia();
    }


    // ========================================
    // CP03 - CATEGORIA INEXISTENTE
    // ========================================

    @Cuando("intento navegar a la categoría Autos")
    public void categoriaAutos() {

        driver().get(URL);

        List<WebElement> categorias =
                driver().findElements(
                        By.xpath(
                                "//a[normalize-space()='Autos']"
                        )
                );

        evidencia();

        Assert.assertTrue(
                "La categoría Autos no debería existir",
                categorias.isEmpty()
        );
    }

    @Entonces("la automatización detecta que la categoría no existe")
    public void validarCategoriaInexistente() {
        // El escenario se detiene en el paso anterior.
    }
}
