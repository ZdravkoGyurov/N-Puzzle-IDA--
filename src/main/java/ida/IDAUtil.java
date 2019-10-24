package ida;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

final public class IDAUtil {

    private static int[] rowCoordinates;
    private static int[] colCoordinates;
    private static Node goal;
    private static final int FOUND = -1;
    private static Node reachedGoal;

    private IDAUtil() {
        // Util class
    }

    public static int heuristicBoard(final int[][] board, final int[] rowCoordinates, final int[] colCoordinates) {
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

    public static boolean isSolvable(final int[][] board) {
        int[] puzzle = new int[board.length * board.length];
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

    public static int[] generateRowCoordinates(final int boardSize, final int numberOfElements) {
        int[] rowCoordinates = new int[numberOfElements];
        for(int i = 0; i < numberOfElements; i++) {
            rowCoordinates[i] = i / boardSize;
        }
        IDAUtil.rowCoordinates = rowCoordinates;
        return rowCoordinates;
    }

    public static int[] generateColCoordinates(final int boardSize, final int numberOfElements) {
        int[] colCoordinates = new int[numberOfElements];
        for(int i = 0; i < numberOfElements; i++) {
            colCoordinates[i] = i % boardSize;
        }
        IDAUtil.colCoordinates = colCoordinates;
        return colCoordinates;
    }

    public static void fixCoordinates(final int[] rowCoordinates, final int[] colCoordinates, final int zeroIndex, final int boardSize) {
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

    public static int[][] generateGoalBoard(final int boardSize, final int[] rowCoordinates, final int[] colCoordinates) {
        int[][] goalBoard = new int[boardSize][boardSize];

        for(int i = 0; i < rowCoordinates.length; i++) {
            goalBoard[rowCoordinates[i]][colCoordinates[i]] = i + 1;
        }

        return goalBoard;
    }

    public static void runIDAStar(final int[][] initBoard, final int zeroRow, final int zeroCol, final int rootHeuristic, final int[][] goalBoard, final int zeroIndex) {
        final Node root = new Node(initBoard, zeroRow, zeroCol, rootHeuristic, null);
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

    private static int search(final Node node, final int g, final int threshold) {
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

    private static void printPath() {
        Node iter = reachedGoal;
        while(iter.getParent() != null) {
            printBoard(iter.getBoardSnapshot());
            iter = iter.getParent();
        }
    }

    private static void printBoard(final int[][] board) {
        System.out.println("----------");
        for(int[] boardRow : board) {
            for(int i : boardRow) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
        System.out.println("----------");
    }

    private static List<Node> nextNodes(final Node node) {
        List<Node> nextNodes = new LinkedList<>();

        int oldRow = node.getZeroRow(), oldCol = node.getZeroCol();

        if(node.getZeroRow() != 0) { // can move up
            nextNodes.add(movedZeroNode(node, oldRow, oldCol, oldRow - 1, oldCol)); // UP
        }
        if(node.getZeroRow() != node.getBoardSnapshot().length - 1) { // can move down
            nextNodes.add(movedZeroNode(node, oldRow, oldCol, oldRow + 1, oldCol)); // DOWN
        }
        if(node.getZeroCol() != 0) { // can move left
            nextNodes.add(movedZeroNode(node, oldRow, oldCol, oldRow, oldCol - 1)); // LEFT
        }
        if(node.getZeroCol() != node.getBoardSnapshot().length - 1) { // can move right
            nextNodes.add(movedZeroNode(node, oldRow, oldCol, oldRow, oldCol + 1)); // RIGHT
        }

        return nextNodes;
    }

    private static Node movedZeroNode(final Node node, final int oldRow, final int oldCol, final int newRow, final int newCol) {
        final int[][] boardAfterMove = generateBoardCopy(node.getBoardSnapshot());
        swapZero(boardAfterMove, oldRow, oldCol, newRow, newCol);
        final int h = heuristicBoard(boardAfterMove, rowCoordinates, colCoordinates);
        return new Node(boardAfterMove, newRow, newCol, h, node);
    }

    private static int[][] generateBoardCopy(final int[][] board) {
        return Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
    }

    private static void swapZero(final int[][] board, final int oldRow, final int oldCol, final int newRow, final int newCol) {
        int temp = board[oldRow][oldCol];
        board[oldRow][oldCol] = board[newRow][newCol];
        board[newRow][newCol] = temp;
    }
}
