package io.sonya.tsp.gui;

import io.sonya.tsp.alg.CostCell;
import io.sonya.tsp.alg.TspAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TspWindow extends JFrame {

    private ControlPanel controlPanel;
    private FormPanel formPanel;
    private MatrixPanel matrixPanel;

    Integer[][] originalCityMatrix;
    private List<CostCell> path = new LinkedList<>();

    public TspWindow() {
        initialize();
    }

    private void initialize() {
        setResizable(true);
        setTitle("Вирішення проблеми комівояжера");
        setBackground(new Color(255, 255, 255));
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(640, 480));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        controlPanel = new ControlPanel(this::clickMatrixHandler, this::clickActionHandler);
        matrixPanel = new MatrixPanel();

        formPanel = new FormPanel();
        formPanel.setLayout(new BorderLayout(0, 0));
        formPanel.add(matrixPanel, BorderLayout.CENTER);

        add(controlPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        pack();
    }

    public static void main(String[] args) {
        //create a window and set it to be visible
        EventQueue.invokeLater(() -> {
            try {
                TspWindow win = new TspWindow();
                win.setVisible(true);
            } catch (Exception ex) {
                System.err.println("Виникла помилка при створенні вікна: " + ex.getMessage());
            }
        });
    }

    private void clickMatrixHandler(ActionEvent event) {
        if (event != null) {
            formPanel.initialize(controlPanel.getDimension());
        }
    }

    private void clickActionHandler(ActionEvent event) {
        if (event != null) {
            Integer[][] cityMatrix = formPanel.getCityMatrix();
            originalCityMatrix = Arrays.copyOf(cityMatrix, cityMatrix.length);
            iterateOverMatrix(cityMatrix);
        }
    }

    public void iterateOverMatrix(Integer[][] cityMatrix) {
        if (cityMatrix.length == 1) {
            System.out.println("Розрахунок закінчено");
            showResult();
            return;
        }

        System.out.format("Вихідна матриця %dx%d%n", cityMatrix.length, cityMatrix.length);
        TspAlgorithm algorithm = new TspAlgorithm(cityMatrix);

        System.out.println("Пошук мінімума і редукція значень клітинок відповідно до знайденого значення");
        Integer[][] matrix = algorithm.searchAndReduce();
        matrixPanel.drawMatrix(matrix);

        System.out.println("Вираховуєм оцінки нульовик клітинок");
        List<CostCell> costs = algorithm.findCosts(matrix);
        matrixPanel.drawCostMatrix(matrix, costs);

        System.out.println("Проводим оцінку і видалення рядка та стовпчика");
        Integer[][] newMatrix = algorithm.shrinkMatrix(matrix, costs);

        System.out.println("Зберігаєм оцінку як пройдене місто в шляху комівояжера");
        path.add(algorithm.getMaxCost());


        if (newMatrix.length > 0) {
            System.out.println("Проводимо пошук по новій матриці");
            iterateOverMatrix(newMatrix);
        }
    }

    private void showResult() {
        JPanel result = new JPanel();
        result.setBackground(new Color(248, 250, 251));
        result.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(206, 221, 231)));

        JLabel pathLabel  = new JLabel();
        StringBuilder builder = new StringBuilder("<html><b>Вирішення задачі: </b>");
        for (int i = 0; i < path.size(); i++) {
            CostCell costCell = path.get(i);
            builder.append(String.format("<b>%d</b><sup>[%dx%d](%d,%d)</sup>",
                costCell.getCost(),
                costCell.getMatrixSize(),
                costCell.getMatrixSize(),
                costCell.getY(),
                costCell.getX()
            ));
            if (i < path.size() - 1) {
                builder.append("→");
            }
        }
        builder.append("</html>");
        pathLabel.setText(builder.toString());
        result.add(pathLabel);
        add(result, BorderLayout.SOUTH);
        validate();
    }
}
