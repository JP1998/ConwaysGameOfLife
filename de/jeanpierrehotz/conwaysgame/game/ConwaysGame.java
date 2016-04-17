package de.jeanpierrehotz.conwaysgame.game;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Admin on 16.04.2016.
 */
public class ConwaysGame {

    private boolean[][] field;

    private int size;

    private static int OFFSET = 200;

    public void setAll(boolean stat){
        for(int i = 0; i < field.length; i++){
            for(int j = 0; j < field[i].length; j++){
                field[i][j] = stat;
            }
        }
    }

    public void setFieldAtTo(int x, int y, boolean stat){
        int xInd = OFFSET + (x / size);
        int yInd = OFFSET + (y / size);

//        System.out.println(xInd + "  " + yInd);

        field[xInd][yInd] = stat;
    }

    public ConwaysGame(int x, int y, int w, int h){
        this(x, y, w, h, 0.25);

//        field = new boolean[2 * OFFSET + 16][2 * OFFSET + 8];
//        size = 20;
//
//        for(int i = 0; i < field.length; i++){
//            for(int j = 0; j < field[i].length; j++){
//                field[i][j] = Math.random() > 0.5;
//            }
//        }
    }

    public ConwaysGame(int x, int y, int w, int h, double ratio){
        this(x, y, w, h, ratio, new int[]{2, 3}, new int[]{3});
    }

    public ConwaysGame(int x, int y, int w, int h, double ratio, int[] survivor, int[] birth){
        int realW = x / w;
        int realH = y / h;

//        field = new boolean[w + 400][h + 400];
        if(realW < realH){
            field = new boolean[(x / realW) + (2 * OFFSET)][(y / realW) + (2 * OFFSET)];
            size = realW;
        }else{
            field = new boolean[(x / realH) + (2 * OFFSET)][(y / realH) + (2 * OFFSET)];
            size = realH;
        }

        for(int i = 0; i < field.length; i++){
            for(int j = 0; j < field[i].length; j++){
                field[i][j] = Math.random() > ratio;
            }
        }

        setSurvivorQualifications(survivor);
        setBirthQualifications(birth);
    }

    public void drawGame(boolean drawGrid, int x, int y, Graphics g){
        Color tempCol = g.getColor();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, x, y);

        if(drawGrid) {

            g.setColor(Color.GRAY);

            for (int i = 0; i <= field.length - (2 * OFFSET); i++)
                g.drawLine(i * size, 0, i * size, y);

            for (int i = 0; i <= field[0].length - 2 * (OFFSET); i++)
                g.drawLine(0, i * size, x, i * size);
        }

        g.setColor(Color.BLACK);

        for(int i = OFFSET; i < field.length - /*2 **/ OFFSET; i++)
            for(int j = OFFSET; j < field[i].length - /*2 **/ OFFSET; j++)
                if(field[i][j])
                    g.fillRect((i - OFFSET) * size /*+ 30*/, (j - OFFSET) * size /*+ 30*/, size, size);

        g.setColor(tempCol);
//        System.out.println(field.length + "|" + field[0].length);
    }

    public void aktualisiereFeld(boolean infinite){
        field = neuesFeld(infinite);
    }

    private boolean[][] neuesFeld(boolean infinite){
        boolean[][] newField = new boolean[field.length][field[0].length];

/*
        for(boolean[] col: field){
            System.out.print("|");
            for(boolean f: col){
                System.out.print((f)? "X|": " |");
            }
            System.out.println();
        }
        System.out.println("__________________________________________________");
*/


        for(int i = 0; i < field.length; i++){
//            System.out.print("|");
            for(int j = 0; j < field[i].length; j++){
                int living = getLivingNeighbors(i, j, infinite);
//                System.out.print(living + "|");

                newField[i][j] = ( field[i][j] && livingCellSurvives(living) ) || ( !field[i][j] && deadCellBorn(living) );
                        //( field[i][j] && ( living == 2 || living == 3 ) ) || ( !field[i][j] && living == 3 );
            }
//            System.out.println();
        }
//        System.out.println("__________________________________________________");
//        for(boolean[] col: newField){
//            System.out.print("|");
//            for(boolean f: col){
//                System.out.print((f)? "X|": " |");
//            }
//            System.out.println();
//        }


        return newField;
    }

    private boolean livingCellSurvives(int ctr){
        for(int i = 0; i < survivorQualifications.length; i++){
            if(survivorQualifications[i] == ctr){
                return true;
            }
        }

        return false;
    }

    private boolean deadCellBorn(int ctr){
        for(int i = 0; i < birthQualifications.length; i++){
            if(birthQualifications[i] == ctr){
                return true;
            }
        }
        return false;
    }

    private int[] survivorQualifications;
    private int[] birthQualifications;

    public void setSurvivorQualifications(int[] survivor){
        ArrayList<Integer> temp = new ArrayList<>();

        for(int i = 0; i < survivor.length; i++){
            if(survivor[i] >= 0 && survivor[i] <= 8 && !temp.contains(survivor[i])){
                temp.add(survivor[i]);
            }
        }

        survivorQualifications = new int[temp.size()];

        for(int i = 0; i < temp.size(); i++){
            survivorQualifications[i] = temp.get(i);
        }
    }

    public void setBirthQualifications(int[] birth){
        ArrayList<Integer> temp = new ArrayList<>();

        for(int i = 0; i < birth.length; i++){
            if(birth[i] >= 0 && birth[i] <= 8 && !temp.contains(birth[i])){
                temp.add(birth[i]);
            }
        }

        birthQualifications = new int[temp.size()];

        for(int i = 0; i < temp.size(); i++){
            birthQualifications[i] = temp.get(i);
        }
    }

    private int getLivingNeighbors(int x, int y, boolean infinite){
        int ctr = 0;

        /*
         * We'll work through the eight neighbors, and count those, that are living.
         * Here's a numberation , which helps to understand which statement handles which field:
         *  ___ ___ ___
         * | 1 | 2 | 3 |
         * |___|___|___|
         * | 4 | F*| 5 |
         * |___|___|___|
         * | 6 | 7 | 8 |
         * |___|___|___|
         *
         * *the "F" shows the field whose coordinates are given in the parameters x and y.
         *  Therefor we are currently calculating the amount of living neighbors of the field "F"
         *  with the coordinates (x|y)
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
