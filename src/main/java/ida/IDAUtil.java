package ida;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

final public class IDAUtil {

    private int[] rowCoordinates;
    private int[] colCoordinates;
    private Node root;
    private Node goal;
    private static final int FOUND = -1;
    private Node reachedGoal;

    public IDAUtil() {
        // Util class
    }

    public int heuristicBoard(final int[][] board, final int[] rowCoordinates, final int[] colCoordinates) {
        int result = 0;
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                int currentElement = board[i][j];
                if(currentElement != 0 && currentElement <= board.length * board.length - 1) {
                    result += Math.abs(i - rowCoordinates[currentElement - 1]) +
                            Math.abs(j - colCoordinates[currentElement - 1]);
                }
            }
        }
        return result;
    }

    public boolean isSolvable(final int[][] board) {
        int puzzle[] = new int[board.length * board.length];
        int inversionCount = 0;
        int index = 0;
        int zeroRow = board.length - 1;

        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                if(board[i][j] == 0) {
                    zeroRow = board.length - i;
                }
                puzzle[index++] = board[i][j];
            }
        }

        for(int i = 0; i < puzzle.length - 1; i++) {
            if(puzzle[i] != 0) {
                for(int j = i + 1; j < puzzle.length; j++) {
                    if(puzzle[i] > puzzle[j] && puzzle[j] != 0) {
                        inversionCount++;
                    }
                }
            }
        }

        boolean oddGridWidth = board.length % 2 == 1;
        boolean evenInversions = inversionCount % 2 == 0;
        boolean evenZeroRow = zeroRow % 2 == 0;

        if(oddGridWidth && evenInversions) {
            return true;
        } else if(!oddGridWidth && evenZeroRow && !evenInversions) {
            return true;
        } else if(!oddGridWidth && !evenZeroRow && evenInversions) {
            return true;
        } else {
            return false;
        }
    }

    public int[] generateRowCoordinates(final int boardSize, final int numberOfElements) {
        int[] rowCoordinates = new int[numberOfElements];
        for(int i = 0; i < numberOfElements; i++) {
            rowCoordinates[i] = i / boardSize;
        }
        this.rowCoordinates = rowCoordinates;
        return rowCoordinates;
    }

    public int[] generateColCoordinates(final int boardSize, final int numberOfElements) {
        int[] colCoordinates = new int[numberOfElements];
        for(int i = 0; i < numberOfElements; i++) {
            colCoordinates[i] = i % boardSize;
        }
        this.colCoordinates = colCoordinates;
        return colCoordinates;
    }

    public void fixCoordinates(final int[] rowCoordinates, final int[] colCoordinates, final int zeroIndex, final int boardSize) {
        for(int i = 0; i < rowCoordinates.length; i++) {
            if(i >= zeroIndex) {
                colCoordinates[i]++;
                if(colCoordinates[i] == boardSize) {
                    colCoordinates[i] = 0;
                    rowCoordinates[i]++;
                }
            }
        }
    }

    public int[][] generateGoalBoard(final int boardSize, final int[] rowCoordinates, final int[] colCoordinates) {
        int[][] goalBoard = new int[boardSize][boardSize];

        for(int i = 0; i < rowCoordinates.length; i++) {
            goalBoard[rowCoordinates[i]][colCoordinates[i]] = i + 1;
        }

        return goalBoard;
    }

    public void runIDAStar(final int[][] initBoard, final int zeroRow, final int zeroCol, final int rootHeuristic, final int[][] goalBoard, final int zeroIndex) {
        root = new Node(initBoard, zeroRow, zeroCol, rootHeuristic, null);
        final int goalZeroRow = zeroIndex / goalBoard.length, goalZeroCol = zeroIndex / goalBoard.length;
        goal = new Node(goalBoard, goalZeroRow, goalZeroCol, 0, null);

        int threshold = rootHeuristic;

        while(true) {
            int temp = search(root, 0, threshold);

            if(temp == FOUND) {
                System.out.println("FOUND");
                printPath();
                return;
            }
            if(temp >= 100) { // TODO ???
                System.out.println("NOT FOUND");
                return;
            }
            threshold = temp;
        }
    }

    private int search(final Node node, final int g, final int threshold) {
        final int f = g + heuristicBoard(node.getBoardSnapshot(), rowCoordinates, colCoordinates);

        if(f > threshold) {
            return f;
        }
        if(node.equals(goal)) {
            reachedGoal = node;
            return FOUND;
        }

        int min = Integer.MAX_VALUE;

        for(final Node tempnode : nextNodes(node)) {
            int temp = search(tempnode, g + 1, threshold);
            if(temp == FOUND) {
                return FOUND;
            }
            if(temp < min) {
                min = temp;
            }
        }
        return min;
    }

    private void printPath() {
        Node iter = reachedGoal;
        while(iter.getParent() != null) {
            printBoard(iter.getBoardSnapshot());
            iter = iter.getParent();
        }
    }

    private void printBoard(final int[][] board) {
        System.out.println("----------");
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("----------");
    }

    private List<Node> nextNodes(final Node node) {
        List<Node> nextNodes = new LinkedList<>();

        int oldRow = node.getZeroRow(), oldCol = node.getZeroCol();

        if(node.getZeroRow() != 0) { // can move up
            int[][] boardU = generateBoardCopy(node.getBoardSnapshot());
            int newRow = node.getZeroRow() - 1, newCol = node.getZeroCol();
            swapZero(boardU, oldRow, oldCol, newRow, newCol);
            int h = heuristicBoard(boardU, rowCoordinates, colCoordinates);
            nextNodes.add(new Node(boardU, newRow, newCol, h, node));
        }
        if(node.getZeroRow() != node.getBoardSnapshot().length - 1) { // can move down
            int[][] boardD = generateBoardCopy(node.getBoardSnapshot());
            int newRow = node.getZeroRow() + 1, newCol = node.getZeroCol();
            swapZero(boardD, oldRow, oldCol, newRow, newCol);
            int h = heuristicBoard(boardD, rowCoordinates, colCoordinates);
            nextNodes.add(new Node(boardD, newRow, newCol, h, node));
        }
        if(node.getZeroCol() != 0) { // can move left
            int[][] boardL = generateBoardCopy(node.getBoardSnapshot());
            int newRow = node.getZeroRow(), newCol = node.getZeroCol() - 1;
            swapZero(boardL, oldRow, oldCol, newRow, newCol);
            int h = heuristicBoard(boardL, rowCoordinates, colCoordinates);
            nextNodes.add(new Node(boardL, newRow, newCol, h, node));
        }
        if(node.getZeroCol() != node.getBoardSnapshot().length - 1) { // can move right
            int[][] boardR = generateBoardCopy(node.getBoardSnapshot());
            int newRow = node.getZeroRow(), newCol = node.getZeroCol() + 1;
            swapZero(boardR, oldRow, oldCol, newRow, newCol);
            int h = heuristicBoard(boardR, rowCoordinates, colCoordinates);
            nextNodes.add(new Node(boardR, newRow, newCol, h, node));
        }

        return nextNodes;
    }

    private int[][] generateBoardCopy(final int[][] board) {
        return Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
    }

    private void swapZero(final int[][] board, final int oldRow, final int oldCol, final int newRow, final int newCol) {
        int temp = board[oldRow][oldCol];
        board[oldRow][oldCol] = board[newRow][newCol];
        board[newRow][newCol] = temp;
    }
}
