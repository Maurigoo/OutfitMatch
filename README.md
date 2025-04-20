# OutfitMatch

**OutfitMatch** es una aplicación móvil diseñada para ayudarte a organizar tu guardarropa y crear outfits automáticamente. Permite a los usuarios subir fotos de sus prendas, clasificarlas en categorías (camisas, pantalones, zapatos), y generar combinaciones de ropa de forma automática. Los usuarios pueden explorar estas combinaciones, guardarlas como favoritas y personalizar su experiencia con un sistema de inicio de sesión y registro.

---

## Descripción del Proyecto

**OutfitMatch** es una aplicación móvil que busca transformar la forma en que las personas gestionan su vestuario y combinan sus prendas. Con esta app, el usuario podrá subir fotos de diferentes prendas de ropa y clasificarlas en categorías específicas como camisas (Shirts), pantalones (Pants) y zapatos (Shoes). 

Una vez que el usuario haya subido sus prendas, **OutfitMatch** genera outfits automáticos combinando las diferentes prendas de manera creativa, ofreciendo al usuario ideas frescas y prácticas para su día a día. Las combinaciones de ropa se muestran en un formato visual interactivo, similar al estilo de *Tinder*, donde el usuario puede deslizar (swipe) para indicar si le gusta o no el conjunto propuesto. Si un outfit es del gusto del usuario, este puede guardarlo en su sección de favoritos para poder revisarlo más tarde.

Además, la aplicación incluye un sistema de inicio de sesión y registro, permitiendo que cada usuario tenga su propio perfil y pueda gestionar su ropa y sus outfits de manera personalizada.

Este proyecto fue desarrollado por un equipo de estudiantes con el objetivo de crear una herramienta útil y divertida que ayude a las personas a tomar decisiones rápidas sobre su vestimenta, a la vez que organiza y clasifica su guardarropa de forma sencilla.

El equipo de desarrollo está compuesto por:

- **Maroua Ezzaki Kadmiri**
- **Avril Pino Pérez**
- **Jose Mauricio Herbas**

---

## Características principales

- **Gestión de prendas**: 
  - Sube fotos de tus prendas de ropa.
  - Organiza las prendas en categorías como *Shirt*, *Pant* y *Shoe*.

- **Generación automática de outfits**:
  - La aplicación crea combinaciones únicas de ropa (outfits) basadas en las prendas que has subido.
  - Explora las combinaciones generadas en un formato interactivo estilo *Tinder*, donde puedes deslizar (swipe) o guardar las que más te gusten.

- **Sección de favoritos**:
  - Guarda tus outfits favoritos para futuras referencias en la sección exclusiva de outfits.

- **Sistema de inicio de sesión y registro**:
  - Crea una cuenta para personalizar tu experiencia.
  - Inicia sesión de manera segura para acceder a tus prendas y outfits.

## Tecnologías utilizadas

**OutfitMatch** se desarrolló utilizando una combinación de tecnologías modernas y herramientas que permiten una experiencia de usuario fluida y dinámica. Las tecnologías principales utilizadas son:

- **Lenguaje de programación**: Java
- **Entorno de desarrollo**: Android Studio
- **Base de datos**: Firebase para almacenamiento y autenticación
- **Interfaz de usuario**: XML para la creación de los layouts
- **Dependencias**:
  - Firebase Authentication para el sistema de inicio de sesión y registro.
  - Glide para la carga eficiente de imágenes.
  - RecyclerView para mostrar los outfits y las prendas.

## Instalación

Sigue estos pasos para instalar y ejecutar **OutfitMatch** en tu dispositivo Android:

1. Clona este repositorio a tu máquina local:
   ```bash
   git clone https://github.com/marouaEzzaki/OutfitMatch.git

2. Abre el proyecto en Android Studio.
3. Asegúrate de tener las dependencias necesarias descargadas y configuradas (como Firebase).
4. Ejecuta la aplicación en un emulador o dispositivo Android físico.

## Cómo usar la aplicación

1. **Iniciar sesión o registrarse**: 
   - Si es tu primera vez, regístrate creando una cuenta con tu correo electrónico.
   - Si ya tienes una cuenta, solo ingresa tus credenciales.
   - Una vez que hayas iniciado sesión, no necesitarás volver a introducir tus credenciales en futuras sesiones, ya que la aplicación mantiene tu sesión activa. 
   - También tienes la opción de cerrar sesión en cualquier momento desde el menú de navegación.

2. **Subir tus prendas**: 
   - Toma fotos de tus prendas y súbelas a la aplicación.
   - Clasifica las prendas según su categoría (camisa, pantalón, zapato).

3. **Ver tus prendas subidas**: 
   - En la sección de **Clothes**, podrás ver todas las prendas que has subido. Las prendas estarán organizadas por categoría (camisas, pantalones, zapatos), lo que facilita la visualización y selección.

4. **Generar outfits**: 
   - La aplicación generará automáticamente combinaciones de ropa a partir de las prendas que hayas subido.
   - Las combinaciones se muestran en un formato visual estilo *Tinder*. Puedes deslizar (swipe) para indicar si te gusta o no el conjunto propuesto.

5. **Guardar tus outfits favoritos**: 
   - Si te gusta una combinación, desliza hacia la derecha (swipe right). Esto guardará automáticamente el outfit en la sección de *Outfits favoritos*.

6. **Navegar por la aplicación**: 
   - La aplicación tiene un menú de navegación donde puedes acceder fácilmente a diferentes secciones como tus outfits guardados, tus prendas subidas, tu perfil, y otras opciones importantes.

## Contribuciones

Si deseas contribuir a **OutfitMatch**, por favor sigue estos pasos:

1. Haz un *fork* del repositorio [OutfitMatch](https://github.com/marouaEzzaki/OutfitMatch).
2. Crea una nueva rama (`git checkout -b feature/nueva-funcionalidad`).
3. Realiza tus cambios y haz un *commit* (`git commit -am 'Añadir nueva funcionalidad'`).
4. Sube tu rama (`git push origin feature/nueva-funcionalidad`).
5. Crea un *pull request*.

Asegúrate de que tus contribuciones sigan las mejores prácticas de desarrollo y documentación.


