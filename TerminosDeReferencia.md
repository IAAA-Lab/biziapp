# BiziZGZ

## Propósito de este documento

* Indicar la motivación y los objetivos del proyecto.
* Describir en líneas generales el alcance del proyecto.
* Indicar cualquier dependencia y limitación ya conocida.
* Identificar los recursos requeridos para la siguiente fase del TFG.
* Justificar y asegurar el apoyo para llevar a cabo la siguiente fase del TFG.

## Aprobación

| Persona | Rol | Fecha |
|---------|-----|-------|
| Francisco J. Lopez-Pellicer | Director | 2017-01-23

## Motivación

Este proyecto se motiva por el interés en crear una infraestructura para recolectar, almacenar, procesar, integrar, enriquecer espacialmente y explotar para diversos usos datos sobobre el servicio Bizi Zaragoza.
  
## Objetivos

Al finalizar el proyecto hemos de tener una solución que:

* Ayuda a crear una base de datos histórica de los datos de Bizi Zaragoza publicados en http://reportingportal-zar.clearchannel.com/ con la granularidad más fina posible. Esta base de datos histórica puede originarse de más de 10000 ficheros diferentes.
* Ayuda a recuperar y almacenar datos nuevos de Bizi Zaragoza que se publiquen en dicho portal. 
* Permite conocer como mínimo la ocupación (bicis disponibles) y el tráfico (retiradas y a dónde van, devoluciones y de dónde vienen) de cada estación por hora.
* Enriquece la información extraída de Bizi Zaragoza con información de referencia, como es la localización de las estaciones Bici Zaragoza.
* Publica mediante un API usable por un analista datos sobre Bizi Zaragoza para su uso por otras aplicaciones, ya sean Web o de escritorio.
* Utiliza en un caso práctico, como sería el cálculo del volumen de tráfico de bicicletas por tramo vial y hora, los datos extraídos, como ejemplo de lo que se puede hacer con la base de datos Bizi Zaragoza.

## Ámbito

La solución **DEBE** ser capaz de:

* Recuperar y almacenar a largo plazo los datos más relevantes ofrecidos en http://reportingportal-zar.clearchannel.com/ para cada estación y con granularidad de una hora (si está disponible) en el formato estructurado (si está disponible) original.
* Recuperar todos los datos históricos disponibles en http://reportingportal-zar.clearchannel.com/.
* Recuperar cada dia todos los datos nuevos disponibles en http://reportingportal-zar.clearchannel.com/.
* Gestionar la recuperación de los datos mediante un sistema que permita ver el estado de la recuperación y tomar decisiones para recuperar de forma manual, automática y periódica los datos.
* Procesar todos los datos para ser expuestos mediante un modelo de datos relacional que permita realizar preguntas SQL analíticas.
* Exponer el modelo mediante un API REST que permita hacer preguntas SQL analíticas.
* Tener un portal que permita navegar los datos, recuperar subconjuntos de información y visualizar en formato tabular y espacial información sobre uso de Bizi Zaragoza.

La solución **IDEALMENTE** debe ser capaz de:

* Enriquecer el modelo con información espacial sobre las estaciones Bizi para permitir la realización de preguntas SQL espaciales.
* Exponer el modelo mediante un API REST que permita hacer preguntas SQL espaciales y que devuelva las respuestas en formatos utilizables tanto por aplicaciones Web (GeoJSON) como de escritorio (ESRI SHP).
* Utilizar un rutómetro y una red vial para estimar intensidades de trafico horarias y exponerlas mediante un API REST que permita hacer preguntas SQL analíticas y espaciales.
* Tener un portal que visualice en formato tabular y espacial información de trafico derivada del uso de Bizi Zaragoza.

## Restricciones del proyecto

* El proyecto sólo recuperará información de http://reportingportal-zar.clearchannel.com/ que sea relevante para el proyecto.

## Beneficios

* Incremento de la relación con GEOT al demostrar nuestra competencia para crear sistemas que simplifican el acceso a grandes volúmenes de datos. 
* Oportunidad para que GeoSLab ofrezca una solución a Movilidad Urbana utilizando los datos extraídos.
* 1 TFG.
* 1 o 2 contribuciones a congresos (JIIDE, AGILE) explicando las características de la solución

## Hipótesis y dependencias

* La recolección, almacenamiento y acceso de datos se basará en el paradigma *Data Lake* utilizando *Hadoop* para el almacenamiento masivo de datos y *Hive* para el acceso de la información mediante SQL.
* En caso de calcular la información de tráfico derivada se asumirá que los cliclistas usarán preferentemente la red ciclista, los carriles bicis y las zonas 30 para calcular la ruta más corta en dos estaciones Bizi.

