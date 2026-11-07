/*package com.nttdata.steps.old;

import com.nttdata.screens.old.LoginScreen;
import com.nttdata.screens.old.SearchDetailsScreen;
import com.nttdata.screens.old.SearchScreen;

import net.serenitybdd.annotations.Step;

public class AirbnbSearchSteps {

    LoginScreen loginScreen;
    SearchScreen searchScreen;
    SearchDetailsScreen searchDetailsScreen;

    @Step("Click en cerrar")
    public void clickClose(){
        loginScreen.clickClose();
    }

    @Step("Búsqueda por texto {0}")
    public void searchByText(String place){
        searchScreen.clickSearchInput();
        searchDetailsScreen.enterSearchInput(place);
        searchDetailsScreen.clickFirstOption();
        searchDetailsScreen.clickNext();
        searchDetailsScreen.clickSearch();
    }

    @Step("Obtiene el texto del resultado")
    public String getResultText(){
        return searchScreen.getResultText();
    }
}
*/