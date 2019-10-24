package ida;

import java.util.List;
import java.util.Scanner;

public class IterativeDeepeningAStar {

    private static int zeroRow = 0;
    private static int zeroCol = 0;

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final IDAUtil idaUtil = new IDAUtil();

        int numberOfElements = scanner.nextInt();
        int zeroIndex = scanner.nextInt();
        if(zeroIndex == -1) zeroIndex = numberOfElements;

        int boardSize = (int) Math.sqrt(numberOfElements + 1);
        int[][] rootBoard = new int[boardSize][boardSize];
        initBoard(rootBoard, boardSize, scanner);

        int[] rowCoordinates = idaUtil.generateRowCoordinates(boardSize, numberOfElements);
        int[] colCoordinates = idaUtil.generateColCoordinates(boardSize, numberOfElements);
        idaUtil.fixCoordinates(rowCoordinates, colCoordinates, zeroIndex, boardSize);

        int rootHeuristic = idaUtil.heuristicBoard(rootBoard, rowCoordinates, colCoordinates);

        final int[][] goalBoard = idaUtil.generateGoalBoard(boardSize, rowCoordinates, colCoordinates);

        idaUtil.runIDAStar(rootBoard, zeroRow, zeroCol, rootHeuristic, goalBoard, zeroIndex);
    }

    private static void initBoard(final int[][] board, final int boardSize, final Scanner scanner) {
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                board[i][j] = scanner.nextInt();
                if(board[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }
    }
}
