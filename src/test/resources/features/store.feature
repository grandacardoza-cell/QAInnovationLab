
# language: es
@teststore

Característica: Product - Store

  Escenario: CP01 - Validación del precio de un producto
    Dado estoy en la página de la tienda
    Cuando inicio sesión con credenciales válidas
    Y navego a la categoría Clothes y subcategoría Men
    Y agrego 2 unidades del primer producto al carrito
    Entonces valido la confirmación y el precio del producto agregado
    Cuando finalizo la compra desde la confirmación
    Entonces valido el título de la página del carrito
    Y valido el cálculo de precios en el carrito

  Escenario: CP02 - Autenticación inválida
    Dado estoy en la página de la tienda
    Cuando inicio sesión con credenciales inválidas
    Entonces verifico que no se permite el acceso

  Escenario: CP03 - Categoría inexistente
    Dado estoy en la página de la tienda
    Cuando intento navegar a la categoría Autos
    Entonces la automatización detecta que la categoría no existe
