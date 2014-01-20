#ManPer

Aplicación Android de gestión de lugares personales.
Un “lugar” es un elemento que representa un sitio físico,
está geolocalizado, tiene nombre e incluye otros datos adicionales. Con esta
aplicación el usuario podrá crear, modificar y eliminar sus lugares, además de
consultarlos en una lista y en un mapa.
El conjunto de lugares se almacenará en una base de datos. Los campos deben
ser los siguientes:
	* _id: número entero.
	* nombre: cadena de texto.
	* descripción: cadena de texto.
	* latitud: número real (float).
	* longitud: número real (float).
	* foto: cadena de texto.
La latitud y longitud son las coordenadas del lugar. El campo “foto” es una URI
de Android que referencia a un fichero de imagen guardado en alguna carpeta.

Las actividades de la aplicación serán las siguientes:

* PrincipalActivity
  -----------------
Actividad principal, se lanza al iniciar la aplicación. Contiene dos botones
(“lista” y “mapa”) para lanzar ListaLugaresActivity y MapaLugaresActivity.

* ListaLugaresActivity
  --------------------
Presenta una lista de todos los lugares guardados en la base de datos.
Para cada uno se muestra su nombre y descripción. Al pulsar en uno
de ellos se lanza MostrarLugarActivity para verlo en detalle.

* MapaLugaresActivity
  -------------------
Muestra un mapa de Google Maps con marcadores que señalan los lugares
del usuario. Al pulsar en un marcador se abrirá MostrarLugarActivity
para verlo en detalle. Al pulsar en cualquier otro punto del mapa se
lanzará EditarLugarActivity para crear un nuevo lugar en las coordenadas
elegidas.

* MostrarLugarActivity
  --------------------
Muestra todos los datos de un lugar: nombre, descripción y foto. La foto
debe visualizarse en una vista a partir de la URI asociada al lugar (el
campo “foto” de la BD). Debe haber un botón “Editar” que abrirá EditarLugarActivity
para modificar el lugar en cuestión.

* EditarLugarActivity
  -------------------
Presenta los datos del lugar (nombre y descripción) en cuadros de texto
editables. La foto también debe ser visible y clickable. Al pulsar la foto
se lanzará una actividad propia de Android para seleccionar una imagen
de entre las que haya en el teléfono (fotos tomadas con la cámara,
etc.). Al volver de esa actividad, la URI de la imagen seleccionada será
la nueva foto asociada al lugar.
Esta actividad sirve tanto para editar un lugar existente como para crear
uno nuevo. En caso de editar, habrá un botón “Guardar” que actualizará
el lugar en la BD con los nuevos valores introducidos por el usuario
(nombre, descripción y URI de la foto). También habrá otro botón “Eliminar”
que borrará el lugar de la BD. En el caso de estar crear un lugar
nuevo, en vez de los botones “Guardar” y “Eliminar” habrá un botón
“Crear” que insertará el nuevo lugar en la BD con los datos introducidos.
El diseño gráfico se deja a voluntad del alumno. Se valorará un diseño atractivo,
funcional y técnicamente sofisticado. Otros detalles que mejoren la aplicación
(menús de opciones, menús contextuales, optimizaciones...) también serán valorados,
aunque no son requeridos. En cualquier caso, el código fuente deberá
estar debidamente comentado para facilitar su comprensión.


###MEJORAS

Algunas de las mejoras que le he añadido:
- Menu: donde nos dara la opcion:
	+ Seleccionar Idioma: Español o Inglés.
	+ About: donde veremos información sobre la aplicación.
	+ Salir: donde terminarmos con la aplicación.
- Mejora en diseño añadiendo librerias:
  + ActionBar: esta librería es una librería desarrollada para aquellos dispositivos donde aun no se le añadio esta mejora. Debido en la versión que está desarrollada (Android 2.2) la función actionbar no está disponible, por lo que he buscado una librería que realizara la función actionbar lo más sencilla posible, encontrando esta que implemento de muy sencillo funcionamiento.
  + Validator: otro problema que me he encontrado, es a la hora de validar los campos edittext, no existe función sencilla o eficiente(o al menos que yo haya encontrado) donde poder validar los campos para que no se inserte valores no deseados. Para ello he utilizado esta libreria compuesta por los siguientes archivos:
    * AbstractValidator.java
    * RegExpressionValidator.java
    * ValidationResult.java
    * Validator.java
