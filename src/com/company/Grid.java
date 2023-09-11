package com.company;

import java.awt.*;

public class Grid {

    private int basisX = 0;   // Для отображения надписей
    private int basisY = 0;

    private int actualShiftX = 0;
    private int actualShiftY = 0;

    private int normal = 86;

    public void changeNormal(int lenX, int lenY) {
        normal += 86;
    }

    public void shiftLine(int lenX, int lenY) {
//        System.out.println("lenX: " + lenX);
        actualShiftX += lenX;
        actualShiftY += lenY;
        int kX = 1;
        int kY = 1;
        int signX = 1;
        int signY = 1;

        int i = Math.abs(actualShiftX / normal);
        int j = Math.abs(actualShiftY / normal);
        if (lenX != 0) {
            signX = actualShiftX / Math.abs(actualShiftX);
        }
        if (lenY != 0) {
            signY = actualShiftY / Math.abs(actualShiftY);
        }
        actualShiftX = Math.abs(actualShiftX);
        actualShiftY = Math.abs(actualShiftY);
        while (actualShiftX >= normal) {
            actualShiftX -= normal;
        }
        while (actualShiftY >= normal) {
            actualShiftY -= normal;
        }
        actualShiftX *= signX;
        actualShiftY *= signY;
        if (lenX > 0) {
            kX = -1;
        }
        if (lenY < 0) {
            kY = -1;
        }
        basisX += normal * i * kX;
        basisY += normal * j * kY;
    }

    public void showGrid(Graphics g) {
        g.setColor(Color.lightGray);

        int step = 86;

        g.drawLine(688 + actualShiftX, 0, 688 + actualShiftX, 735);
        g.drawLine(0, 368 + actualShiftY, 1375, 368 + actualShiftY);

        g.drawString("(" + basisX + ", " + basisY + ")", 688 + 5 + actualShiftX, 368 + 15 + actualShiftY);

        // Рисование линий
        for (int i = 0; i < 8; i++) {
            // Изменение шага
            int delP = step * (i + 1);

            // Рисование осей X
            g.drawLine(688 - delP + actualShiftX, 0, 688 - delP + actualShiftX, 735);
            g.drawLine(688 + delP + actualShiftX, 0, 688 + delP + actualShiftX, 735);

            // Рисование осей Y
            g.drawLine(0, 368 - delP + actualShiftY, 1475, 368 - delP + actualShiftY);
            g.drawLine(0, 368 + delP + actualShiftY, 1475, 368 + delP + actualShiftY);
        }

        // Рисование надписей
        for (int i = 0; i < 8; i++) {
            int delP = step * (i + 1);

            String str1 = "(" + (basisX - delP) + ", " + (basisY) + ")";
            String str2 = "(" + (basisX + delP) + ", " + (basisY) + ")";

            g.drawString(str1, 688 - delP + 5 + actualShiftX, 368 + 15 + actualShiftY);
            g.drawString(str2, 688 + delP + 5 + actualShiftX, 368 + 15 + actualShiftY);
        }

        for (int y = 0; y < 8; y++) {
            int delY = step * (y + 1);
            String str1 = "(" + (basisX) + ", " + (basisY + delY) + ")";
            String str2 = "(" + (basisX) + ", " + (basisY - delY) + ")";
            String str3 = "";
            String str4 = "";

            g.drawString(str1, 688 + 5 + actualShiftX, 368 - delY + 15 + actualShiftY);
            g.drawString(str2, 688 + 5 + actualShiftX, 368 + delY + 15 + actualShiftY);

            for (int x = 0; x < 8; x++) {
                int delX = step * (x + 1);

                str1 = "(" + (basisX - delX) + ", " + (basisY + delY) + ")";
                str2 = "(" + (basisX - delX) + ", " + (basisY - delY) + ")";

                str3 = "(" + (basisX + delX) + ", " + (basisY + delY) + ")";
                str4 = "(" + (basisX + delX) + ", " + (basisY - delY) + ")";

                g.drawString(str1, 688 - delX + 5 + actualShiftX, 368 - delY + 15 + actualShiftY);
                g.drawString(str2, 688 - delX + 5 + actualShiftX, 368 + delY + 15 + actualShiftY);

                g.drawString(str3, 688 + delX + 5 + actualShiftX, 368 - delY + 15 + actualShiftY);
                g.drawString(str4, 688 + delX + 5 + actualShiftX, 368 + delY + 15 + actualShiftY);
            }
        }

        g.setColor(Color.BLACK);
    }

}
