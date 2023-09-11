package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Draw extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;

    private ArrayList<RegularPolygon> allPolygons = new ArrayList<RegularPolygon>();    // Наши фигуры
    private ArrayList<Square> allSquare = new ArrayList<Square>();      // Контуры для фигур
    private ArrayList<Vertex> allVertex = new ArrayList<Vertex>();        // Точки для управления вершинами
    private int activityFigure = -1;
    private CreateLines l = new CreateLines();

    private Grid grid = new Grid();

    private int bX = 0;
    private int bY = 0;

    private int degreeOfScale = 0;

    private boolean editingFigure = false;    // Редактируется контур
    private boolean editingVertex = false;    // Редактируются точки
    private boolean editingWindow = false;    // Редактируется окно

    private boolean showGrid = false;         // Показ координатной сетки

    private boolean isEdited = false;         // Фигура редактируется (происходит взаимодействие с одним из маркеров)
    private boolean shiftPolygon = false;     // Передвижение всего многоугольника
    private boolean zoomFromUpp = false;      // Изменение масштаба с помощью верхних маркеров
    private boolean zoomFromDown = false;     // Изменение масштаба с помощью нижних маркеров
    private boolean shiftVertex = false;      // Передвижение точки

    private int needVertex;                   // Индекс точки для перемещения

    // Точки, с помощью которых можем тянуть мышью
    private int startPointX;
    private int startPointY;
    private int finishPointX;
    private int finishPointY;

    private void cleanTextAreas() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
    }

    private int getInteger(JTextField te) {
        return Integer.parseInt(te.getText());
    }

    public Draw() {
        this.setTitle("FrameMain");
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(1200, 500);
        this.setVisible(true);

        // Создание новой фигуры
        button1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(bY);
                int x0 = getInteger(textField1) + bX;
                int y0 = getInteger(textField2) - bY;
                int r = getInteger(textField3);
                int n = getInteger(textField4);

                cleanTextAreas();
                RegularPolygon p = new RegularPolygon();
                p.createPolygon(x0, y0, r, n);
                allPolygons.add(p);

                // Формируем средства редактирования
                // Контур
                Square sq = new Square();
                sq.createSquare(p.getYUpp(), p.getYDown(), p.getXLeft(), p.getXRight(),
                        p.getCentreX(), p.getCentreY());
                allSquare.add(sq);

                // Точки
                Vertex ver = new Vertex();
                ver.createMarkers(p);
                allVertex.add(ver);

                callRe();
            }
        });

        // Активация редактирования фигуры (окна)
        panel1.addMouseListener(new MouseEvents() {
            @Override
            public void mousePressed(MouseEvent e) {
//                System.out.println("Mouse Pressed");
                startPointX = e.getX() + 8;
                startPointY = e.getY() + 31;

                if (activityFigure != -1) {
                    Square sq = allSquare.get(activityFigure);
                    RegularPolygon pol = allPolygons.get(activityFigure);

                    if (!allSquare.get(activityFigure).weAtSquare(startPointX, startPointY)) {
                        // Мы не в фигуре!
                        activityFigure = -1;
                        callRe();
                        // Определяем тип редактирования
                    } else if (editingFigure) {
                        contourType(startPointX, startPointY, sq);
                    } else if (editingVertex) {
                        vertexNeeds(startPointX, startPointY, pol);
                    }
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
//                System.out.println("Mouse Released");
                finishPointX = e.getX() + 8;
                finishPointY = e.getY() + 31;
                int delX = finishPointX - startPointX;
                int delY = finishPointY - startPointY;
                if (isEdited && (delX != 0 || delY != 0)) {
                    if (editingFigure) {
                        editContour(delX, delY);
                        callRe();
                    }
                    if (editingVertex) {
                        editVertex(delX, delY);
                        callRe();
                    }
                }
                if (editingWindow) {
                    bX += delX;
                    bY += delY;
                    shiftAll(delX, delY);
                }

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (editingFigure || editingVertex) {
                    searchActivityFigure(e.getX() + 8, e.getY() + 31);
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("!");
            }
        });

        // Режим редактирования через контур
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editingVertex = false;
                editingWindow = false;

                editingFigure = true;

                if (activityFigure != -1) {
                    callRe();
                }
            }
        });

        // Режим редактирования через вершины
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editingFigure = false;
                editingWindow = false;

                editingVertex = true;

                if (activityFigure != 1) {
                    callRe();
                }
            }
        });

        // Показ координатной сетки
        button4.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               showGrid = !showGrid;
               callRe();
           }
        });

        // Обзор
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editingFigure = false;
                editingVertex = false;
                isEdited = false;
                activityFigure = -1;

                editingWindow = !editingWindow;
                callRe();
            }
        });
    }

    // Редактирование контура
    public void editContour(int delX, int delY) {
        // Сдвиг всей фигуры
        if (shiftPolygon) {
            allPolygons.get(activityFigure).shiftPoints(delX, delY);
            allSquare.get(activityFigure).shiftLines(delX, delY);
            allVertex.get(activityFigure).shiftPoints(delX, delY);
        }
        // Масштабирование верхним маркером
        if (zoomFromUpp) {
            allPolygons.get(activityFigure).changeScaling(delY * -1);
            createSq(allPolygons.get(activityFigure));
            allVertex.get(activityFigure).scalingPoints(allPolygons.get(activityFigure));
        }
        // Масштабирование нижним маркером
        if (zoomFromDown) {
            allPolygons.get(activityFigure).changeScaling(delY);
            createSq(allPolygons.get(activityFigure));
            allVertex.get(activityFigure).scalingPoints(allPolygons.get(activityFigure));
        }
        endOfEditing();
    }

    // Редактирование точек
    public void editVertex(int delX, int delY) {
        if (isEdited && shiftVertex) {
            RegularPolygon pol = allPolygons.get(activityFigure);
            int xLeft = pol.getXLeft();
            int xRight = pol.getXRight();
            int yUpp = pol.getYUpp();
            int yDown = pol.getYDown();

            allPolygons.get(activityFigure).changeOnePoint(delX, delY, needVertex);
            allVertex.get(activityFigure).shiftPoint(delX, delY, needVertex);

            if (xLeft != pol.getXLeft() || xRight != pol.getXRight() || yUpp != pol.getYUpp()
                    || yDown != pol.getYDown()) {
                createSq(pol);
            }
            callRe();
        }
        endOfEditing();
    }

    public void createSq(RegularPolygon rp) {
        Square sq = new Square();
        sq.createSquare(rp.getYUpp(), rp.getYDown(), rp.getXLeft(), rp.getXRight(),
                rp.getCentreX(), rp.getCentreY());
        allSquare.set(activityFigure, sq);
    }

    public void endOfEditing() {
        isEdited = false;
        shiftPolygon = false;
        zoomFromUpp = false;
        zoomFromDown = false;
        shiftVertex = false;
    }

    // Определяем - что мы делаем с данной фигурой
    public void contourType(int x, int y, Square sq) {
        if (sq.itIsCenter(x, y)) {
            shiftPolygon = true;
        }
        if (sq.weAtUppVertex(x, y)) {
            zoomFromUpp = true;
        }
        if (sq.weAtDownVertex(x, y)) {
            zoomFromDown = true;
        }

        if (shiftPolygon || zoomFromUpp || zoomFromDown) {
            isEdited = true;
        }
    }

    // Определяем точку, которую хотим переместить
    public void vertexNeeds(int x, int y, RegularPolygon pol) {
        int indexOfPoint = -1;
        for (int i = 0; i < pol.getCount(); i++) {
            if (pol.itIsPoint(x, y, i)) {
                indexOfPoint = i;
                break;
            }
        }
        if (indexOfPoint != -1) {
            needVertex = indexOfPoint;
            shiftVertex = true;
            isEdited = true;
        }
    }

    // Идентификация выбранной фигуры
    public void searchActivityFigure(int x, int y) {
        boolean isItFigure = false;

        int lenSquare = allSquare.toArray().length;
        for (int i = lenSquare - 1; i >= 0; i--) {
            Square sq = allSquare.get(i);

            // Фигура, соответствующая данной области
            if (sq.weAtSquare(x, y)) {
                activityFigure = i;
                isItFigure = true;
                break;
            }
        }
        if (!isItFigure) {
            activityFigure = -1;
        }
        if (editingFigure || editingVertex) {
            callRe();
        }
    }

    // Сдвинуть всё!
    public void shiftAll(int lenX, int lenY) {
        int len = allPolygons.toArray().length;
        for (int i = 0; i < len; i++) {
            shiftPolygon = true;
            activityFigure = i;
            editContour(lenX, lenY);
        }
        activityFigure = -1;
        shiftPolygon = false;

        grid.shiftLine(lenX, lenY);

        callRe();
    }

    // Т.к. в теле условий repaint() не вызывается...
    public void callRe() {
        repaint();
    }


    private void prepareToPrint(RegularPolygon pol, int numberFigure, Graphics g) {
        // Соединяем вершины линиями
        for (int i = 1; i < pol.getCount(); i++) {
            l.print(pol.getPointX(i), pol.getPointY(i), pol.getPointX(i - 1), pol.getPointY(i - 1), g);

            if (i == pol.getCount() - 1) {
                l.print(pol.getPointX(i), pol.getPointY(i), pol.getPointX(0), pol.getPointY(0), g);
            }
        }
        if (editingFigure) {
            if (activityFigure == numberFigure) {
                allSquare.get(numberFigure).showSquare(g);
            }
        } else if (editingVertex) {
            if (activityFigure == numberFigure) {
                allVertex.get(numberFigure).showMarkers(g);
            }
        }
    }

    public void paint(Graphics g){
        super.paint(g);

        Graphics2D gr = (Graphics2D) g;

        if (showGrid) {
            grid.showGrid(gr);
        }

        // Передаём фигуры
        int i = 0;
        for (RegularPolygon p : allPolygons) {
            prepareToPrint(p, i, g);
            i += 1;
        }

    }

}
