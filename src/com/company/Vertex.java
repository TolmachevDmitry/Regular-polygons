package com.company;

import java.awt.*;
import java.util.ArrayList;

public class Vertex {

    private class Marker {
        public int x;
        public int y;

        public Marker(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void plusDel(int delX, int delY) {
            this.x += delX;
            this.y += delY;
        }
    }

    private ArrayList<Marker> markers = new ArrayList<Marker>();

    // Создание нового маркера
    public void createMarkers(RegularPolygon p) {
        for (int i = 0; i < p.getCount(); i++) {
            markers.add(new Marker(p.getPointX(i), p.getPointY(i)));
        }
    }

    // Смещение всех точек
    public void shiftPoints(int delX, int delY) {
        int n = markers.toArray().length;
        for (int i = 0; i < n; i++) {
            shiftPoint(delX, delY, i);
        }
    }

    // Смещение точки
    public void shiftPoint(int delX, int delY, int index) {
        markers.get(index).plusDel(delX, delY);
    }

    // Масштабирование всех точек
    public void scalingPoints(RegularPolygon pol) {
        for (int i = 0; i < pol.getCount(); i++) {
            markers.set(i, new Marker(pol.getPointX(i), pol.getPointY(i)));
        }
    }

    // Отображение маркеров
    public void showMarkers(Graphics g) {
        for (int i = 0; i < markers.toArray().length; i++) {
            g.setColor(Color.CYAN);
            Marker m = markers.get(i);
            g.drawOval(m.x - 10, m.y - 10, 20, 20);
            // Окрестность вершины многоугольника - 20px

            g.setColor(Color.BLACK);
        }
    }

    public void uppDateOfMarker(int x, int y, int index) {
        markers.add(index, new Marker(x, y));
    }

}
