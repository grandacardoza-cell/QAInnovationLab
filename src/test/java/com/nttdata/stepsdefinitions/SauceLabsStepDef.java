package com.nttdata.stepsdefinitions;

import com.nttdata.steps.AppConfigSteps;
import com.nttdata.steps.SuaceLabsLoginSteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class SauceLabsStepDef {


    private final SuaceLabsLoginSteps login =
            new SuaceLabsLoginSteps();

    @When("ingreso el usuario {string}")
    public void ingresoElUsuario(String usuario) {
        System.out.println("ingresoElUsuario - stepsDef");
        login.ingresoElUsuario(usuario);
    }



    @When("inicio sesión con mi usuario {string} y clave {string}")
    public void inicioSesiónConMiUsuarioYClave(String arg0, String arg1) {
        login.ingresoElUsuario(arg0);
        login.ingresarClave(arg1);
        login.ingresar();

    }



    @And("ingreso la clave {string}")
    public void ingresoLaClave(String arg0) {
        //login.ingresarClave(arg0);
    }

    @And("hago clic en LOGIN")
    public void hagoClicEn() {
        //login.ingresar();
    }

    @And("valido el login OK")
    public void validoElLoginOK() {
        //login.validacionLogin();
    }
}
