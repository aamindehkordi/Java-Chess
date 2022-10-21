package com.chess.engine.board;

public class BoardUtils {


    public static final boolean[] FIRST_COLUMN = initCol(0);
    public static final boolean[] SECOND_COLUMN = initCol(1);
    public static final boolean[] SEVENTH_COLUMN = initCol(6);
    public static final boolean[] EIGHTH_COLUMN = initCol(7);

    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;
    /**
     * Sets tiles in a column to true.
     * @param colNum the column number
     * @return boolean array
     */
    private static boolean[] initCol(int colNum) {
        final boolean[] column = new boolean[NUM_TILES];
        do {
            column[colNum] = true;
            colNum += NUM_TILES_PER_ROW;
        } while (colNum < NUM_TILES);
        return column;
    }

    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate me!");
    }

    public static boolean isValidTileCoordinate(final int coord) {
        return coord >= 0 && coord < NUM_TILES;
    }


}
