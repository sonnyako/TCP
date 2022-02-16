package io.sonya.tsp.alg;

public class CostCell {

    private int y;
    private int x;
    private int cost;
    private int matrixSize;

    private CostCell(int y, int x, int cost) {
        this.y = y;
        this.x = x;
        this.cost = cost;
    }

    public static CostCell of(int y, int x, int cost) {
        return new CostCell(y, x, cost);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getCost() {
        return cost;
    }

    public int getMatrixSize() {
        return matrixSize;
    }

    public void setMatrixSize(int matrixSize) {
        this.matrixSize = matrixSize;
    }
}
