package de.jeanpierrehotz.conwaysgame.game;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Diese Klasse bildet ein Spielfeld des Conways game of life, welches auch
 * Informationen zu den einzelnen Feldern speichert
 */
public class ConwaysGame {

    /* Konstanten */

    /**
     * Diese Konstante gibt an, wie groß der (unsichtbare) Rand zwischen dem Rand des Fensters
     * (und damit dem sichtbaren Teil des Feldes) und dem Rand des Spielfeldes (und damit
     * dem unsichtbaren Teil des Feldes).
     * Diese sind in Feldteilen angegeben.
     */
    private static int OFFSET = 100;



    /* primitive Variablen */

    /**
     * Dieses Array enthält die einzelnen Zellen des Spiels, wobei folgende Beziehungen bestehen:
     *  true  -> lebendig
     *  false -> tot
     */
    private boolean[][] field;
    /**
     * Diese Variable gibt an, wie breit / hoch eine Zelle gemalt werden muss (in px)
     */
    private int size;
    /**
     * Dieses Array kann die Zahlen in dem Intervall [0 .. 8] je höchstens einmal enthalten,
     * und beschreibt, wie viele Nachbarn eine bereits lebende Zelle benötigt, um zu überleben.
     */
    private int[] survivorQualifications;
    /**
     * Dieses Array kann die Zahlen in dem Intervall [0 .. 8] je höchstens einmal enthalten,
     * und beschreibt, wie viele Nachbarn eine tote Zelle benötigt, um geboren zu werden.
     */
    private int[] birthQualifications;



    /* Konstruktoren */

    /**
     * Dieser Konstruktor erzeugt ein Conways Game of life-Objekt mit den normalen Regeln
     * (2 oder 3 Nachbarn zum Überleben, 3 Nachbarn zum geboren werden), einer Zufallsrate von 0,25 und
     * den gegebenen Werten für die Größe des Spielfelds.
     * @param x         Breite des Fensters, in dem das Spiel angezeigt wird
     * @param y         Höhe des Fensters, in dem das Spiel angezeigt wird
     * @param w         gewünschte Breite des Spielfelds
     * @param h         gewünschte Breite des Spielfeldes
     * @see ConwaysGame#ConwaysGame(int, int, int, int, double)
     * @see ConwaysGame#ConwaysGame(int, int, int, int, double, int[], int[])
     */
    public ConwaysGame(int x, int y, int w, int h){
        this(x, y, w, h, 0.25);
    }

    /**
     * Dieser Konstruktor erzeugt ein Conways Game of life-Objekt mit den normalen Regeln
     * (2 oder 3 Nachbarn zum Überleben, 3 Nachbarn zum geboren werden) und den gegebenen Werten für
     * die Zufallsrate und die Größe des Spielfelds.
     * @param x         Breite des Fensters, in dem das Spiel angezeigt wird
     * @param y         Höhe des Fensters, in dem das Spiel angezeigt wird
     * @param w         gewünschte Breite des Spielfelds
     * @param h         gewünschte Breite des Spielfeldes
     * @param ratio     die Zufallsrate mit der der Initialzustand der Zellen ermittelt wird
     * @see ConwaysGame#ConwaysGame(int, int, int, int, double, int[], int[])
     */
    public ConwaysGame(int x, int y, int w, int h, double ratio){
        this(x, y, w, h, ratio, new int[]{2, 3}, new int[]{3});
    }

