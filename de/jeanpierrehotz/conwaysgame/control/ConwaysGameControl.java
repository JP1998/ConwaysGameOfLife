package de.jeanpierrehotz.conwaysgame.control;

import de.jeanpierrehotz.conwaysgame.game.ConwaysGame;
import de.jeanpierrehotz.conwaysgame.ui.ConwaysGameUI;

import java.awt.*;
import java.util.ArrayList;

/**
 * Diese Klasse bildet zum Einen die Steuerungsklasse des Conways Game of life-Spiels, und bildet
 * gleichzeitig auch die Einstiegsklasse (Hauptklasse) des Programms.
 */
public class ConwaysGameControl {

    /**
     * Einstiegspunkt des Programms, in dem nur ein Steuerungsobjekt erzeugt wird, von dem aus
     * dann weitere Vorbereitungen getroffen werden
     * @param args      Kommandozeilenargumente
     */
    public static void main(String[] args){
        new ConwaysGameControl();
    }

    /**
     * Dieses Objekt bildet die Oberfläche, die als Schnittstelle zwischen User und Programm dient.
     */
    private ConwaysGameUI ui;

    /**
     * Dieses Objekt bildet das Spiel, welches gerade gespielt wird.
     */
    private ConwaysGame game;

    /**
     * Diese Variable gibt an, ob sich die Steuerung gerade in einem Spiel befindet
     */
    private boolean inGame;
    /**
     * Diese Variable gibt an, ob das Spielfeldnetz gemalt werden soll.
     */
    private boolean drawingGrid;
    /**
     * Diese Variable gibt an, ob das Spielfeld unendlich sein soll.
     */
    private boolean infinite;
    /**
     * Diese Variable gibt an, ob das Spiel derzeit pausiert ist.
     */
    private boolean paused;
    /**
     * Diese Methode gibt an, wie breit die UI war, als das Spiel gestartet wurde.
     */
    private int width;
    /**
     * Diese Variable gibt an, wie hoch die UI war, als das Spiel gestartet wurde.
     */
    private int height;
    /**
     * Dieser Thread ist der für das Spiel zeitgebende Thread.
     */
    private Thread gameThread;
    /**
     * Diese Methode gibt an, wie lange ein Frame angezeigt werden soll.
     */
    private int sleepTime;

    /**
     * Dieser Konstruktor erzeugt ein Steuerungsobjekt, das mit einem neu erzeugten Oberflächenobjekt assoziiert wird.
     */
    public ConwaysGameControl(){
         ui = new ConwaysGameUI(this);
    }

    /**
     *
     * @return      ob die Steuerung derzeit in einem Spiel ist
     */
    public boolean isInGame() {
        return inGame;
    }
    /**
     * Diese Methode schaltet zwischen Pause und Spielen um.
     */
    public void togglePause(){
        paused = !paused;
    }

    /**
     * Diese Methode zeichnet das aktuelle Spiel in seinem derzeitigen Zustand
     * @param g     das Graphics-Objekt, mit dem gemalt wird
     */
    public void drawGame(Graphics g){
        game.drawGame(drawingGrid, width, height, g);
    }

    /**
     * Diese Methode initialisiert ein Spiel, indem es alle benötigten Werte von der UI einliest, und
     * diese übernimmt
     */
    public void init(){
//      Zuerst lassen wir die UI das Spiel anzeigen, damit der User keine Werte mehr ändern kann
        ui.showGame();

//      Dann initialisieren wir ein Spiel mit den eingelesenen Werten
        init(
                ui.shouldShowGridLines(),
                ui.shouldBeInfinite(),
                ui.getWidth(),
                ui.getHeight(),
                ui.getSpalten(),
                ui.getZeilen(),
                ui.getAktualisierungsZeit(),
                (ui.shouldBeMdifizierteZufallsRate())? ui.getModifizierteZufallsRate(): 0.25
        );

//      Und passen evtl. nachträglich die Regeln an
        if(ui.shouldBeKopierWelt()){
            game.setSurvivorQualifications(new int[]{1, 3, 5, 7});
            game.setBirthQualifications(new int[]{1, 3, 5, 7});
        }else if(ui.shouldBeModifizierteRegeln()){
            game.setSurvivorQualifications(getListOfNumbers(ui.getModifizierteRegelnUeberlebenText()));
            game.setBirthQualifications(getListOfNumbers(ui.getModifizierteRegelnGeburtText()));
        }
    }
    /**
     * Diese Methode initialisiert ein Spiel mit den gegebenen Werten, und startet dieses.
     * Dabei wird kein Zugriff auf die Oberfläche genutzt.
     * @param grid          Ob das Spielfeldnetz gezeichnet werden soll
     * @param inf           Ob das Spielfeld unendlich sein soll
     * @param w             die Breite der Oberfläche in px
     * @param h             die Höhe der Oberfläche in px
     * @param gw            die gewünschte Breite des Spiels in Feldern
     * @param gh            die gewünschte Höhe des Spiels in Feldern
     * @param sl            wie lange ein Frame angezeigt werden soll
     * @param randomRate    wie hoch die Chance darauf sein soll, dass eine Zelle lebendig ist
     */
    private void init(boolean grid, boolean inf, int w, int h, int gw, int gh, int sl, double randomRate){
//      Das neue Spiel erzeugen
        game = new ConwaysGame(w, h, gw, gh, randomRate);

//      Alle Werte übernehmen
        drawingGrid = grid;
        infinite = inf;
        paused = false;
        width = w;
        height = h;
        sleepTime = sl;

        inGame = true;

//      Und das Spiel starten
        gameThread = new Thread(new Timer());
        gameThread.start();
    }

