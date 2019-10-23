package ida;

final public class IDAUtil {

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

    public static int heuristicPuzzle(final int[] puzzle, final int[] coordinates) {
        int result = 0;
        for(int i = 0; i < puzzle.length; i++) {
            int currentElement = puzzle[i];
            if(currentElement != 0 && currentElement <= puzzle.length - 1) {
                result += Math.abs(i - coordinates[currentElement]);
            }
        }
        return result;
    }

    public static int[] generateCoordinates(final int numberOfElements, int zeroIndex) {
        int[] coordinates = new int[numberOfElements + 1];

        coordinates[0] = zeroIndex;

        for(int i = 1; i < numberOfElements + 1; i++) {
            if(i <= zeroIndex) {
                coordinates[i] = i - 1;
            } else {
                coordinates[i] = i;
            }
        }

        return coordinates;
    }

    public static boolean isSolvable(final int[][] board) {
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

    public static int[] generateRowCoordinates(final int boardSize, final int numberOfElements) {
        int[] rowCoordinates = new int[numberOfElements];
        for(int i = 0; i < numberOfElements; i++) {
            rowCoordinates[i] = i / boardSize;
        }
        return rowCoordinates;
    }

    public static int[] generateColCoordinates(final int boardSize, final int numberOfElements) {
        int[] colCoordinates = new int[numberOfElements];
        for(int i = 0; i < numberOfElements; i++) {
            colCoordinates[i] = i % boardSize;
        }
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
            System.out.println(rowCoordinates[i] + " " + colCoordinates[i]);
        }
    }

    public static boolean validPosition(final int row, final int col, final int boardSize) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
    }
}
