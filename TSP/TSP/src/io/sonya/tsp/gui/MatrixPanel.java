package io.sonya.tsp.gui;

import io.sonya.tsp.alg.CostCell;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatrixPanel extends JPanel {

    private static final int SIZE = 48;

    public MatrixPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
    }

    public void drawMatrix(Integer[][] matrix) {
        drawCostMatrix(matrix, new ArrayList<>());
    }

    public void drawCostMatrix(Integer[][] original, List<CostCell> costs) {

        int length = original.length;
        Integer[][] matrix = Arrays.copyOf(original, length);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (int y = 0; y < length; y++) {
            JPanel row = new JPanel();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.setPreferredSize(new Dimension(length * SIZE, SIZE));
            panel.add(row);

            for (int x = 0; x < length; x++) {
                JTextField cell = new JTextField();
                cell.setMaximumSize(new Dimension(SIZE, SIZE));
                cell.setBorder(BorderFactory.createLineBorder(new Color(105, 113, 118), 1));
                cell.setEnabled(false);
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setMargin(new Insets(1, 1, 1, 1));
                cell.setDisabledTextColor(new Color(105, 113, 118));

                setCellBackground(matrix[y][x], cell);
                setCellText(cell, matrix[y][x], costs, x, y);

                row.add(cell);
            }
        }
        panel.setPreferredSize(new Dimension(length * SIZE, length * SIZE));
        add(panel, BorderLayout.CENTER);

        repaint();
        validate();
    }


    private void setCellText(JTextField cell, Integer cellValue, List<CostCell> costs, int x, int y) {
        if (cellValue == Integer.MAX_VALUE) {
            cell.setText("M");
        } else {
            if (cellValue == 0 && !costs.isEmpty()) {
                Integer cost = findCost(costs, y, x);
                cell.setText(cost == null ? Integer.toString(cellValue) : String.format("%d[%d]", cellValue, cost));
                if (cost != null) {
                    cell.setBackground(new Color(198, 233, 220));
                }
            } else {
                cell.setText(Integer.toString(cellValue));
            }
        }
    }

    private void setCellBackground(Integer matrix, JTextField cell) {
        if (matrix == Integer.MAX_VALUE) {
            cell.setBackground(new Color(239, 221, 224));
        } else if (matrix == 0) {
            cell.setBackground(new Color(255, 255, 231));
        } else {
            cell.setBackground(new Color(255, 255, 255));
        }
    }

    private Integer findCost(List<CostCell> costs, int y, int x) {
        for (CostCell cost : costs) {
            if (cost.getY() == y && cost.getX() == x) {
                return cost.getCost();
            }
        }
        return null;
    }
}
