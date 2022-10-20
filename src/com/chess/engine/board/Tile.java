package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import java.util.HashMap;
import java.util.Map;
import static java.util.Collections.unmodifiableMap;

public abstract class Tile {

    protected final int tileCoord;

    private static final Map< Integer, EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        //
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < 64; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return unmodifiableMap(emptyTileMap);
        //return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile(final int tileCoord, final Piece piece) {
        return piece != null ? new OccupiedTile(tileCoord, piece) : EMPTY_TILES.get(tileCoord);
    }
    Tile(int tileCoord) {
        this.tileCoord = tileCoord;
    }
    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile {
        EmptyTile(int coordinate) {
            super(coordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile;

        OccupiedTile(int tileCoord, Piece pieceOnTile) {
            super(tileCoord);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
    }
}
