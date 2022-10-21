package com.chess.engine.board;

public class BoardUtils {

    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate me!");
    }

    public static boolean isValidTileCoordinate(int coord) {
        return coord >= 0 && coord < 64;
    }


}
