package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Tile {

    // The tile coordinate
    protected final int tileCoord;

    // The cache of all the tiles
    private static final Map< Integer, EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();

    /** Create all the possible empty tiles
     *
     * @return an unmodifiable map of all the possible empty tiles
     */
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {

        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        // Create all the empty tiles
        for (int i = 0; i < 64; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        // Return an unmodifiable map
        return Collections.unmodifiableMap(new LinkedHashMap<>(emptyTileMap));
        //return ImmutableMap.copyOf(emptyTileMap);
    }

    /** Create a tile
     *
     * @param tileCoord the tile coordinate
     * @param piece the piece on the tile
     * @return the tile
     */
    public static Tile createTile(final int tileCoord, final Piece piece) {
        /* If the piece is null, return the empty tile from the cache
         * Otherwise, return a new occupied tile
         */
        return piece != null ? new OccupiedTile(tileCoord, piece) : EMPTY_TILES.get(tileCoord);
    }

    /** Constructor
     *
     * @param tileCoord the tile coordinate
     */
    Tile(int tileCoord) {
        this.tileCoord = tileCoord;
    }

    /** Is the tile occupied?
     *
     * @return true if the tile is occupied, false otherwise
     */
    public abstract boolean isTileOccupied();

    /** Get the piece on the tile
     *
     * @return the piece on the tile
     */
    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile {

        /** Constructor
         *
         * @param coordinate the tile coordinate
         */
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

        /** Constructor
         *
         * @param tileCoord the tile coordinate
         * @param pieceOnTile the piece on the tile
         */
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
