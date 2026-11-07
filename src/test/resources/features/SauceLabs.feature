
Feature: Carrito de compras Sauce Labs

  @Carrito
  @PruebaCarrito
  Scenario Outline: Agregar productos al carrito
    Given estoy en la aplicación de SauceLabs
    And valido que carguen correctamente los productos en la galería
    When agrego <UNIDADES> del producto "<PRODUCTO>"
    Then valido que el carrito actualice correctamente

    Examples:
      | PRODUCTO              | UNIDADES |
      | Sauce Labs Backpack   | 1        |
      | Sauce Labs Bolt T-Shirt | 1      |
      | Sauce Labs Bike Light | 2        |
