# Laboratorio 1: APLICACIONES DISTRIBUIDAS (HTTP, SOCKETS, HTML, JS,MAVEN, GIT)

Una aplicación para consultar la información de películas de cine. La aplicación recibe una frase de búsqueda del título, por ejemplo “Guardians of the galaxy” y deberá mostrar el los datos de la película correspondiente.

Utiliza el API gratuito de [omdbapi](https://www.omdbapi.com/). La implementación es eficiente en cuanto a recursos así que implementa un Caché que permite evitar hacer consultas repetidas al API externo.

## Empezando:

Estas instrucciones te van a permitir obtener una copia del proyecto y ejecutarlo en tu máquina.

### Requisitos:
* Control de versiones: https://git-scm.com/
* Manejador de dependencias: https://maven.apache.org/
* Java: https://www.oracle.com/java/technologies/downloads/#java15

> [!IMPORTANT]
> Es necesario tener instalado Git, Maven y Java 20 (JDK 20) o Java 15 (JDK 15) para poder ejecutar el proyecto.
> NOTA: Este proyecto fue realizado en NETBEANS, se recomienda poner en las propiedades de NETBEANS JDK 15.

### Instalación
    1. Clonar este respositorio en su computadora:
      - ```bash
git clone https://github.com/FDanielMC/AREP_LAB-1.git
cd AREP_LAB-1.git/

```
    2. Abra el proyecto desde NETBEANS.
    3. Luego diríjase al directorio src en el cual se encontrará las carpetas para que pueda ejecutar el programa.
    4. Arranca el Main.java.
    5. Abra el navegador mozilla firefox: 
        5.1. Consultar la pagina de la siguiente manera:  http://localhost:35000/

### JAVADOC
Se encuentra en AREP_LAB-1\AREPLab1\target\site\apidocs.