    /**
     * Diese Methode konvertiert einen Text, aus Zahlen, die mit Kommata voneinander getrennt sind, in ein
     * Array aus primitiven Ganzzahlen um.
     * @param text Der Text, der umgewandelt werden soll
     * @return Die Liste der Ganzzahlen in dem gegebenen Text
     */
    private int[] getListOfNumbers(String text){
//      Die Liste an Kommata auftrennen
        String[] temp = text.split(",");
        ArrayList<Integer> tempIntegerList = new ArrayList<>();

//      jedes Element wird dann gekürzt, und versucht in eine Ganzzahl umgewandelt zu werden
        for(int i = 0; i < temp.length; i++){
            temp[i] = temp[i].trim();
            try{
                int tempNum = Integer.parseInt(temp[i]);
                tempIntegerList.add(tempNum);
            }catch (Exception ignored){}
        }

//      Dann konvertieren wir die ArrayList in ein Array
        int[] resultingList = new int[tempIntegerList.size()];
        for(int i = 0; i < tempIntegerList.size(); i++){
            resultingList[i] = tempIntegerList.get(i);
        }

        return resultingList;
    }

    /**
     * Diese Methode lässt die Steuerung das vorherige Spiel wieder aufnehmen
     */
    public void resumeGame(){
        ui.showGame();

        inGame = true;

        gameThread = new Thread(new Timer());
        gameThread.start();

        ui.repaint();
    }

    /**
     * Diese Methode lässt die Steuerung die (evtl.) veränderten Einstellungen speichern, und dann
     * das vorherige Spiel wieder aufnehmen.
     */
    public void saveAndResumeGame(){
        ui.showGame();

        drawingGrid = ui.shouldShowGridLines();
        infinite = ui.shouldBeInfinite();
        sleepTime = ui.getAktualisierungsZeit();

        if(ui.shouldBeKopierWelt()){
            game.setSurvivorQualifications(new int[]{1, 3, 5, 7});
            game.setBirthQualifications(new int[]{1, 3, 5, 7});
        }else if(ui.shouldBeModifizierteRegeln()){
            game.setSurvivorQualifications(getListOfNumbers(ui.getModifizierteRegelnUeberlebenText()));
            game.setBirthQualifications(getListOfNumbers(ui.getModifizierteRegelnGeburtText()));
        }

        inGame = true;

        gameThread = new Thread(new Timer());
        gameThread.start();

        ui.repaint();
    }

    /**
     * Diese Methode setzt alle Zellen des Spiels auf den gegebenen Wert
     * @param status
     */
    public void setAllTo(boolean status){
        game.setAll(status);
        ui.repaint();
    }

    /**
     * Diese Methode setzt die Zelle, die über der Koordinate (x|y) liegt
     * auf den gegebenen Wert
     * @param x     Die x-Koordinate, über der die Zelle liegen soll
     * @param y     Die y-Koordinate über der die Zelle liegen soll
     * @param stat  Der Wert auf den die Zelle gesetzt werden soll
     */
    public void setFieldTo(int x, int y, boolean stat){
        game.setFieldAtTo(x, y, stat);
        ui.repaint();
    }

    /**
     * Diese Methode lässt die Steuerung das Spiel unterbrechen
     */
    public void abortGame(){
        ui.showUI("Sie haben das Spiel unterbrochen!");
        ui.setResumable(true);
        ui.repaint();
        inGame = false;
    }

    /**
     * Diese Methode führt einen Tick des Spiels aus.
     * In diesem wird nur das Spielfeld neu berechnet und angezeigt
     */
    private void onTick(){
        game.aktualisiereFeld(infinite);
        ui.repaint();
    }

    /**
     * Diese Klasse gibt in einem regelmäßigen Zeitabstand einen Aufruf der onTick-Methode aus,
     * und bildet somit die Zeitsteuerung des Programms
     * @author Admin
     */
    private class Timer implements Runnable{
        @Override
        public void run() {
            Thread curr = Thread.currentThread();
            while(curr == gameThread && inGame){
                if(!paused) onTick();
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Diese Methode stopt den Spielthread, damit man das Programm beenden kann
     */
    public void disposeThread(){
        gameThread = null;
    }
}
