package de.jeanpierrehotz.conwaysgame.control;

import de.jeanpierrehotz.conwaysgame.game.ConwaysGame;
import de.jeanpierrehotz.conwaysgame.ui.ConwaysGameUI;

import java.awt.*;
import java.util.ArrayList;

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
                ui.getAktualisierungsZeit(),
                (ui.shouldBeMdifizierteZufallsRate())? ui.getModifizierteZufallsRate(): 0.25
        );

        if(ui.shouldBeKopierWelt()){
            game.setSurvivorQualifications(new int[]{1, 3, 5, 7});
            game.setBirthQualifications(new int[]{1, 3, 5, 7});
        }else if(ui.shouldBeModifizierteRegeln()){
            game.setSurvivorQualifications(getListOfNumbers(ui.getModifizierteRegelnUeberlebenText()));
            game.setBirthQualifications(getListOfNumbers(ui.getModifizierteRegelnGeburtText()));
        }
    }

    private int[] getListOfNumbers(String text){
        String[] temp = text.split(",");
        ArrayList<Integer> tempIntegerList = new ArrayList<>();


        for(int i = 0; i < temp.length; i++){
            temp[i] = temp[i].trim();
            try{
                int tempNum = Integer.parseInt(temp[i]);
                tempIntegerList.add(tempNum);
            }catch (Exception exc){}
        }

        int[] resultingList = new int[tempIntegerList.size()];
        for(int i = 0; i < tempIntegerList.size(); i++){
            resultingList[i] = tempIntegerList.get(i);
        }

        return resultingList;
    }

    public void resumeGame(){
        ui.showGame();

        inGame = true;

        gameThread = new Thread(new Timer());
        gameThread.start();

        ui.repaint();
    }

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

    private void init(boolean grid, boolean inf, int w, int h, int gw, int gh, int sl, double randomRate){
        game = new ConwaysGame(w, h, gw, gh, randomRate);

        drawingGrid = grid;
        infinite = inf;
        paused = false;
        width = w;
        height = h;
        sleepTime = sl;

        inGame = true;

        paused = true;

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
