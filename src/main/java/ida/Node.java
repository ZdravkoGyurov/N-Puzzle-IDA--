package ida;

import java.util.Arrays;

public class Node {
    private int[][] boardSnapshot;
    private int heuristic;
    private Node parent;
    private int zeroRow;
    private int zeroCol;

    public Node(final int[][] boardSnapshot, final int zeroRow, final int zeroCol, final int heuristic, final Node parent) {
        this.boardSnapshot = boardSnapshot;
        this.zeroRow = zeroRow;
        this.zeroCol = zeroCol;
        this.heuristic = heuristic;
        this.parent = parent;
    }

    public int[][] getBoardSnapshot() {
        return boardSnapshot;
    }

    public void setBoardSnapshot(final int[][] boardSnapshot) {
        this.boardSnapshot = boardSnapshot;
    }

    public int getZeroRow() {
        return zeroRow;
    }

    public void setZeroRow(int zeroRow) {
        this.zeroRow = zeroRow;
    }

    public int getZeroCol() {
        return zeroCol;
    }

    public void setZeroCol(int zeroCol) {
        this.zeroCol = zeroCol;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(final int heuristic) {
        this.heuristic = heuristic;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(final Node parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Arrays.equals(boardSnapshot, node.boardSnapshot);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(boardSnapshot);
    }
}