## Riesgos del proyecto

* *Acceso vía formularios*: Los datos de Bizi Zaragoza tienen que extraerse de un portal vía formularios protegidos por usuario y contraseña. Existe un riesgo bajo de cambio del portal durante el proyecto. Este riesgo aumentará con el tiempo una vez finalizado el proyecto.
* *Formatos no pensados para máquinas*: Los datos de Bizi Zaragoza están disponibles en formatos orientados a su consulta via web, usando un lector de PDF o una aplicación capaz de trabajar con ficheros Excel o CSV. Los datos descargados tienen que ser adaptados para su posterior consumo. 
* *Inconsistencia de información*: Se ha detectado que hay inconsistencias en la información recuperada al comparar los resultados de la misma pregunta en diferentes formatos. En el momento actual no podemos estar seguros que toda la información proporcionada es consistente entre sí. 
* *Cuenta pensada para analista*: La cuenta que tenemos para acceder a los datos está pensada para un analista. Existe un riesgo bajo de que Clear Channel cancele este usuario y contraseña por considerar que se está haciendo un uso incorrecto de la cuenta por utilizarla para realizar descargas sistemáticas de información.  
* *Inestabilidad del sistema*: El portal de Bizi Zaragoza presenta algunos problemas de estabilidad al acceder a la información. Por ejemplo, el 2017-01-18 estuvo parcialmente caido durante varias horas sin que ninguna información sobre los motivos del problema estuvieran disponibles.

## Recursos e instalaciones necesarias

* Un alumno (Daniel Cabrera).
* Un puesto de trabajo libre en el L2.09.

## Plazos y costes

* El plazo de finalización de este proyecto es junio 2017.
* El proyecto debe poderse realizar por un alumno durante un plazo de 4 meses.
* Los recursos inicialmente comprometidos para este proyecto son un alumno durante 350 horas y un director durante 35 horas.

## Implicados

###Participantes clave (alta influencia, alto interés):

| Nombre | Rol | Contacto |
| ------ | --- | -------- |
| [GEOT](http://iuca.unizar.es/es/grupo-de-investigacion/grupo-de-estudios-de-ordenacion-del-territorio-geot) | Cliente<br>Usuario | [Ángel Pueyo Campos](mailto:senoufou@gmail.com) (Cliente)<br>[Aldo Arranz López](mailto:arranz@unizar.es) (Usuario)
| [IAAA](http://iaaa.unizar.es) | Dirección | Francisco J Lopez-Pellicer

**GEOT**: GEOT es un Grupo de Investigación de UNIZAR. Sus proyectos están ligados a la ordenación y análisis territorial, social y sanitario, las infraestructuras de transportes, los equipamientos públicos, y a los problemas de insustentabilidad y de segregación social, económica y ambiental asociados a los modelos recientes de la expansión de las ciudades. Necesitan de los datos de Bizi Zaragoza para realizar estudios de movilidad urbana para el **Servicio de Movilidad Urbana (Ayuntamiento de Zaragoza)**.

**IAAA**: IAAA es un Grupo de Investigación de UNIZAR interesado en Sistemas de Información avanzados relacionados con la información geográfica.

###Importantes (alta influencia, bajo interés)

| Nombre | Rol | Contacto |
| ------ | --- | -------- |
| [Clear Channel España](https://www.clearchannel.es/) | Proveedor |  

**Clear Channel España** es la empresa concesionaria del servicio Bizi Zaragoza y proveedora de los datos. Se clasifica por ahora como importante ya que no tienen conocimiento o interés en el proyecto pero con sus decisiones pueden influir en el futuro del proyecto.

###Afectados (baja influencia, alto interés)

| Nombre | Rol | Contacto |
| ------ | --- | -------- |
| [GeoSLab](http://www.geoslab.com/es/home) | Usuario | María José Perez 

**GeoSLab** es una PYME Interesada en desarrollar una solución para el **Servicio de Movilidad Urbana (Ayuntamiento de Zaragoza)** de información de cálculo de rutas multimodales. Están interesados en los datos de BiziZGZ y pueden estar interesados en combinarlos con datos de AZUSA. No pueden afectar al proyecto pero están interesados en sus resultados.

###Otros (baja influencia, bajo interés)

| Nombre | Rol | Contacto |
| ------ | --- | -------- |
| Servicio de Movilidad Urbana  | Beneficiado | José Antonio Chanca

El **Servicio de Movilidad Urbana (Ayuntamiento de Zaragoza)** puede considerarse un beneficiario indirecto del proyecto a través tanto via GEOT como GeoSLAB. 
