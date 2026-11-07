package com.nttdata.stepsdefinitions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.Before;
import com.nttdata.steps.AppConfigSteps;
import io.cucumber.java.Scenario;

public class AppConfigStepsDefs {

    private final AppConfigSteps appConfigSteps =
            new AppConfigSteps();

    private Scenario scenario;


    @Before
    public void configurarScenario(Scenario scenario) {

        this.scenario = scenario;
    }
    
    
    @Given("ingreso al aplicativo de AppConfig")
    public void ingreso_al_aplicativo_de_app_config() {
        appConfigSteps.IniciarAplicacion();
        //appConfigSteps.ValidarPantallaInicial();
        tomarScreenshot("Pantalla inicial");
    }
    
    @Then("busco el texto {string}")
    public void busco_el_texto(String texto) {
        appConfigSteps.BuscarTexto(texto);
        tomarScreenshot(
                "Busqueda realizada - " + texto
        );

        appConfigSteps.CerrarAplicacion();
    }

    //@AfterStep
    public void tomarScreenshot(String nombre) {

        byte[] screenshot =
                appConfigSteps.TomarScreenshot();

        if (screenshot != null &&
                screenshot.length > 0) {

            scenario.attach(
                    screenshot,
                    "image/png",
                    nombre
            );
        }
    }
}
