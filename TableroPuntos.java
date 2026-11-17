
import java.util.Scanner;

public class TableroPuntos {

    // Dimensiones del tablero
    static final int FILAS = 10;
    static final int COLUMNAS = 20;

    // Caracteres
    static final char PUNTO = '.';
    static final char JUGADOR = 'P';
    static final char TRAMPA = 'X';
    static final char ENEMIGO = 'E';
    static final char PORTAL = 'O';
    static final char ESCUDO = 'S';

    // Matriz del tablero
    static char[][] tablero = new char[FILAS][COLUMNAS];

    // Posicion del jugador
    static int filaJugador = 0;
    static int colJugador = 0;

    // Variables del juego
    static int vidas = 3;
    static int escudos = 0;
    static int puntuacion = 0;

    // Cantidad de elementos
    static final int CANTIDAD_TRAMPAS = 15;
    static final int CANTIDAD_ENEMIGOS = 15;
    static final int CANTIDAD_PORTALES = 2;
    static final int CANTIDAD_ESCUDOS = 3;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        inicializarTablero();
        colocarElementos();

        boolean salir = false;

        while (!salir && vidas > 0) {

            mostrarTablero();

            System.out.println("-------------------------");
            System.out.println("VIDAS: " + vidas);
            System.out.println("ESCUDOS: " + escudos);
            System.out.println("PUNTUACION: " + puntuacion);
            System.out.println("-------------------------");

            if (contarTrampasRestantes() == 0 &&
                contarEnemigosRestantes() == 0) {

                System.out.println("¡FELICIDADES! HAS SOBREVIVIDO");
                break;
            }

            System.out.println("Mover: W S A D | Q (salir)");
            System.out.print("Opcion: ");

            String opcion = sc.nextLine().trim().toLowerCase();

            switch (opcion) {

                case "w":
                    mover(-1, 0);
                    break;

                case "s":
                    mover(1, 0);
                    break;

                case "a":
                    mover(0, -1);
                    break;

                case "d":
                    mover(0, 1);
                    break;

                case "q":
                    salir = true;
                    break;

                default:
                    System.out.println("Movimiento no valido");
            }
        }

        if (vidas <= 0) {
            System.out.println("GAME OVER. Te has quedado sin vidas");
            mostrarTableroFinal();
        }

        System.out.println("Puntuacion final: " + puntuacion);
        System.out.println("Fin del juego.");

        sc.close();
    }

    // Inicializa el tablero
    static void inicializarTablero() {

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tablero[i][j] = PUNTO;
            }
        }

        filaJugador = FILAS / 2;
        colJugador = COLUMNAS / 2;

        tablero[filaJugador][colJugador] = JUGADOR;
    }

    // Coloca todos los elementos
    static void colocarElementos() {

        colocarObjeto(TRAMPA, CANTIDAD_TRAMPAS);
        colocarObjeto(ENEMIGO, CANTIDAD_ENEMIGOS);
        colocarObjeto(PORTAL, CANTIDAD_PORTALES);
        colocarObjeto(ESCUDO, CANTIDAD_ESCUDOS);
    }

    // Coloca objetos aleatoriamente
    static void colocarObjeto(char objeto, int cantidad) {

        int colocados = 0;

        while (colocados < cantidad) {

            int f = (int)(Math.random() * FILAS);
            int c = (int)(Math.random() * COLUMNAS);

            if (tablero[f][c] == PUNTO) {

                tablero[f][c] = objeto;
                colocados++;
            }
        }
    }

    // Movimiento del jugador
    static void mover(int df, int dc) {

        int nuevaFila = filaJugador + df;
        int nuevaColumna = colJugador + dc;

        // Comprueba los limites
        if (nuevaFila < 0 || nuevaFila >= FILAS ||
            nuevaColumna < 0 || nuevaColumna >= COLUMNAS) {

            System.out.println("No puedes salir del tablero");
            return;
        }

        char casilla = tablero[nuevaFila][nuevaColumna];

        // Borra la posicion anterior
        tablero[filaJugador][colJugador] = PUNTO;

        // Actualiza la posicion
        filaJugador = nuevaFila;
        colJugador = nuevaColumna;

        switch (casilla) {

            case TRAMPA:
                System.out.println("¡HAS CAIDO EN UNA TRAMPA!");
                recibirDanio();
                puntuacion += 5;
                break;

            case ENEMIGO:
                System.out.println("¡TE HAS ENCONTRADO CON UN ENEMIGO!");
                recibirDanio();
                puntuacion += 10;
                break;

            case ESCUDO:
                recogerEscudo();
                break;

            case PORTAL:
                usarPortal();
                break;

            case PUNTO:
                puntuacion++;
                break;
        }

        // Coloca al jugador en su nueva posicion
        tablero[filaJugador][colJugador] = JUGADOR;
    }

    // Recoger un escudo
    static void recogerEscudo() {

        escudos++;

        System.out.println("¡HAS CONSEGUIDO UN ESCUDO!");
        System.out.println("Escudos disponibles: " + escudos);
    }

    // Recibir daño
    static void recibirDanio() {

        if (escudos > 0) {

            escudos--;

            System.out.println("¡EL ESCUDO HA BLOQUEADO EL DAÑO!");

        } else {

            vidas--;

            System.out.println("Has perdido una vida.");
            System.out.println("Vidas restantes: " + vidas);
        }
    }

    // Usar portal
    static void usarPortal() {

        System.out.println("¡HAS ENCONTRADO UN PORTAL!");

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {

                if (tablero[i][j] == PORTAL) {

                    // Evita volver a la misma posicion
                    if (i != filaJugador || j != colJugador) {

                        filaJugador = i;
                        colJugador = j;

                        System.out.println("¡TE HAS TELETRANSPORTADO!");
                        return;
                    }
                }
            }
        }
    }

    // Contar trampas restantes
    static int contarTrampasRestantes() {

        int contador = 0;

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {

                if (tablero[i][j] == TRAMPA) {
                    contador++;
                }
            }
        }

        return contador;
    }

    // Contar enemigos restantes
    static int contarEnemigosRestantes() {

        int contador = 0;

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {

                if (tablero[i][j] == ENEMIGO) {
                    contador++;
                }
            }
        }

        return contador;
    }

    // Mostrar tablero
    static void mostrarTablero() {

        System.out.println();

        for (int i = 0; i < FILAS; i++) {

            for (int j = 0; j < COLUMNAS; j++) {

                char casilla = tablero[i][j];

                // Ocultar trampas
                if (casilla == TRAMPA) {
                    System.out.print(". ");
                } else {
                    System.out.print(casilla + " ");
                }
            }

            System.out.println();
        }
    }

    // Mostrar tablero final
    static void mostrarTableroFinal() {

        System.out.println("\nTABLERO FINAL:");

        for (int i = 0; i < FILAS; i++) {

            for (int j = 0; j < COLUMNAS; j++) {

                System.out.print(tablero[i][j] + " ");
            }

            System.out.println();
        }
    }
}
