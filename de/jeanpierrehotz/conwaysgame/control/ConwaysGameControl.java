package de.jeanpierrehotz.conwaysgame.control;

import de.jeanpierrehotz.conwaysgame.game.ConwaysGame;
import de.jeanpierrehotz.conwaysgame.ui.ConwaysGameUI;

import java.awt.*;

/**
 * Created by Admin on 16.04.2016.
 */
public class ConwaysGameControl {

    private ConwaysGameUI ui;

    private ConwaysGame game;

    private boolean inGame;
    public boolean isInGame() {
        return inGame;
    }

    private boolean drawingGrid;
    private boolean infinite;
    private boolean paused;
    public void togglePause(){
        paused = !paused;
    }
    private int width;
    private int height;
    private Thread gameThread;
    private int sleepTime;

    public ConwaysGameControl(){
         ui = new ConwaysGameUI(this);
    }

    public void drawGame(Graphics g){
        game.drawGame(drawingGrid, width, height, g);
    }

    public static void main(String[] args){
        new ConwaysGameControl();
    }

    public void init(){
        ui.showGame();
        init(
                ui.shouldShowGridLines(),
                ui.shouldBeInfinite(),
                ui.getWidth(),
                ui.getHeight(),
                ui.getSpalten(),
                ui.getZeilen(),
                ui.getAktualisierungsZeit()
        );
    }

    public void resumeGame(){
        ui.showGame();

        inGame = true;

        gameThread = new Thread(new Timer());
        gameThread.start();

        ui.repaint();
    }

    public void setAllTo(boolean status){
        game.setAll(status);
        ui.repaint();
    }

    public void setFieldTo(int x, int y, boolean stat){
        game.setFieldAtTo(x, y, stat);
        ui.repaint();
    }

    public void abortGame(){
        ui.showUI("Sie haben das Spiel unterbrochen!");
        ui.setResumable(true);
        ui.repaint();
        inGame = false;
    }

    private void init(boolean grid, boolean inf, int w, int h, int gw, int gh, int sl){
        game = new ConwaysGame(w, h, gw, gh, 0.25);

        drawingGrid = grid;
        infinite = inf;
        paused = false;
        width = w;
        height = h;
        sleepTime = sl;

        inGame = true;

        gameThread = new Thread(new Timer());
        gameThread.start();
    }

    private void onTick(){
        game.aktualisiereFeld(infinite);
        ui.repaint();
    }

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
    public void disposeThread(){
        gameThread = null;
    }
}