    /**
     * Dieser Konstruktor erzeugt ein Conways Game of life-Objekt mit den gegebenen Regeln, der gegebenen
     * Zufallsrate und der gegebenen Größe des Spielfelds.
     * Dabei ist zu beachten, dass zu der Größe des wirklichen Spielfelds 2 * {@link ConwaysGame#OFFSET}
     * (je in horizontaler und vertikaler Richtung) hinzugefügt wird, damit nicht sichtbare Vorgänge nicht
     * wegfallen gelassen werden.
     * Des weiteren wird das sichtbare Spielfeld derart berechnet, dass die Zellen komplett als Quadrate angezeigt werden
     * (sofern nicht abgeschnitten), wobei das größere Feld übernommen wird.
     * (-> Bei einer gew. Breite = 800 und gew. Höhe = 8 und einer Fenstergröße von 1600 x 800px wird die Höhe des
     *  sichtbaren Spielfelds auf 400 Zellen angepasst, damit deren Höhe ebenfalls 2px beträgt.)
     * @param x         Breite des Fensters, in dem das Spiel angezeigt wird
     * @param y         Höhe des Fensters, in dem das Spiel angezeigt wird
     * @param w         gewünschte Breite des Spielfelds
     * @param h         gewünschte Breite des Spielfeldes
     * @param ratio     die Zufallsrate mit der der Initialzustand der Zellen ermittelt wird
     * @param survivor  die Liste der Anzahlen der Nachbarn, die zum Überleben benötigt werden
     * @param birth     die Liste der Anzahlen der Nachbarn, die zum geboren werden benötigt werden
     */
    public ConwaysGame(int x, int y, int w, int h, double ratio, int[] survivor, int[] birth){
        /*
        Zuerst die Größe des Feldes errechnen, indem wir die kleinere Breite einer Zelle nehmen,
        und diese auf die Dimensionen des Fensters aufteilen
         */
        int realW = x / w;
        int realH = y / h;

        if(realW < realH){
            field = new boolean[(x / realW) + (2 * OFFSET)][(y / realW) + (2 * OFFSET)];
            size = realW;
        }else{
            field = new boolean[(x / realH) + (2 * OFFSET)][(y / realH) + (2 * OFFSET)];
            size = realH;
        }

        /*
        Die Zellen mithilfe der Zufallsrate auf true oder false setzen
         */

        for(int i = 0; i < field.length; i++){
            for(int j = 0; j < field[i].length; j++){
                field[i][j] = Math.random() > ratio;
            }
        }

        /*
        Und dem Spiel die Regeln zuweisen
         */

        setSurvivorQualifications(survivor);
        setBirthQualifications(birth);
    }



    /* Setter und Getter */

    /**
     * Diese Methode setzt alle Zellen des Spielfeldes auf den gegebenen Wert. Dabei gilt:
     *  true -> lebendig
     *  false -> tot
     * @param stat  Der Zustand auf den das gesamte Feld gesetzt werden soll.
     */
    public void setAll(boolean stat){
        for(int i = 0; i < field.length; i++){
            for(int j = 0; j < field[i].length; j++){
                field[i][j] = stat;
            }
        }
    }

    /**
     * Diese Methode setzt die Zelle an den gegebenen Koordinaten auf den gegebenen Wert.
     * Dabei ist zu beachten, dass eine Zelle mehrere Koordinaten beinhalten kann, und dass folgendes gilt:
     *  true -> lebendig
     *  false -> tot
     * @param x         Die x-Koordinate der Zelle
     * @param y         Die y-Koordinate der Zelle
     * @param stat      Der gewünschte Status der Zelle
     */
    public void setFieldAtTo(int x, int y, boolean stat){
//      Index berechnen:
        int xInd = OFFSET + (x / size);
        int yInd = OFFSET + (y / size);

//      Zelle an dem Index auf den gegebenen Wert setzen:
        field[xInd][yInd] = stat;
    }

    /**
     * Diese Methode setzt die Regeln zum Überleben auf die gegebene Liste, wobei zu beachten ist, dass
     * die Zahlen im Intervall [0 .. 8] liegen müssen, und keine Zahl doppelt vorkommen soll.
     * @param survivor      Die Lsite mit Vorgaben
     */
    public void setSurvivorQualifications(int[] survivor){
        ArrayList<Integer> temp = new ArrayList<>();

//      Alle Elemente kopieren, und Elemente außerhalb von [0 .. 8] und doppelte Vorkommnisse herausfiltern
        for(int i = 0; i < survivor.length; i++){
            if(survivor[i] >= 0 && survivor[i] <= 8 && !temp.contains(survivor[i])){
                temp.add(survivor[i]);
            }
        }

//      Die Elemente sortieren
        Collections.sort(temp);

//      und die sortierten und gefilterten Elemente in das Array kopieren
        survivorQualifications = new int[temp.size()];

        for(int i = 0; i < temp.size(); i++){
            survivorQualifications[i] = temp.get(i);
        }
    }

