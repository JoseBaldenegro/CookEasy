### Descripción General
El proyecto CookEasy tiene como objetivo el desarrollo de una aplicación móvil para Android que permita a los usuarios consultar, seguir y compartir recetas de cocina de forma práctica e interactiva.

La aplicación mostrará una variedad de recetas clasificadas por categorías (postres, sopas, etc.), con ingredientes, pasos detallados y videos de apoyo.
Además, permitirá al usuario tomar fotografías de sus platillos terminados o seleccionar una imagen desde la galería, y compartir sus resultados en redes sociales directamente desde la app.

El sistema estará desarrollado en Java utilizando Android Studio y almacenará la información de las recetas, ingredientes y pasos en una base de datos local SQLite.
La interfaz se adaptará automáticamente para teléfonos y tabletas, aprovechando el tamaño de pantalla disponible y manteniendo su funcionalidad tanto en orientación vertical como horizontal.

### Objetivos Específicos
* Desarrollar una aplicación Android nativa utilizando Java y Android Studio.
* Implementar al menos dos actividades principales:
  * Actividad 1: Lista o catálogo de recetas.
  * Actividad 2: Detalle de la receta seleccionada (ingredientes, pasos, video, foto y opciones para compartir).
* Diseñar una interfaz adaptable a diferentes dispositivos (teléfonos y tabletas) mediante el uso de ConstraintLayout y Fragments.
* Mantener el estado de la aplicación ante cambios de orientación o interrupciones utilizando onSaveInstanceState() y/o ViewModel.
* Persistir los datos en una base de datos SQLite, incluyendo recetas, ingredientes, pasos y fotos del usuario.
* Utilizar una característica del dispositivo móvil, como la cámara o la galería de fotos, para capturar o seleccionar imágenes del resultado final.
* Integrar comunicación externa, permitiendo compartir contenido (foto y nombre de la receta) en redes sociales mediante Intents de tipo ACTION_SEND.
* Incorporar videos de apoyo vinculados a cada receta.

### Documentación y Demostración

Aquí puedes encontrar la documentación completa del proyecto y una demostración de la aplicación en funcionamiento:

* [Manual de Usuario](./docs/Manual%20de%20Usuario%20CookEasy.pdf)
* [Diseño del proyecto](./docs/Diseño%20de%20proyecto.pdf)
* [Propuesta de Proyecto](./docs/Propuesta%20Proyecto%20Móviles%20CookEasy.pdf)

**Video de demostración:**
* [Haz clic aquí para ver el funcionamiento de CookEasy](./docs/CookEasy%20Aplicaciones%20Móviles.mp4)
