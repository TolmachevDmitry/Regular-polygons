package com.company;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

// ДЛЯ КООРДИНАТЫ X МЫ "БЕРЁМ" КОСИНУС, А ДЛЯ Y "БЕРЁМ"

class RegularPolygon {

    private class Point {
        public int x;
        public int y;
        public int num;
        public int radius;
        public double valSin;
        public double valCos;

        public Point(int x, int y, int num, int radius, double valC, double valS) {
            this.x = x;
            this.y = y;
            this.num = num;
            this.radius = radius;
            this.valSin = valS;
            this.valCos = valC;
        }

        public void plusDel(int delX, int delY) {
            this.x += delX;
            this.y += delY;
        }
    }

    private ArrayList<Point> points = new ArrayList<Point>();
    private Point centre;
    private int n;
    private int r0;
    private int yUpp, yDown, xLeft, xRight;


    private double calculationNormalForm(double value) {
        if (value > 270) {
            return 360 - value;
        } else if (value > 180) {
            return value - 180;
        } else if (value > 90) {
            return 180 - value;
        } else {
            return value;
        }
    }

    private int calculationSign(double value, int f) {
        if (f == 0) {     //This is sin
            if (value > 180) {
                return -1;
            }
        }
        if (f == 1) {    // This is cos
            if (90 < value && value < 270) {
                return -1;
            }
        }
        return 1;
    }

    // Фиксация крайних точек
    private void limitPoints(int x0, int y0) {
        if (points.toArray().length == 0) {
            xLeft = x0;
            xRight = x0;
            yUpp = y0;
            yDown = y0;
        } else {
            if (x0 <= xLeft) {
                xLeft = x0;
            }
            if (x0 >= xRight) {
                xRight = x0;
            }
            if (y0 >= yDown) {
                yDown = y0;
            }
            if (y0 <= yUpp) {
                yUpp = y0;
            }
        }
    }


    // Данный метод распределяет именно вершины
    public void createPolygon(double x0, double y0, int r, int n) {
        this.centre = new Point((int) (688 + x0), (int) (368 - y0), -1, 0, 0, 0);
        this.n = n;
        double mainCorner = 360 / n;
        r0 = r;

        for (int i = 0; i < n; i++) {
            double corner = mainCorner + ((2 * 180 * i) / n);
            double valX = Math.cos(Math.toRadians(calculationNormalForm(corner))) * calculationSign(corner, 1);
            double valY = Math.sin(Math.toRadians(calculationNormalForm(corner))) * calculationSign(corner, 0);

            int xOut = (int) (centre.x + r * valX);
            int yOut = (int) (centre.y + r * valY);

            limitPoints(xOut, yOut);

            System.out.println(valX + " <> " + valY);

            points.add(new Point(xOut, yOut, i, r, valX, valY));
        }

//        for (int i = 0; i < points.toArray().length; i++) {
//            System.out.println(points.get(i).y);
//        }
//        System.out.println(yUpp + " --- " + yDown);
    }

    // В этой ли мы точке ?
    public boolean itIsPoint(int x, int y, int index) {
        Point p = points.get(index);
        return (p.x - 10 <= x && x <= p.x + 10) && (p.y - 10 <= y && y <= p.y + 10);
    }

    // Смещение всех точек
    public void shiftPoints(int delX, int delY) {
        centre.x += delX;
        centre.y += delY;

        for (int i = 0; i < n; i++) {
            shiftPoint(delX, delY, i);
        }

        xLeft += delX;
        xRight += delX;
        yUpp += delY;
        yDown += delY;
    }

    public void shiftPoint(int delX, int delY, int index) {
        points.get(index).plusDel(delX, delY);
    }

    public void changeOnePoint(int dexX, int dexY, int index) {
        Point p = points.get(index);
        int x = p.x + dexX;
        int y = p.y + dexY;

        int cX = centre.x;
        int cY = centre.y;
        int r = (int) Math.sqrt((x - cX) * (x - cX) + (y - cY) * (y - cY));

        double valSin = (double) (y - cY) / r;
        double valCos = (double) (x - cX) / r;
        points.set(index, new Point(x, y, index, r, valCos, valSin));

        changeLimits(x, y);
    }

    public void changeLimits(int x, int y) {
        if ((xLeft < x && x < xRight) || (yUpp < y && y < yDown)) {
            searchLimits(x, y);
        } else {
            comparisonPoints(x, y);
        }
    }

    private void searchLimits(int x, int y) {
        int xL = x;
        int xR = x;
        int yU = y;
        int yD = y;
        for (int i = 0; i < points.toArray().length; i++) {
            Point pT = points.get(i);
            if (pT.x > xR) {
                xR = pT.x;
            } else if (pT.x < xL) {;
                xL = pT.x;
            }
            if (pT.y < yU) {
                yU = pT.y;
            } else if (pT.y > yD) {
                yD = pT.y;
            }
        }
        xLeft = xL;
        xRight = xR;
        yUpp = yU;
        yDown = yD;
    }

    private void comparisonPoints(int x, int y) {
        if (x < xLeft) {
            xLeft = x;
        } else if (xRight < x) {
            xRight = x;
        }
        if (y < yUpp) {
            yUpp = y;
        } else if (y > yDown) {
            yDown = y;
        }
    }

    // Масштабирование
    public void changeScaling(int dSc) {
        int dYU = 0;
        int dYD = 0;
        int dXL = 0;
        int dXR = 0;

        for (int i = 0; i < points.toArray().length; i++) {
            Point p = points.get(i);

            int x0 = p.x;
            int y0 = p.y;
            int r1 = p.radius + dSc;
            int x = (int) (centre.x + r1 * p.valCos);
            int y = (int) (centre.y + r1 * p.valSin);
            points.set(i, new Point(x, y, i, r1, p.valCos, p.valSin));

            if (y0 == yUpp) {
                dYU = y0 - y;
            }
            if (y0 == yDown) {
                dYD = y - y0;
            }
            if (x0 == xLeft) {
                dXL = x0 - x;
            }
            if (x0 == xRight) {
                dXR = x - x0;
            }
        }
        xLeft -= dXL;
        xRight += dXR;
        yUpp -= dYU;
        yDown += dYD;
    }

    public int getPointX(int index) {
        return points.get(index).x;
    }

    public int getPointY(int index) {
        return points.get(index).y;
    }

    public int getCentreX() {
        return centre.x;
    }

    public int getCentreY() {
        return centre.y;
    }

    public int getCount() {
        return n;
    }

    public int getXLeft() {
        return xLeft;
    }

    public int getXRight() {
        return xRight;
    }

    public int getYUpp() {
        return yUpp;
    }

    public int getYDown() {
        return yDown;
    }
}
