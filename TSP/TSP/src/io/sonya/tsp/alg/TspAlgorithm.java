package io.sonya.tsp.alg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TspAlgorithm {

    private Integer[][] originalMatrix;
    private CostCell maxCost;

    public TspAlgorithm(Integer[][] matrix) {
        this.originalMatrix = matrix;
    }

    public CostCell getMaxCost() {
        return maxCost;
    }

    public Integer[][] searchAndReduce() {
        System.out.println("Оригінальна матриця");
        System.out.println(debug(originalMatrix));

        searchMinValueAndReduceCellsInRows(originalMatrix);
        System.out.println("Матриця з вичисленими мінімумами по рядках");
        System.out.println(debug(originalMatrix));

        searchMinValueAndReduceCellsInRows(transposeMatrix(originalMatrix));
        System.out.println("Value Reduced In Columns");
        // back to normal;
        originalMatrix = transposeMatrix(originalMatrix);
        System.out.println(debug(originalMatrix));

        return originalMatrix;
    }

    // ми завжди працюєм з рядками
    private void searchMinValueAndReduceCellsInRows(Integer[][] matrix) {
        for (Integer[] row : matrix) {
            // копіюєм рядок щоб не змінити оригінальну матрицю
            Integer[] temp = Arrays.copyOf(row, matrix.length);

            // сортуєм щоб знайти найменше
            Arrays.sort(temp, Comparator.nullsLast(Integer::compareTo));

            // проводим редукцію по найменшому значенні
            reduceCellValue(row, temp[0]);
        }
    }

    // повертаємо матрицю, для того щоб використаті ті самі методи для колонок і рядків
    private Integer[][] transposeMatrix(Integer[][] matrix) {
        Integer[][] transposed = Arrays.copyOf(matrix, matrix.length);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                int cell = transposed[i][j];
                transposed[i][j] = transposed[j][i];
                transposed[j][i] = cell;
            }
        }
        return transposed;
    }

    private void reduceCellValue(Integer[] cells, Integer minValue) {
        for (int i = 0; i < cells.length; i++) {
            // якщо це max значення це пересічення пропускаємо його
            if (cells[i] == Integer.MAX_VALUE) {
                continue;
            }
            // інакше робимо редукцію значення
            cells[i] -= minValue;
        }
    }

    public Integer[][] shrinkMatrix(Integer[][] matrix, List<CostCell> costs) {
        // максимальна оцінка вже містить необхідні кординати
        // знаходим max оцінку
        maxCost = findMaxCost(costs);
        maxCost.setMatrixSize(matrix.length);

        // пошук мертвих точок;
        int my = maxCost.getX();
        int mx = maxCost.getY();
        if (my < matrix.length && mx < matrix.length) {
            matrix[my][mx] = Integer.MAX_VALUE;
        }

        // ми завжди видаляєм 1 рядок і 1 колонку того довжина матриці -= 1
        int length = matrix.length - 1;
        Integer[][] newMatrix = new Integer[length][length];

        // курсор рядка
        int newY = 0;
        for (int y = 0; y < matrix.length; y++) {
            if (y != maxCost.getY()) {
                Integer[] row = matrix[y];

                // курсор колонки
                int newX = 0;
                for (int x = 0; x < row.length; x++) {
                    if (x != maxCost.getX()) {
                        newMatrix[newY][newX] = row[x];
                        // обновлюєм курсор рядка
                        newX++;
                    }
                }
                // обновлюєм курсор колонки
                newY++;
            }
        }

        // нова матриця збудована
        return newMatrix;
    }

    private CostCell findMaxCost(List<CostCell> costs) {
        if (costs.isEmpty()) {
            // якщо список пустий повертаєм кост першої клітинки
            return CostCell.of(0, 0, 0);
        }
        costs.sort(Comparator.comparing(CostCell::getCost).reversed());
        return costs.get(0);
    }

    public List<CostCell> findCosts(Integer[][] matrix) {

        // нам потрібно зберігти проміжні оцінки і координати
        // ми не знаєм наперед скільки їх буде
        // ми використовуєм список для цього а не фіксований масив
        List<CostCell> costs = new ArrayList<>();


        for (int y = 0; y < matrix.length; y++) {
            Integer[] row = matrix[y];
            for (int x = 0; x < row.length; x++) {
                if (row[x] == 0) {

                    // ми знайшли нульову клітинку
                    // треба її оцінити
                    int cost = calculateCostForZeroCell(matrix, y, x);
                    System.out.format("Знайдено оцінки %dx%d = %d%n", y, x, cost);

                    // записуєм її оцінку і координати
                    costs.add(CostCell.of(y, x, cost));
                }
            }
        }

        // повертаєм список оцінок
        return costs;
    }

    private int calculateCostForZeroCell(Integer[][] matrix, int y, int x) {
        Integer[] row = Arrays.copyOf(matrix[y], matrix.length);


        Integer[] column = new Integer[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            column[i] = matrix[i][x];
        }
        // позначаєм координати як max значення щоб потім їх виключити з оцінок
        // тому що це координати клітинки яку ми оцінюєм
        row[x] = Integer.MAX_VALUE;
        column[y] = Integer.MAX_VALUE;

        // сортуєм щоб знайти мінімальне

        Arrays.sort(row, Comparator.nullsLast(Integer::compareTo));
        Arrays.sort(column, Comparator.nullsLast(Integer::compareTo));

        // пропускаєм значення клітинки по координаті
        if (row[0] == Integer.MAX_VALUE || column[0] == Integer.MAX_VALUE) {
            return 0;
        } else {
            return row[0] + column[0];
        }
    }


    private String debug(Integer[][] matrix) {
        // перетворюєм в потік даних
        return Stream.of(matrix)
                        // переводим число в стрінг, якщо це пересічення (Integer.MAX_VALUE) заміняєм на М
            .map(row -> Stream.of(row).map(cell -> cell == Integer.MAX_VALUE
                ? "M"
                : Integer.toString(cell)).collect(Collectors.joining(", "))
            )
            .collect(Collectors.joining("\n"));
    }
}
