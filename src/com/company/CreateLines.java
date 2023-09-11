package com.company;

import javax.swing.*;
import java.awt.*;

public class CreateLines {

    private void plot(int x, int y, Graphics g) {

        Graphics2D gr = (Graphics2D) g;
        gr.fillRect(x, y, 1, 1);

    }

    private void ddaAlgorithm(int x0, int y0, int x1, int y1, Graphics g) {

        double l = Math.max(Math.abs(x1 - x0), Math.abs(y1 - y0));

        double dX = (x1 - x0) / l;
        double dY = (y1 - y0) / l;

        double [] arrayX = new double[2000];
        double [] arrayY = new double[2000];

        double x = x0;
        double y = y0;
        for (int i = 0; i < l; i++) {
            arrayX[i] = x;
            arrayY[i] = y;

            x += dX;
            y += dY;
        }

        for (int i = 0; i < l; i++) {
            plot((int)arrayX[i], (int)arrayY[i], g);
        }

    }

    public void print(double ix0, double iy0, double ix1, double iy1, Graphics g) {
        double x0, x1, y0, y1;

        if (ix0 > ix1) {
            x0 = ix1;
            x1 = ix0;
            y0 = iy1;
            y1 = iy0;
        } else {
            x1 = ix0;
            x0 = ix1;
            y1 = iy0;
            y0 = iy1;
        }

        ddaAlgorithm((int)x0, (int)y0, (int)x1, (int)y1, g);
    }

}