    /**
     * Diese Methode setzt die Regeln zum Geboren werden auf die gegebene Liste, wobei zu beachten ist, dass
     * die Zahlen im Intervall [0 .. 8] liegen müssen, und keine Zahl doppelt vorkommen soll.
     * @param birth         Die Lsite mit Vorgaben
     */
    public void setBirthQualifications(int[] birth){
        ArrayList<Integer> temp = new ArrayList<>();

//      Alle Elemente kopieren, und Elemente außerhalb von [0 .. 8] und doppelte Vorkommnisse herausfiltern
        for(int i = 0; i < birth.length; i++){
            if(birth[i] >= 0 && birth[i] <= 8 && !temp.contains(birth[i])){
                temp.add(birth[i]);
            }
        }

//      Die Elemente sortieren
        Collections.sort(temp);

//      und die sortierten und gefilterten Elemente in das Array kopieren
        birthQualifications = new int[temp.size()];

        for(int i = 0; i < temp.size(); i++){
            birthQualifications[i] = temp.get(i);
        }
    }



    /* öffentliche Methoden, um das Spiel zu modifizieren */

    /**
     * Diese Methode lässt das Spiel die neue Generation erstellen.
     * @param infinite      ob das Spiel unendlich sein soll
     */
    public void aktualisiereFeld(boolean infinite){
        field = neuesFeld(infinite);
    }

    /**
     * Diese Methode zeichnet die derzeitige Generation des Spiels
     * @param drawGrid  ob das Gitter gemalt werden soll
     * @param x         die Breite des Fensters
     * @param y         die Höhe des Fensters
     * @param g         das Graphics-Objekt mit dem gemalt werden soll
     */
    public void drawGame(boolean drawGrid, int x, int y, Graphics g){
//      Zuerst derzeitige Farbe zwischenspeichern
        Color tempCol = g.getColor();

//      Den Bildschirm löschen
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, x, y);

//      Falls nötig das Gitternetz in Grau malen
        if(drawGrid) {
            g.setColor(Color.GRAY);

            for (int i = 0; i <= field.length - (2 * OFFSET); i++)
                g.drawLine(i * size, 0, i * size, y);

            for (int i = 0; i <= field[0].length - 2 * (OFFSET); i++)
                g.drawLine(0, i * size, x, i * size);
        }

//      alle Felder in schwarz malen
        g.setColor(Color.BLACK);

