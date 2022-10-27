package com.chess.engine.pieces;

public enum PieceType {

    /** Pawn */
    PAWN("P", 100) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    /** Knight */
    KNIGHT("N", 300) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    /** Bishop */
    BISHOP("B", 300) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    /** Rook */
    ROOK("R", 500) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return true;
        }
    },
    /** Queen */
    QUEEN("Q", 900) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    /** King */
    KING("K", 10000) {
        @Override
        public boolean isKing() {
            return true;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    };

    /** The piece's name */
    private final String pieceName;
    /** The piece's value */
    private final int pieceValue;

    /** Constructor
     * @param pieceName the name of the piece
     */
    PieceType(final String pieceName, final int pieceValue) {
        this.pieceName = pieceName;
        this.pieceValue = pieceValue;
    }

    public int getPieceValue() {
        return pieceValue;
    }

    @Override
    public String toString() {
        return this.pieceName;
    }

    /** Is the piece a king?
     * @return true if the piece is a king, false otherwise
     */
    public abstract boolean isKing();

    /** Is the piece a rook?
     * @return true if the piece is a rook, false otherwise
     */
    public abstract boolean isRook();
}
