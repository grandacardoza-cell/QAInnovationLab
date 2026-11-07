
package com.nttdata.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.options.UiAutomator2Options;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AppConfigScreen {

    private static AndroidDriver driver;

    private static final String APPIUM_URL =
            "http://127.0.0.1:4723/";


    private static final Duration TIMEOUT =
            Duration.ofSeconds(20);

    private static final String APP_PACKAGE =
            "com.saucelabs.mydemoapp.android";


    private final By toolbar =
            AppiumBy.id("com.wt.apkinfo:id/toolbar");

    private final By cajaBusqueda =
            AppiumBy.id("com.wt.apkinfo:id/searchEdit");

    public void iniciarAplicacion() {

        try {
            System.out.println("Iniciando My Demo App...");

            UiAutomator2Options options = new UiAutomator2Options();

            options.setPlatformName("Android");
            options.setAutomationName("UiAutomator2");
            options.setDeviceName("Android Emulator");
            options.setUdid("emulator-5554");

            options.setAppPackage(APP_PACKAGE);
            options.setAppActivity(
                    "com.saucelabs.mydemoapp.android.view.activities.SplashActivity"
            );
            options.setAppWaitActivity("*");
            options.setAppWaitDuration(Duration.ofSeconds(30));
            //options.setAppActivity("com.saucelabs.mydemoapp.android.view.activities.MainActivity");
            options.setNoReset(false);
            options.setAutoGrantPermissions(false);
            options.setCapability("appium:skipDeviceInitialization", false);
            options.setCapability("appium:skipServerInstallation", false);
            options.setCapability("appium:skipServerInstallation", false);
            options.setCapability("appium:ignoreHiddenApiPolicyError", true);
            options.setCapability(
                    "appium:forceAppLaunch", true
            );

            options.setCapability(
                    "appium:settings[waitForIdleTimeout]", 1000
            );

            options.setCapability(
                    "appium:disableWindowAnimation", true
            );

            options.setCapability(
                    "appium:skipLogcatCapture", true
            );

            options.setNewCommandTimeout(
                    Duration.ofSeconds(300)
            );

            driver = new AndroidDriver(
                    new URL(APPIUM_URL),
                    options
            );

            System.out.println(
                    "Sesion Appium creada: " +
                            driver.getSessionId()
            );

        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "URL de Appium invalida", e
            );
        }
    }

    public static AndroidDriver getDriver() {
        return driver;
    }

    public void ValidarPantallaInicial() {

        WebDriverWait wait =
                new WebDriverWait(driver, TIMEOUT);

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        toolbar
                )
        );
    }

    public void BuscarTexto(String texto) {

        WebDriverWait wait =
                new WebDriverWait(driver, TIMEOUT);

        WebElement elementoToolbar =
                wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                toolbar
                        )
                );

        elementoToolbar.click();

        WebElement inputBusqueda =
                wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                cajaBusqueda
                        )
                );

        inputBusqueda.click();
        inputBusqueda.sendKeys(texto);

        driver.pressKey(
                new KeyEvent(AndroidKey.ENTER)
        );
    }

    public void CerrarAplicacion() {

        if (driver != null) {

            try {
                System.out.println(
                        "Cerrando Swag Labs..."
                );

                driver.executeScript(
                        "mobile: terminateApp",
                        java.util.Map.of(
                                "appId", APP_PACKAGE
                        )
                );

            } finally {

                System.out.println(
                        "Cerrando sesion Appium..."
                );

                driver.quit();
                driver = null;

                System.out.println(
                        "Sesion Appium cerrada"
                );
            }
        }
    }

    public byte[] TomarScreenshot() {

        if (driver == null) {
            return new byte[0];
        }

        return driver.getScreenshotAs(
                OutputType.BYTES
        );
    }
}