        for(int i = OFFSET; i < field.length - OFFSET; i++)
            for(int j = OFFSET; j < field[i].length - OFFSET; j++)
                if(field[i][j])
                    g.fillRect((i - OFFSET) * size, (j - OFFSET) * size, size, size);

//      vorherige Farbe wieder herstellen
        g.setColor(tempCol);
    }



    /* private Methoden, um das Spiel zu modifizieren */

    /**
     * Diese Methode ermittelt die neue Generation, und gibt diese zurück
     * @param infinite      ob das Spielfeld unendlich sein soll
     * @return              die neue Generation des Spielfeldes
     */
    private boolean[][] neuesFeld(boolean infinite){
//      Die neue Generation ist genauso groß wie die derzeitige Generation
        boolean[][] newField = new boolean[field.length][field[0].length];

//      dann gehen wir durch alle Felder
        for(int i = 0; i < field.length; i++){
            for(int j = 0; j < field[i].length; j++){
//              ermitteln wie viele lebende Nachbarm dieses Feld hat
                int living = getLivingNeighbors(i, j, infinite);

//              und ermitteln ob es lebendig sein soll, indem wir zuerst ermitteln welche Regeln angewendet werden sollen,
//              und anhand dieser Entscheidung dann entweder die Überleben-Regeln oder die Geboren werden-Regeln anwenden:
                newField[i][j] = ( field[i][j] && livingCellSurvives(living) ) || ( !field[i][j] && deadCellBorn(living) );
            }
        }

//      Die neue Generation wird zuletzt zurückgegeben
        return newField;
    }

    /**
     * Diese Methode ermittelt, ob eine lebendige Zelle mit der gegebenen Anzahl an lebenden Nachbarn überleben soll
     * @param ctr       Die Anzahl an lebenden Nachbarn
     * @return          ob diese Zelle lebendig sein soll
     */
    private boolean livingCellSurvives(int ctr){
//      Wir gehen durch alle Überleben-Regeln
        for(int i = 0; i < survivorQualifications.length; i++){
//          falls die Regel am Index i und die gegebene Anzahl lebendiger Nachbarn gleich sind wird true zurückgegeben
            if(survivorQualifications[i] == ctr){
                return true;
//          falls diese größer als die gegebene Anzahl lebendiger Nachbarn ist wird false zurückgegeben, da die Regeln sortiert sind, weshalb
//          ab dem ersten Element, das größer ist ALLE Elemente größer sein werden.
            }else if(survivorQualifications[i] > ctr){
                return false;
            }
        }

//      Falls nichts in der Schleife zutrifft wird false zurückgegeben
        return false;
    }

    /**
     * Diese Methode ermittelt, ob eine tote Zelle mit der gegebenen Anzahl an lebenden Nachbarn überleben soll
     * @param ctr       Die Anzahl an lebenden Nachbarn
     * @return          ob diese Zelle lebendig sein soll
     */
    private boolean deadCellBorn(int ctr){
//      Wir gehen durch alle Geboren-werden-Regeln
        for(int i = 0; i < birthQualifications.length; i++){
//          falls die Regel am Index i und die gegebene Anzahl lebendiger Nachbarn gleich sind wird true zurückgegeben
            if(birthQualifications[i] == ctr){
                return true;
//          falls diese größer als die gegebene Anzahl lebendiger Nachbarn ist wird false zurückgegeben, da die Regeln sortiert sind, weshalb
//          ab dem ersten Element, das größer ist ALLE Elemente größer sein werden.
            }else if(birthQualifications[i] > ctr){
                return false;
            }
        }

//      Falls nichts in der Schleife zutrifft wird false zurückgegeben
        return false;
    }

    /**
     * Diese Methode zählt die Anzahl an lebendigen Nachbarn der Zelle bei x|y
     * @param x         x-Index der Zelle
     * @param y         y-Index der Zelle
     * @param infinite  Ob das Spielfeld unendlich sein soll
     * @return          Die Anzahl an lebenden Nachbarn
     */
    private int getLivingNeighbors(int x, int y, boolean infinite){
        int ctr = 0;

        /*
         * Wir gehen durch die acht Nachbarn, und zählen die, die lebendig sind.
         * Hier eine Nummerierung, die dabei helfen soll zu verstehen, welcher Befehl welches Feld behandelt
         *  ___ ___ ___
         * | 1 | 2 | 3 |
         * |___|___|___|
         * | 4 | F*| 5 |
         * |___|___|___|
         * | 6 | 7 | 8 |
         * |___|___|___|
         *
         * * das "F" zeigt das Feld, dessen Koordinaten in den Parametern x und y gegeben sind.
         *   Das zeigt, dass wir derzeitig die Anzahl an lebendigen Nachbarn der Zelle "F" mit den Koordinaten (x|y) berechnen.
         */

//      1:
        if( ( infinite || ( x != 0 && y != 0 ) ) && field[(x != 0)? x - 1: field.length - 1][(y != 0)? y - 1: field[0].length - 1] ) ctr++;
//      2:
        if( ( infinite || y != 0 ) && field[x][(y != 0)? y - 1: field[0].length - 1] ) ctr++;
//      3:
        if( ( infinite || ( x != field.length - 1 && y != 0 ) ) && field[(x != field.length - 1)? x + 1: 0][(y != 0)? y - 1: field[0].length - 1] ) ctr++;
//      4:
        if( ( infinite || x != 0 ) && field[(x != 0)? x - 1: field.length - 1][y] ) ctr++;
//      5:
        if( ( infinite || x != field.length - 1 ) && field[(x != field.length - 1)? x + 1: 0][y] ) ctr++;
//      6:
        if( ( infinite || ( x != 0 && y != field[0].length - 1 ) ) && field[(x != 0)? x - 1: field.length - 1][(y != field[0].length - 1)? y + 1: 0] ) ctr++;
//      7:
        if( ( infinite || y != field[0].length - 1 ) && field[x][(y != field[0].length - 1)? y + 1: 0] ) ctr++;
//      8:
        if( ( infinite || ( x != field.length - 1 && y != field[0].length - 1 ) ) && field[(x != field.length - 1)? x + 1: 0][(y != field[0].length - 1)? y + 1: 0] ) ctr++;

        return ctr;
    }
}
