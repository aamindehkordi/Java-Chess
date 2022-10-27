package com.chess.engine.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BoardUtils {


    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;
    /* ROWS */
    public static final boolean[] FIRST_ROW = initRow(0);
    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] THIRD_ROW = initRow(16);
    public static final boolean[] FOURTH_ROW = initRow(24);
    public static final boolean[] FIFTH_ROW = initRow(32);
    public static final boolean[] SIXTH_ROW = initRow(40);
    public static final boolean[] SEVENTH_ROW = initRow(48);
    public static final boolean[] EIGHTH_ROW = initRow(56);

    /* COLUMNS */
    public static final boolean[] FIRST_COLUMN = initCol(0);
    public static final boolean[] SECOND_COLUMN = initCol(1);
    public static final boolean[] THIRD_COLUMN = initCol(2);
    public static final boolean[] FOURTH_COLUMN = initCol(3);
    public static final boolean[] FIFTH_COLUMN = initCol(4);
    public static final boolean[] SIXTH_COLUMN = initCol(5);
    public static final boolean[] SEVENTH_COLUMN = initCol(6);
    public static final boolean[] EIGHTH_COLUMN = initCol(7);

    public static final Map<String, Integer> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            positionToCoordinate.put(String.valueOf(ALGEBRAIC_NOTATION.get(i)), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }


    private static Map<String, Integer> initializeAlgebraicNotation() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        {
            positionToCoordinate.put("a8", 0);
            positionToCoordinate.put("b8", 1);
            positionToCoordinate.put("c8", 2);
            positionToCoordinate.put("d8", 3);
            positionToCoordinate.put("e8", 4);
            positionToCoordinate.put("f8", 5);
            positionToCoordinate.put("g8", 6);
            positionToCoordinate.put("h8", 7);
            positionToCoordinate.put("a7", 8);
            positionToCoordinate.put("b7", 9);
            positionToCoordinate.put("c7", 10);
            positionToCoordinate.put("d7", 11);
            positionToCoordinate.put("e7", 12);
            positionToCoordinate.put("f7", 13);
            positionToCoordinate.put("g7", 14);
            positionToCoordinate.put("h7", 15);
            positionToCoordinate.put("a6", 16);
            positionToCoordinate.put("b6", 17);
            positionToCoordinate.put("c6", 18);
            positionToCoordinate.put("d6", 19);
            positionToCoordinate.put("e6", 20);
            positionToCoordinate.put("f6", 21);
            positionToCoordinate.put("g6", 22);
            positionToCoordinate.put("h6", 23);
            positionToCoordinate.put("a5", 24);
            positionToCoordinate.put("b5", 25);
            positionToCoordinate.put("c5", 26);
            positionToCoordinate.put("d5", 27);
            positionToCoordinate.put("e5", 28);
            positionToCoordinate.put("f5", 29);
            positionToCoordinate.put("g5", 30);
            positionToCoordinate.put("h5", 31);
            positionToCoordinate.put("a4", 32);
            positionToCoordinate.put("b4", 33);
            positionToCoordinate.put("c4", 34);
            positionToCoordinate.put("d4", 35);
            positionToCoordinate.put("e4", 36);
            positionToCoordinate.put("f4", 37);
            positionToCoordinate.put("g4", 38);
            positionToCoordinate.put("h4", 39);
            positionToCoordinate.put("a3", 40);
            positionToCoordinate.put("b3", 41);
            positionToCoordinate.put("c3", 42);
            positionToCoordinate.put("d3", 43);
            positionToCoordinate.put("e3", 44);
            positionToCoordinate.put("f3", 45);
            positionToCoordinate.put("g3", 46);
            positionToCoordinate.put("h3", 47);
            positionToCoordinate.put("a2", 48);
            positionToCoordinate.put("b2", 49);
            positionToCoordinate.put("c2", 50);
            positionToCoordinate.put("d2", 51);
            positionToCoordinate.put("e2", 52);
            positionToCoordinate.put("f2", 53);
            positionToCoordinate.put("g2", 54);
            positionToCoordinate.put("h2", 55);
            positionToCoordinate.put("a1", 56);
            positionToCoordinate.put("b1", 57);
            positionToCoordinate.put("c1", 58);
            positionToCoordinate.put("d1", 59);
            positionToCoordinate.put("e1", 60);
            positionToCoordinate.put("f1", 61);
            positionToCoordinate.put("g1", 62);
            positionToCoordinate.put("h1", 63);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

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

    /** Constructor
     * <p>
     * DO NOT CONSTRUCT AN INSTANCE OF THIS CLASS
     * <p>
     * BoardUtils is used to store constants and static methods
     * and should not be instantiated.
     */
    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate me!");
    }

    /** This method is used to check if the given coordinate is valid.
     *
     * @param coord the coordinate
     * @return true if the coordinate is valid, false otherwise
     */
    public static boolean isValidTileCoordinate(final int coord) {
        return coord >= 0 && coord < NUM_TILES;
    }

    public static int getCoordinateAtPosition(final String position) {
        return ALGEBRAIC_NOTATION.get(position);
    }

    public static int getPositionAtCoordinate(final int coord) {
        return POSITION_TO_COORDINATE.get(coord);
    }

}
