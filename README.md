# Bizi Zaragoza

Se trata de un Trabajo de Fin de Grado desarrollado en el seno del grupo de investigación IAAA. Su objetivo es la implementación de un proceso de extractión, tratamiento y explotación de datos desde un portal web que no facilita una API de su servicio. (Para más detalles, consultar [Términos de referencia](TerminosDeReferencia.md) o la memoria del trabajo).

<!-- TOC START min:2 max:4 link:true update:true -->
- [Toma de contacto con el proyecto](#toma-de-contacto-con-el-proyecto)
  - [Tecnologías](#tecnologas)
  - [Organización de los contenedores Docker](#organizacin-de-los-contenedores-docker)
- [Proceso de desarrollo y desplegado](#proceso-de-desarrollo-y-desplegado)
  - [Desplegado el Docker Registry](#desplegado-el-docker-registry)
  - [Poblado el Docker Registry](#poblado-el-docker-registry)
  - [Puesta en marcha del sistema](#puesta-en-marcha-del-sistema)
- [Estructura y funcionamiento del sistema](#estructura-y-funcionamiento-del-sistema)
  - [Clases importantes](#clases-importantes)
  - [Funcionamiento interno](#funcionamiento-interno)
- [Aspectos importantes](#aspectos-importantes)
  - [Configuración avanzada de la aplicación](#configuracin-avanzada-de-la-aplicacin)
  - [Problemas con Sqoop](#problemas-con-sqoop)
- [Tareas pendientes](#tareas-pendientes)

<!-- TOC END -->


## Toma de contacto con el proyecto

La tarea que nos ataña ahora es la introducción de los conceptos clave del proyecto, imprescindibles para su fácil entendimiento y rápida incorporación al trabajo.

### Tecnologías

Es importante conocer a fondo las tecnologías utilizadas debido a que será necesario saber manejarse con fluidez con ellas a la hora de resolver problemas complejos.

Como consejo, si no se entiende el funcionamiento de alguno de los pasos seguidos en estas instrucción, convendría consultar la documentación oficial permitente.

Las tecnologías utilizadas son:

* **Docker:** Es un programa que aprovecha la tecnología de contenedores de Linux llamada LXC ofreciendo entornos de ejecución ligeros y autocontenidos basados en imágenes de sistema. Existe una gran comunidad a su alrededor que desarrolla y ofrece sus propias imágenes con todo tipo de software pre-configurado.
* **Docker Compose:** Es un programa que orquesta ejecuciones de varios contenedores Docker simultáneamente basándose en su fichero de configuración `docker-compose.yml`. Su existencia se debe a que rara vez se necesita un sólo contenedor, sino que, por le contrario, el camino que ha tomado Docker es el de emular sistemas multi-nodo.

* **Spring Boot:** Es una librería perteneciente al framework de desarrollo de aplicaciones java Spring que ayuda a la configuración y puesta en marcha de una aplicación con Spring. sus puntos fuertes son las configuraciones que ofrece por defecto, permitiendo al desarrollador ignorar los detalles que no le atañan en un primer momento.

* **JHipster:** Es un programa de "inicio de proyectos". Al ejecutarse, muestra una serie de preguntas en la terminal sobre cómo es la aplicación que desea crearse. Al final de ese proceso JHipster creará todos los ficheros del proyecto, atendiendo a las mejores prácticas de ingeniería y desarrollo, para resultar en una aplicación lista para producción.

* **Hadoop, HDFS y Hive:** Hadoop es un framework de big data para el procesamiento de grandes cantidades de datos de forma distribuída, originalmente basado en su motor MapReduce. HDFS es su sistema de ficheros distribuído. Hive es un programa que, en base a ficheros almacenados en HDFS, contruye vistas del contenido de los mismos en forma de tablas relacionales. Eso permite hacer consultas en SQL sobre esas tablas y obtener resultados de los datos de esos ficheros. Lo que estará sucendiendo por debajo, es que Hive traducirá esas consultas a rutinas MapReduce y las lanzará en el cluster de Hadoop.

* **Sqoop:** Es un programa que básicamente transfiere datos de HDFS a una base de datos relacional tradicional como puede ser MySQL.


### Organización de los contenedores Docker

Podemos dividir la arquitectura del sistema en tres áreas:

* **Hadoop Cluster:** En éste área, encontramos los contenedores `namenode` y `datanode` que contienen los servicios replicables de Hadoop y los contenedores `hive-server`, `hive-metastore` y `hive-metastore-postgresql` que contienen los servicios de Hive y se comunican con el resto de servicios de Hadoop.

* **Aplicación:** En éste área se encuentra `biziapp`, el contenedor dodne está montada la aplicación, compilada con perfil de producción y configurada para funcionar dentro del sistema; y `mysql`, la base de datos que requiere la aplicación para funcionar.

* **Monitoreo:** En éste área se incluyen las herramientas de monitoreo de serivicos como `portainer`, la cual es tremendamente útil para visualizar el estado de los contenedores, sus logs en tiempo real, etc; y `adminer`, que ofrece una interfaz web de gestión para la base de datos MySQL.

Los sistemas dentro de Docker están comunicados entre sí mediante una red virtual que comparten. Sin embargo, la comunicación con el `host` está limitada a los puertos que se expongan explícitamente en el fichero de configuración `docker-compose.yml`.

Los puertos por cada contenedor pueden ser consultados en dicho fichero, pero los más habituales son:

* BiziApp Web Interface: [puerto 8080](http://localhost:8080)
* Adminer Web Interface: [puerto 8081](http://localhost:88081)
* Portainer Web Interface: [puerto 9000](http://localhost:9000)

## Proceso de desarrollo y desplegado

El entorno de ejecución y despliegue de la aplicación se encuentra encapsulado en el directorio `./biziapp_production`. Ahí es donde se localiza el mencionado fichero `docker-compose.yml` y el resto de directorios necesarios. Es aconsejable mantener éste directorio aislado y no trasladarlo a la raíz del proyecto si no se va a ser estrictamente riguroso con la estructura de ficheros ya que de lo contrario, corremos el riesgo de confundir que elementos son para desarrollo y cuales para producción.

### Desplegado el Docker Registry

La imágenes que Docker, en general, utiliza para crear los contenedores pueden ser:

* Fruto de la compilación de una imagen mediante `docker build .`
* Descargadas de un Docker Registry.

Las imágenes que descargamos cuando hacemos `docker run <nombre de la imagen>` provienen por defecto del Docker Registry oficial de Docker llamado [Docker Hub](https://hub.docker.com), pero podemos utilizar otro siempre que tengamos acceso a él.

Es una buena práctica acostumbrarse a utilizar un Docker Registry local para simular un entorno de producción profesional en el que, en la intranet de la empresa habría un Registry común para todos los trabajadores.

Para desplegar un Docker Registry ejecutamos:

```bash
docker run -d -p 5000:5000 --restart=always --name registry registry:2
```
A partir de ahora, podremos _subir_ las imágenes que hemos creado con DockerBuild a nuestro Registry etiquetando la imagen con `localhost:5000/<nombre de la imagen>:<tag>` y haciendo `docker push`.

_Más información en [documentación oficial](https://docs.docker.com/registry/deploying/)._

### Poblado el Docker Registry

Ahora que tenemos el registro ejecutándose en nuestra máquina, debemos compilar la imagen de `biziapp` y la de `hive-server`, pero antes de eso, es necesario configurar `biziapp` con una serie de datos que no están fijados, bien sea por su sensibilidad o por su dependencia del equipo en el que se desarrolla.

En este proceso de desplegado en Docker, al estar el entorno controlado en contenedores, tan sólo es necesario fijar las credenciales de acceso al portal configurando el fichero `./biziapp_production/biziapp.env`:

```bash
APPLICATION_LEGACYSYSTEM_CREDENCIALES=<user>:<pass>
```
_Nota: Si se quisieran seguir los pasos del punto [Configuración avanzada de la aplicación](#configuracin-avanzada-de-la-aplicacin) habría que configurarlos **antes** de compilar `biziapp`._

Ahora que tenemos el registro ejecutándose en nuestra máquina, podemos compilar la imagen de `biziapp` y la de `hive-server` ejecutando desde el directorio raíz del proyecto el script `./push.sh`.

Éste script ejecuta el comando de Gradle para:
1. La compilación de la aplicación
2. La creación de las imágenes
3. El etiquetado de las imágenes
4. La subida de las imágenes al registro local

Siguiendo éste paso ya tendremos las imágenes necesarias para poner en marcha el sistema.

### Puesta en marcha del sistema

Entendiendo entonces que estamos en dicho directorio, podemos ejecutar el sistema entero, con aplicaciones de monitoreo incluídas, lanzando el comando:

```bash
docker-compose up -d
```
o sólo el sistema, sin aplicaciones de monitoreo, con:

```bash
docker-compose up -d biziapp
```

La explicación de por qué esto funciona así es que, en la configuración de Docker Compose, hemos establecido una red de dependencias entre los distintos contenedores siendo `biziapp` la raíz de todos ellos. Esto es debido a que, al ser la aplicaicón final, depende de forma transitiva de todos los elementos.

Así pues, Docker Compose no puede lanzar un contenedor sin antes ejecutar aquellos de los que depende. Los contenedores de monitoreo son los únicos de los cuales no depende nadie y por tanto éstos serán ignorados.

_Nota: Obsérvese que algunas imágenes referenciadas pertenecen al registro local. Por ello es estrictamente necesario que se hayan seguido los pasos de los puntos [Desplegado el Docker Registry](#desplegado-el-docker-registry) y [Poblado el Docker Registry](#poblado-el-docker-registry)._

## Estructura y funcionamiento del sistema

El sistema en ejecución implementa en su aplicación principal `biziapp` un servicio con varios procesos periódicos así como los servicios necesarios `hive-server`, `mysql`, etc.

### Clases importantes

Cada uno de esos procesos periódicos se ha denominado `tarea` y se encuentran en el paquete `es.unizar.iaaa.biziapp.tareas` junto con prácticamente todo el código implementado (aquel que no ha sido autogenerado por JHipster). Las `tareas` son controladas desde la clase `Gestor` donde se configura su periodicidad.

Además éstas `tareas` pueden ser activadas cuando uno lo desee accediendo a los _endpoint_ REST habilitados en `es.unizar.iaaa.biziapp.web.rest.ControlExterno`. De este modo se puede depurar el comportamiento sin tener que esperar a que la `tarea` se vuelva a ejecutar.

### Funcionamiento interno

Si bien el orden de ejecución de las `tareas` no es determinista, existe un workflow que describe el flujo de la información dentro de la aplicación, y es el siguiente:

1. El sistema se inicia, la aplicación se autoconfigura y establece las conexiones con MySQL, la base de datos donde almacenará toda la información persistente dle proceso.

1. La `tarea` `GenerardorFechas` se ejecuta debido a su programación temporal.
  1. Obtiene la fecha del día anterior al actual.

  1. Escribe una entrada nueva en la tabla de la base de datos de la siguiente
  `tarea`, `DescargarFichero` en estado de `WAITING`.

1. La `tarea` `DescargarFichero` se ejecuta debido a su programación temporal.
  1. Lee de la base de datos MySQL las entradas pendientes asignadas: las que están `WAITING`.

  1. Ejecuta Selenium para acceder al portal, navegar por él y descargar los datos   en el directorio adecuado.

  1. Escribe una entrada nueva en la tabla de la base de datos de la siguiente `tarea`, `TratamientoFicheros` en estado de `WAITING` y marca su propia entrada como `FINISHED`.

1. La `tarea` `TratamientoFicheros` se ejecuta debido a su programación temporal.
  1. Lee de la base de datos MySQL las entradas pendientes asignadas: las que están `WAITING`.

  1. Lee el fichero descargado en el paso anterior, que esá indicado en la información de su entrada.

  1. Lo procesa y convierte a formato CSV.

  1. Escribe una entrada nueva en la tabla de la base de datos de la siguiente `tarea`, `InsertarHadoop` en estado de `WAITING` y marca su propia entrada como `FINISHED`.

1. La `tarea` `InsertarHadoop` se ejecuta debido a su programación temporal.
  1. Lee de la base de datos MySQL las entradas pendientes asignadas: las que están `WAITING`.

  1. Copia el fichero creado en el paso anterior, que esá indicado en la información de su entrada, al directorio compartido con el contenedor de `hive-server`.

  1. Crea una tabla temporal en Hive a través de HiveJDBC.

  1. Carga los datos del fichero a la tabla a través de una orden a Hive mediante HiveJDBC.

  1. Mueve las entradas de la tabla temporal a la tabla definitiva a través de una orden a Hive mediante HiveJDBC.

  1. Escribe una entrada nueva en la tabla de la base de datos de la siguiente `tarea`, `Insertar` en estado de `WAITING` y marca su propia entrada como `FINISHED`.

## Aspectos importantes

### Configuración avanzada de la aplicación

La aplicación está entéramente embebida en Docker, pero puede ser interesante para el desarrollador hacer un despliegue _híbrido_:
* Desplegando sólo la infraestructura (`hadoop/hive` y `mysql`).
* Corriendo una versión en local de la aplicación en el `host`.

_**WARNING:** Algunas de las funcionalidades no están soportadas en este modo debido a cómo se ha creado el docker-compose. Concretamente, el volcado de datos a Hive no se puede realizar debido a que la carpeta `/compartida` es un volumen no accesible desde fuera de Docker. Al tratarse de una feature fruto de un mal diseño y considerarse DEPRECATED, no se ha diseñado un workarround para solventarla, sino que la solución pasa por arreglar ese comportamiento mal diseñado y utilizar HDFS desde un principio (como se detalla en [Tareas pendientes](#tareas-pendientes))._

De ese modo se evita el largo proceso de compilado y actualización del registry. Para ello ha de entender cómo funciona la configuración de la aplicación.

Spring Framework ofrece una utilidad para acceder a configuraciones propias que es el uso de las `@ConfigurationProperties`. Su funcionamiento está detallado en su [documentación oficial](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) donde se muestra el orden de proporidad de las propiedades.

Básicamente lo que ofrece es, por ejemplo, una forma de mapear ficheros de configuración como `./src/main/resources/config/application.yml` a clases dentro del entorno de ejecución de Java como `es.unizar.iaaa.biziapp.config.ApplicationProperties` donde tendremos precisamente las configuraciones específicas para nuestra aplicación.

Los valores de esos atributos se pueden definir, no sólo mediante ficheros, sino también a través de variables de entorno que son mapeadas a ellos. Las variables de entorno y sus respectivas configuraciones serían:

Variable de entorno  | Propiedad  | Descripción  
--|---|--
APPLICATION_LEGACYSYSTEM_CREDENCIALES  |  application.legacysystem_credenciales |  Credenciales de acceso al portal
APPLICATION_LEGACYSYSTEM_BASE_URL  |  application.legacysystem_base_url |  URL del portal
APPLICATION_PATH_CHROME_DRIVER  |  application.path_chrome_driver |  Path del fichero chrome driver (local)
APPLICATION_PATH_DOWNLOAD  |  application.path_download |  Directorio absoluto donde descargar del portal
APPLICATION_PATH_CSV  |  application.path_csv |  Directorio absoluto donde depositar los ficheros procesados
APPLICATION_PATH_DOCKER_SHARED  |  application.path_docker_shared |  Directorio absoluto donde depositar los ficheros a insertar en hadoop
APPLICATION_HIVE_URL  | application.hive_url  |  Dirección URL del servidor hive
APPLICATION_HIVE_CREDENCIALES  | application.hive_credenciales  |  Credenciales de acceso a Hive
APPLICATION_HIVE_JDBC  | application.hive_jdbc  |  JDBC de Hive
APPLICATION_MYSQL_URL  | application.mysql_url  |  Dirección URL del servidor MySQL
SPRING_DATASOURCE_URL  | application.  |  Dirección URL del servidor MySQL (redundante debido a mal diseño de la aplicación)
APPLICATION_MYSQL_CREDENCIALES  | application.mysql_credenciales  |  Credenciales de acceso a MySQL
APPLICATION_MYSQL_JDBC  | application.mysql_jdbc  |  JDBC de MySQL
APPLICATION_DOCKER  | application.docker  |  

Entendiendo entonces cómo se modifican estos parámetros, es necesario fijar varios de ellos si se quiere optar por este despliegue _híbrido_. Los atributos **necesarios** son:
* **application.legacysystem_credenciales:** Las credenciales de acceso al portal
* **application.path_chrome_driver:** Un directorio local donde se encuentre el ChromeDriver instalado (Es necesario además tener Chrome o Chromium)
* **application.path_download:** Un directorio local donde el sistema almacenará los datos descargados
* **application.path_csv:** Un directorio local donde el sistema almacenará los csv convertidos
* **application.path_docker_shared:** DEPRECATED debido a la explicación del principio de este punto.

El segundo requisito, como se ha mencionado, es tener instalado Chromium y haber descargado el Google Chrome Driver en un directorio controlado. (Puede ser la raíz dle proyecto).


### Problemas con Sqoop

El último paso de Sqoop no está automatizado y además da errores con la configuración actual.

La idea original era que periódicamente, Sqoop volcara los datos de Hive a MySQL, pero la manera en la que se ha afrontado no es la idónea. No obstante, el _workarround_ para realizar dicho paso sería acceder al contenedor `hive-server` mediante `docker exec -it hive-server bash` lo que abrirá una terminal en el contenedor y desde ahí ejecutar el comando `/home/dump.sh` que es un fichero bindeado al fichero `./biziapp_production/sqoop/dump.sh`. Las posibles modificaciones a realizar se harían en el fichero y los cambios estarían disponibles en el contenedor  sin necesidad de relanzarlo.

## Tareas pendientes

* Reingeniería de toda la aplicación

* Aprovechamiento de las herramientas de Spring para no reimplementar la rueda como SpringData

* Uso de un patrón de _Task Management_ con colas de tareas y notificaciones basado, por ejemplo, en Redis o en RabbitMQ que permita reaccionar a las tareas mediante _callbacks_ en lugar de comprobar periódicamente MySQL

* Implementación de tests que comprueben configuraciones y aspectos susceptibles de fallar

* Actualización del Cluster de Hadoop a una versión más moderna

* Utilización de HDFS como directorio de trabajo para todas las estapas del procesamiento en lugar de directorios locales

* Inserción de datos en Hive a través de HDFS, creando una `EXTERNAL TABLE` vinculada un directorio y transfiriendo el nuevo documento CSV a ese directorio directamente.

# Referencias
* [JHipster](JHipster.md)
