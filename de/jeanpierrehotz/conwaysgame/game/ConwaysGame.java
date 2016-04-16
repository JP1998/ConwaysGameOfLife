package de.jeanpierrehotz.conwaysgame.game;

import java.awt.*;

/**
 * Created by Admin on 16.04.2016.
 */
public class ConwaysGame {

    private boolean[][] field;

    private int size;

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

    public ConwaysGame(int x, int y, int w, int h, double ratio){
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
    }

    public void drawGame(boolean drawGrid, int x, int y, Graphics g){
        if(drawGrid) {
            for (int i = 0; i <= field.length - (2 * OFFSET); i++)
                g.drawLine(i * size, 0, i * size, y);

            for (int i = 0; i <= field[0].length - 2 * (OFFSET); i++)
                g.drawLine(0, i * size, x, i * size);
        }

        for(int i = OFFSET; i < field.length - /*2 **/ OFFSET; i++)
            for(int j = OFFSET; j < field[i].length - /*2 **/ OFFSET; j++)
                if(field[i][j])
                    g.fillRect((i - OFFSET) * size /*+ 30*/, (j - OFFSET) * size /*+ 30*/, size, size);

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

                newField[i][j] = ( field[i][j] && ( living == 2 || living == 3 ) ) || ( !field[i][j] && living == 3 );
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
