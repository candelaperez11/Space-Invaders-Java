# Space-Invaders-Java
Implementación del videojuego Space Invaders en Java desarrollada como proyecto académico para la asignatura Tecnología de la Programación I, aplicando principios de programación orientada a objetos y lógica de juego.

Este repositorio contiene la implementación del videojuego Space Invaders desarrollada en Java como proyecto académico para la asignatura Tecnología de la Programación I. El proyecto fue realizado por Ana de Miguel Prieto y Candela Muñoz Pérez. 

Durante su desarrollo se aplicamos principios de programación orientada a objetos (POO) para la estructuración del código y la gestión de las entidades del juego. Se implementó la lógica principal incluyendo control de estados, movimiento de enemigos, disparos y detección de colisiones, así como el uso de estructuras de datos y modularización para una arquitectura clara y mantenible.

El proyecto obtuvo una calificación de 9/10 en la evaluación final de la asignatura.

## Cómo Ejecutarlo 
  1. Abre el proyecto en tu IDE (por ejemplo, IntelliJ o Eclipse).
  2. Ejecuta la clase **`Main.java`**:
     - En el IDE: `Run As...` → **Java Application**
  3. Una vez iniciado, el juego se controla por consola mediante comandos.

### Comandos disponibles
Escribe `help` o `h` para ver la lista de comandos:

- `[l]oad <fileName>`: carga la configuración del juego desde el archivo `<fileName>`
- `[s]ave <fileName>`: guarda la configuración actual en el archivo `<fileName>`
- `[a]dd[O]bject <object_description>`: añade un objeto al tablero  
  `<object_description> = (col,row) objName [dir [BIG|SMALL]]`  
  Ejemplo: `(12,3) Mario LEFT SMALL`
- `[a]ction [[R]IGHT | [L]EFT | [U]P | [D]OWN | [S]TOP]+`: realiza acciones del usuario
- `[u]pdate` o `""`: no se realiza ninguna acción
- `[r]eset [numLevel]`: reinicia el juego (si se indica `numLevel`, carga ese nivel)
- `[h]elp`: muestra esta ayuda
- `[e]xit`: salir del juego

### Configuración de inicio (opcional)
Puedes iniciar el juego con un nivel o una configuración específica desde la configuración de ejecución del IDE:  
- **Nivel inicial (argumentos):** `-1`, `0`, `1` o `2` (niveles implementados actualmente).  
- **Archivo de configuración:** en `Run Configurations` → `Common` → `InputFile`, selecciona el archivo de configuración con el que quieres comenzar. Debe tener la estructura de los archivos contenidos en la carpeta Configuraciones. 
