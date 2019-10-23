package ida;

import java.util.Scanner;

public class IterativeDeepeningAStar {

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        int numberOfElements = scanner.nextInt();
        int zeroIndex = scanner.nextInt();
        if(zeroIndex == -1) zeroIndex = numberOfElements;
//        int[] puzzle = new int[numberOfElements + 1];
//        initPuzzle(puzzle, numberOfElements, scanner);
        int boardSize = (int) Math.sqrt(numberOfElements + 1);
        int[][] board = new int[boardSize][boardSize];
        initBoard(board, boardSize, scanner);

        int[] rowCoordinates = IDAUtil.generateRowCoordinates(boardSize, numberOfElements);
        int[] colCoordinates = IDAUtil.generateColCoordinates(boardSize, numberOfElements);
        IDAUtil.fixCoordinates(rowCoordinates, colCoordinates, zeroIndex, boardSize);

        int h = IDAUtil.heuristicBoard(board, rowCoordinates, colCoordinates);

//        int[] coordinates = IDAUtil.generateCoordinates(numberOfElements, zeroIndex);
//        int h = IDAUtil.heuristicPuzzle(puzzle, coordinates);

        System.out.println("heuristic: " + h);
        System.out.println("solvable: " + IDAUtil.isSolvable(board));
    }

    private static void initBoard(final int[][] board, final int boardSize, final Scanner scanner) {
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                board[i][j] = scanner.nextInt();
            }
        }
    }

    private static void initPuzzle(final int[] puzzle, final int numberOfElements, final Scanner scanner) {
        for(int i = 0; i < numberOfElements + 1; i++) {
            puzzle[i] = scanner.nextInt();
        }
    }
}
