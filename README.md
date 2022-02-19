# 10000

[Diseño - Hecho en Figma](https://www.figma.com/embed?embed_host=notion&url=https%3A%2F%2Fwww.figma.com%2Ffile%2FD2uOFrnuDvuWTXrRZiXvpX%2F10000%3Fnode-id%3D0%253A1)

## Normas

### **Objetivo**

El objetivo del juego es llegar a los 10000 puntos exactos.

### **El juego**

El jugador tira 6 dados.

Si no obtiene puntos pierde el turno.

Si obtiene puntos, puede decidir entre:

1. Volver a tirar los dados que no le hayan dado puntos.
2. Pasar el turno al siguiente jugador.

Si al volver a tirar no obtiene puntos, pierde los puntos que había obtenido en esta ronda y pierde el turno.

En el caso contrario, si aún le quedan dados que no le han dado puntos, puede decidir volver a tirar y así sucesivamente hasta que no le queden dados o saque 0 puntos.

### **Puntuaciones**

1 vale 100 puntos

5 vale 50 puntos

3 x 1 = 1000  | 4 x 1 = 2000 | 5 x 1 = 4000 | 6 x 1 = 8000

3 x 2 = 200    | 4 x 2 = 400   | 5 x 2 = 800   | 6 x 2 =1600

3 x 3 = 300    | 4 x 3= 600    | 5 x 3 = 1200 | 6 x 3= 2400

3 x 4 = 400    | 4 x 4= 800    | 5 x 4 = 1600 | 6 x 4= 3200

3 x 5 = 500    | 4 x 5 = 1000 | 5 x 5 = 2000 | 6 x 5= 4000

3 x 6 = 600    | 4 x 6 = 1200 | 5 x 6 = 2400 | 6 x 6 = 4800

un tiro de escalera (1 + 2+ 3 + 4 +5 +6) = 1500

### **El juego termina cuando**

1. Un jugador gana (obtiene 10000 puntos exactos).
2. Todos los jugadores pierden (todos los jugadores se pasan de 10000 puntos).

---

# Interfaz

![Partes de la interfaz](img/Untitled%201.png)

① Botón para tirar los dados. (*)

② Visor de puntos de tirada.

③ Puntos totales del jugador actual.

④ Puntos acumulados de la ronda 1 del jugador actual.

⑤ Numero de la ronda.

⑥ Puntos acumulados de la ronda y jugador actuales.

⑦ Nombre del jugador actual.

⑧ Tabla completa de puntuaciones. (**)

⑨ Botón para mostrar/ocultar la tabla.

⑩ Botón para activar/desactivar la música. (***)

⑪ Imagen con las combinaciones y sus puntos.

*A su lado aparece el botón skip, pero solo cuando el jugador puede pasar el turno.

![Botón de skip](img/Untitled%202.png)

**La tabla de puntuaciones no baja automáticamente, hay que utilizar el scroll.

***Aún no está implementado.

---

*NOTA: Los dados desaparecen cuando cambiamos de jugador, porque si no, se vería la tirada anterior.*

Cuando un jugador pierde, es decir, se pasa de 10000 puntos,  salta una alerta informando al jugador que ha perdido y en la tabla de puntuaciones, aparece sombreado.

![Perder](img/Untitled%203.png)

![Perder2](img/Untitled%204.png)

Cuando todos los jugadores pierden, salta esta alerta y al hacer clic en aceptar, el juego se cierra.

![Todos pierden](img/Untitled%205.png)

Cuando un jugador consigue 10000 puntos exactos gana, y salta esta alerta. Cuando hacemos clic en aceptar, el juego se cierra.

![Ganar](img/Untitled%206.png)