package io.sonya.tsp.gui;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Arrays;

public class FormPanel extends JPanel {
    private static final int SIZE = 32;

    private Integer[][] cityMatrix;
    private JPanel formTable;

    private final InputVerifier verifier = new InputVerifier() {
        public boolean verify(JComponent input) {
            String text = ((JTextField) input).getText();
            if (text.length() > 0 && text.matches("\\d+")) {
                String[] tokens = input.getName().split(",");
                int cost = Integer.parseInt(text, 10);
                int xx = Integer.parseInt(tokens[0], 10);
                int yy = Integer.parseInt(tokens[1], 10);

                cityMatrix[yy][xx] = cost;
                return true;
            } else {
                return false;
            }
        }
    };

    public FormPanel() {
        setBackground(new Color(255, 255, 255));
        initialize(0);
    }

    public Integer[][] getCityMatrix() {
        Integer[][] matrix = Arrays.copyOf(cityMatrix, cityMatrix.length);
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                if (x == y) {
                    matrix[y][x] = Integer.MAX_VALUE;
                }
                if (matrix[y][x] == null) {
                    matrix[y][x] = 0;
                }
            }
        }
        return cityMatrix;
    }

    public void initialize(int dimension) {

        if (dimension < 2) {
            return;
        }
        if (formTable != null) {
            remove(formTable);
        }

        cityMatrix = new Integer[dimension][dimension];

        formTable = new JPanel();
        formTable.setLayout(new BoxLayout(formTable, BoxLayout.Y_AXIS));
        formTable.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(206, 221, 231)));
        formTable.setBackground(new Color(248, 250, 251));
        formTable.setMaximumSize(new Dimension(getWidth() / 2, getHeight()));
        add(formTable, BorderLayout.WEST);

        for (int y = 0; y < dimension; y++) {
            JPanel row = new JPanel();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.setPreferredSize(new Dimension(dimension * SIZE, SIZE));
            formTable.add(row);

            for (int x = 0; x < dimension; x++) {
                NumberFormat format = NumberFormat.getIntegerInstance();
                format.setGroupingUsed(false);

                JTextField cell = new JFormattedTextField(format);
                cell.setMaximumSize(new Dimension(SIZE, SIZE));
                cell.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, new Color(206, 221, 231)));
                cell.setBackground(new Color(255, 255, 255));
                cell.setName(String.format("%d,%d", x, y));
                cell.setDisabledTextColor(new Color(177, 37, 10));
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setMargin(new Insets(1, 1, 1, 1));
                if (x == y) {
                    cell.setEnabled(false);
                    cell.setText("M");
                } else {
                    cell.setText("0");
                    cell.setInputVerifier(verifier);
                }
                row.add(cell);
            }
        }

        this.validate();
        this.repaint();
    }
}
