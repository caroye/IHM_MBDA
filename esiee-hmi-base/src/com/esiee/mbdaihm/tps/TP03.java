package com.esiee.mbdaihm.tps.solution;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import javax.swing.*;

/**
 * Use of Java2D API.
 */
public class TP03
{
    private final JFrame frame;

    private final DrawingPanel panel;

    public TP03()
    {
        frame = new JFrame("TP03");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new DrawingPanel();
    }

    private void layoutComponents()
    {
        frame.setLayout(new BorderLayout());
        frame.add(panel);
    }

    private void display()
    {
        frame.setBounds(0, 0, 800, 600);
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        TP03 tp = new TP03();

        tp.layoutComponents();

        SwingUtilities.invokeLater(() -> tp.display());
    }

    private static class DrawingPanel extends JPanel
    {
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            //*
            AffineTransform tr = g2d.getTransform();
            tr.translate(0, 400);

            tr.scale(0.5, -1);

            g2d.setTransform(tr);
//*/
            g2d.setPaint(Color.GREEN);
            g2d.fillRect(100, 100, 200, 100);

            g2d.setPaint(Color.RED);
            g2d.fillOval(300, 100, 100, 100);

            GeneralPath triangle = new GeneralPath();
            triangle.moveTo(200, 200);
            triangle.lineTo(100, 300);
            triangle.lineTo(300, 300);
            triangle.closePath();

            g2d.setPaint(Color.BLACK);
            g2d.draw(triangle);

            GeneralPath t = new GeneralPath();
            t.moveTo(400, 250);
            t.lineTo(550, 250);
            t.lineTo(550, 300);
            t.lineTo(500, 300);
            t.lineTo(500, 350);
            t.lineTo(450, 350);
            t.lineTo(450, 300);
            t.lineTo(400, 300);
            t.closePath();

            g2d.setPaint(Color.CYAN);
            g2d.fill(t);
        }

    }
}
