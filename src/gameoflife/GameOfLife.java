/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;
import java.lang.Thread;

/**
 *
 * @author hun7er
 */

class GameThread extends Thread {
    GameOfLife g;
    GOLFrame frame;
    public boolean cont = true, started = false;
    
    public GameThread(GOLFrame _f) {
        this.g = _f.mygame;
        this.frame = _f;
    }
    
    public void run() {
        if (this.started == false) started = true;
        /*synchronized(this) {
           while (!cont)
               try {
                   wait();
               } catch (Exception e) {}
           notify();
        }*/
        while(cont) {
            g.update();
            g.generation++;
            frame.genLabel.setText(String.valueOf(g.generation));
            frame.redrawTable();
            /* for (int i = 0; i < jTable1.getColumnCount(); i++) {
                javax.swing.table.TableColumn tableCol = jTable1.getColumnModel().getColumn(i);
                tableCol.setCellRenderer(new CustomRenderer( Font.decode("Monospace"), Color.BLACK, JLabel.CENTER, mygame ));
                jTable1.repaint();
            }
             Tutaj dodać odrysowywanie tabeli */
            try {
                Thread.sleep(50);
            } catch(Exception e) {
            } // try {} catch {}
        } // while
    } // public void run()
} // class GameThread

class CalcThread extends Thread {
    int x, y, height, width;
    GameOfLife g;
    
    public CalcThread(int _x, int _y, GameOfLife game) {
        //tab1 = t1;
        //tab2 = t2;
        x = _x;
        y = _y;
        //height = h;
        //width = w;
        g = game;
        height = g.height;
        width = g.width;
    }
    
    public void run() {
        //System.out.println("Hello from a thread!");
        int s = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int _y = y+i;
                int _x = x+j;
                if ((i != 0 || j != 0) && _y >= 0 && _x >= 0 && _y < height && _x < width) {
                    if(g.tab[_y][_x] == true) s++;
                }
            }
        }
        if (g.tab[y][x] == false) {
            if (s == 3) g.tab2[y][x] = true;
        } else if (g.tab[y][x] == true) {
            if(s < 2 || s > 3) g.tab2[y][x] = false;
            else g.tab2[y][x] = true;
        }
    }
    
}

public class GameOfLife {
    public boolean[][] tab, tab2;
    public int generation = 0;
    public int width, height;
    //public int threadCount = 0;
    
    public void update() {
        //tab[2][2] = true;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                CalcThread c = new CalcThread(j, i, this);
                c.start();
                //c.start();
                //System.out.println("Hello from GameOfLife::update!");
            }
        }
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tab[i][j] = tab2[i][j];
                tab2[i][j] = false;
            }
        }
    }
    
    public void setupAll() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tab[i][j] = false;
                tab2[i][j] = false;
            }
        }
    }
    
    public GameOfLife(int w, int h) {
        this.width = w;
        this.height = h;
        tab = new boolean[h][w];
        tab2 = new boolean[h][w];
        setupAll();
        //System.out.println("Hello from GameOfLife!");
    }
    
}
