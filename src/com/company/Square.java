package com.company;

import java.awt.*;

// Квадрат для масштабирования и сдвига
public class Square {

    public int yUpp;
    public int yDown;
    public int xLeft;
    public int xRight;

    public int xCentre;
    public int yCentre;

    public void createSquare(int yUpp, int yDown, int xLeft, int xRight, int xCentre, int yCentre) {
        this.yUpp = yUpp;
        this.yDown = yDown;
        this.xLeft = xLeft;
        this.xRight = xRight;

        this.xCentre = (xRight + xLeft) / 2;
        this.yCentre = (yDown + yUpp) / 2;


//        this.xCentre = xCentre;
//        this.yCentre = yCentre;
    }

    public void shiftLines(int delX, int delY) {
        xLeft += delX;
        xRight += delX;
        yUpp += delY;
        yDown += delY;

        xCentre += delX;
        yCentre += delY;
    }

    // Вычисляет, являемся ли мы внутри квадрата или нет ?
    public boolean weAtSquare(int x, int y) {
//        System.out.println(yUpp + " <= " + y + " <= " + yDown);
        return ((yUpp <= y && y <= yDown) && (xLeft <= x && x <= xRight));
    }

    // Мы в центральном маркере ?
    public boolean itIsCenter(int x, int y) {
//        System.out.println(xCentre - 10 + " < " + x + " < " + (xCentre + 10));
//        System.out.println(yCentre + " -- " + y);
        return ((xCentre - 20 <= x && x <= xCentre + 20) && (yCentre - 20 <= y && y <= yCentre + 20));
    }

    // Мы дёргаем верхние уголки контура ? (изменение масштаба)
    public boolean weAtUppVertex(int x, int y) {
        return ((xRight - 10 <= x && x <= xRight + 10) && (yUpp + 10 >= y && y >= yUpp - 10)) ||
                ((xLeft - 10 <= x && x <= xLeft + 10) && (yUpp + 10 >= y && y >= yUpp - 10));
    }

    // Мы дёргаем нижние уголки контура ? (изменение масштаба)
    public boolean weAtDownVertex(int x, int y) {
        return ((xRight - 10 <= x && x <= xRight + 10) && (yDown + 10 >= y && y >= yDown - 10)) ||
                ((xLeft - 10 <= x && x <= xLeft + 10) && (yDown + 10 >= y && y >= yDown - 10));
    }

    //Рисуем "описанный" прямоугольник для многоугольников
    public void showSquare(Graphics g) {

        CreateLines l = new CreateLines();

        g.setColor(Color.CYAN);
        g.drawLine(xLeft, yUpp, xRight, yUpp);
        g.drawLine(xLeft, yUpp, xLeft, yDown);
        g.drawLine(xLeft, yDown, xRight, yDown);
        g.drawLine(xRight, yDown, xRight, yUpp);

        g.drawRect(xLeft - 10, yUpp - 10, 20, 20);
        g.drawRect(xRight - 10, yUpp - 10, 20, 20);
        g.drawRect(xLeft - 10, yDown - 10, 20, 20);
        g.drawRect(xRight - 10, yDown - 10, 20, 20);

        g.drawRect(xCentre - 10, yCentre - 10, 20, 20);

        g.setColor(Color.BLACK);

    }
}
