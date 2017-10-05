package com.esiee.mbdaihm.tps.solution;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Use of listeners in Swing.
 */
public class TP02
{
    private final JFrame frame;

    private final ArrayListListModel listModel;

    private final JList<String> list;

    private final JScrollPane listPane;

    private final JButton addButton;

    private final JTextField entryTextField;

    private final JLabel selectonLabel;

    private final JTextField selectedTextField;

    public TP02()
    {
        frame = new JFrame("TP 02");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        entryTextField = new JTextField(8);

        addButton = new JButton("Add value");

        listModel = new ArrayListListModel();

        list = new JList<>(listModel);

        list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listPane = new JScrollPane(list);

        selectonLabel = new JLabel("Selected value :");

        selectedTextField = new JTextField(8);
    }

    private void initListeners()
    {
        addButton.addActionListener(e ->
                {
                    String text = entryTextField.getText();
                    if (text == null || "".equals(text))
                    {
                    }
                    else
                    {
                        listModel.add(text);
                    }
                });

        list.addListSelectionListener(e -> selectedTextField.setText(list.getSelectedValue()));
    }

    private void layComponentsOut()
    {
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        listPane.setPreferredSize(new Dimension(160, 160));

        frame.add(entryTextField);
        frame.add(addButton);
        frame.add(listPane);
        frame.add(selectonLabel);
        frame.add(selectedTextField);
    }

    private void display()
    {
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        TP02 tp = new TP02();

        tp.initListeners();

        tp.layComponentsOut();

        SwingUtilities.invokeLater(() -> tp.display());
    }

    private static class ArrayListListModel extends AbstractListModel<String>
    {

        private final ArrayList<String> content = new ArrayList<>();

        public void add(String value)
        {
            content.add(value);
            fireContentsChanged(this, 0, content.size());
        }

        @Override
        public int getSize()
        {
            return content.size();
        }

        @Override
        public String getElementAt(int index)
        {
            return content.get(index);
        }
    }
}
