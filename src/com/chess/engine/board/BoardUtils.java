package com.chess.engine.board;

public class BoardUtils {


    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;
    public static final boolean[] FIRST_COLUMN = initCol(0);
    public static final boolean[] SECOND_COLUMN = initCol(1);
    public static final boolean[] SEVENTH_COLUMN = initCol(6);
    public static final boolean[] EIGHTH_COLUMN = initCol(7);
    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] SEVENTH_ROW = initRow(48);

    /**
     * This method is used to initialize the columns of the board.
     * @param colNum the column number
     * @return boolean array
     */
    private static boolean[] initCol(int colNum) {
        final boolean[] column = new boolean[NUM_TILES];
        /*
         * The following loop is used to initialize the columns of the board.
         * The loop starts at the column number and increments by 8.
         * The loop ends when the column number is greater than 63.
         */
        do {
            column[colNum] = true;
            colNum += NUM_TILES_PER_ROW;
        } while (colNum < NUM_TILES);
        return column;
    }

    /**
     * This method is used to initialize the rows of the board.
     * @param rowNum the row number
     * @return boolean array
     */
    private static boolean[] initRow(int rowNum) {
        final boolean[] row = new boolean[NUM_TILES];
        /*
         * The following loop is used to initialize the rows of the board.
         * The loop starts at the row number and increments by 1.
         * The loop ends when the row number is greater than 7.
         */
        do {
            row[rowNum] = true;
            rowNum++;
        } while (rowNum % NUM_TILES_PER_ROW != 0);
        return row;
    }

    /* Constructor
     *
     * DO NOT CONSTRUCT AN INSTANCE OF THIS CLASS
     *
     * BoardUtils is used to store constants and static methods
     * and should not be instantiated.
     */
    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate me!");
    }

    /**
     * This method is used to check if the given coordinate is valid.
     * @param coord the coordinate
     * @return true if the coordinate is valid, false otherwise
     */
    public static boolean isValidTileCoordinate(final int coord) {
        return coord >= 0 && coord < NUM_TILES;
    }


}
