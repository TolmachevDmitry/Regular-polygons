package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreatePicture extends JFrame {

    CreatePicture() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        Graphics2D gr = (Graphics2D) g;

        JPanel jPanel = new JPanel();
        this.add(jPanel);
        JButton jb = new JButton("Hello!");
        jPanel.add(jb);
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateLines l = new CreateLines();
                RegularPolygon p = new RegularPolygon();
                p.createPolygon(0, 0, 200, 6);

                for (int i = 1; i < p.getCount(); i++) {
                    l.print(p.getPointX(i), p.getPointY(i), p.getPointX(i - 1), p.getPointY(i - 1), g);

                    if (i == p.getCount() - 1) {
                        l.print(p.getPointX(i), p.getPointY(i), p.getPointX(0), p.getPointY(0), g);
                    }
                }

            }

        });

//        CreateLines l = new CreateLines();
//        RegularPolygon p = new RegularPolygon();
//        p.createPolygon(0, 0, 200, 6);
//
//        for (int i = 1; i < p.getCount(); i++) {
//            l.print(p.getPointX(i), p.getPointY(i), p.getPointX(i - 1), p.getPointY(i - 1), g);
//
//            if (i == p.getCount() - 1) {
//                l.print(p.getPointX(i), p.getPointY(i), p.getPointX(0), p.getPointY(0), g);
//            }
//        }

    }

}