package com.nttdata.steps;
import com.nttdata.screens.AppConfigScreen;

public class AppConfigSteps {
    private final AppConfigScreen appConfigScreen =
            new AppConfigScreen();
    
    public void IniciarAplicacion() {
        appConfigScreen.iniciarAplicacion();
    }
    public void ValidarPantallaInicial() {
        appConfigScreen.ValidarPantallaInicial();
    }

    public void BuscarTexto(String texto) {
        appConfigScreen.BuscarTexto(texto);
    }

    public void CerrarAplicacion() {
           appConfigScreen.CerrarAplicacion();
    }

   public byte[] TomarScreenshot() {

    return appConfigScreen.TomarScreenshot();
    }
    
}
