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

    public int getZeroRow() {
        return zeroRow;
    }

    public int getZeroCol() {
        return zeroCol;
    }

    public Node getParent() {
        return parent;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Node node = (Node) o;

        for(int i = 0; i < this.boardSnapshot.length; i++) {
            for(int j = 0; j < this.boardSnapshot.length; j++) {
                if(this.boardSnapshot[i][j] != node.getBoardSnapshot()[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(boardSnapshot);
    }
}
