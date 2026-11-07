
package com.nttdata.steps;

import com.nttdata.screens.AppConfigSauceLabsScreen;
import org.junit.Assert;

public class SuaceLabsLoginSteps {

    private final AppConfigSauceLabsScreen login =
            new AppConfigSauceLabsScreen();

    public void ingresoElUsuario(String usuario) {
        System.out.println("Ingresando usuario");
        login.escribirUsuario(usuario);
    }

    public void ingresarClave(String clave) {
        login.ingresarClave(clave);
    }

    public void ingresar() {
        login.ingresar();
    }

    public void validacionLogin() {
        Assert.assertTrue(
                "No se mostró la pantalla de productos",
                login.validarLogin()
        );
    }
}
