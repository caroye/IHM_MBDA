package com.esiee.mbdaihm.tps.solution;

import javax.swing.*;
import java.awt.*;

/**
 * First experiments with Swing components and Layouts.
 */
public class TP01
{
    private final JFrame frame;

    private final JPanel topPanel;

    private final JPanel bottomPanel;

    private final JLabel centerLabel;

    private final JButton[] buttons;

    private final JLabel bottomLabel;

    /**
     * Create a new TP01 instance. Initialise
     */
    public TP01()
    {
        frame = new JFrame("TP 01");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        topPanel = new JPanel();

        bottomPanel = new JPanel();

        centerLabel = new JLabel("This Label is at screen center", JLabel.CENTER);

        buttons = new JButton[]
        {
            new JButton("B1"),
            new JButton("B2")
        };

        bottomLabel = new JLabel("Clock place?");
    }

    private void layComponentsOut()
    {
        frame.setLayout(new BorderLayout());

        frame.add(centerLabel, BorderLayout.CENTER);

        topPanel.add(buttons[0]);
        topPanel.add(buttons[1]);
        frame.add(topPanel, BorderLayout.NORTH);

        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(bottomLabel);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void display()
    {
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Class wide main method.
     *
     * @param args the command line arguments, not used
     */
    public static void main(String[] args)
    {
        TP01 tp = new TP01();

        tp.layComponentsOut();

        SwingUtilities.invokeLater(() -> tp.display());

    }

}
