package io.sonya.tsp.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class ControlPanel extends JPanel {

    private int dimension = 0;

    public ControlPanel(ActionListener clickMatrixHandler, ActionListener clickActionHandler) {
        initialize(clickMatrixHandler, clickActionHandler);
    }

    private void initialize(ActionListener clickMatrixHandler, ActionListener clickActionHandler) {
        setBackground(new Color(248, 250, 251));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(206, 221, 231)));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.setBackground(new Color(248, 250, 251));

        createInput(inputPanel);
        createMatrixButton(inputPanel, clickMatrixHandler);
        createActionButton(inputPanel, clickActionHandler);

        add(inputPanel, BorderLayout.EAST);
    }

    private void createMatrixButton(JPanel inputPanel, ActionListener clickHandler) {
        JButton button = new JButton("Створити");
        button.setPreferredSize(new Dimension(100, 22));
        button.addActionListener(clickHandler);
        inputPanel.add(button);
    }

    private void createActionButton(JPanel inputPanel, ActionListener clickHandler) {
        JButton button = new JButton("Розпочати");
        button.addActionListener(clickHandler);
        inputPanel.add(button);
    }

    private void createInput(JPanel inputPanel) {
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);

        JTextField dimensionField = new JFormattedTextField(format);
        dimensionField.setToolTipText("Введіть розмірність матриці не більше 9");
        dimensionField.setColumns(5);
        dimensionField.setHorizontalAlignment(SwingConstants.LEFT);
        dimensionField.setMinimumSize(new Dimension(160, 32));

        InputVerifier verifier = new InputVerifier() {
            public boolean verify(JComponent input) {
                String text = dimensionField.getText();
                if (text.length() > 0 && text.matches("\\d+")  && (Integer.valueOf(text) <= 9 && Integer.valueOf(text) >= 2)) {
                    dimension = Integer.valueOf(text);
                    return true;
                } else {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        input,
                        "Введіть кількість точок від 2-го до 9-ти",
                        "Помилка!",
                        JOptionPane.ERROR_MESSAGE
                    ));
                    return false;
                }
            }
        };

        dimensionField.setInputVerifier(verifier);

        JLabel dimensionLabel = new JLabel("Виберіть кількість торгових точок:", JLabel.RIGHT);
        dimensionLabel.setLabelFor(dimensionField);
        dimensionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        dimensionLabel.setVerticalAlignment(SwingConstants.CENTER);
        dimensionLabel.setMinimumSize(new Dimension(160, 32));

        inputPanel.add(dimensionLabel);
        inputPanel.add(dimensionField);
    }

    public int getDimension() {
        return dimension;
    }
}
