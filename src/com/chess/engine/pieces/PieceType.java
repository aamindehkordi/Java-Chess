package com.chess.engine.pieces;

public enum PieceType {

    PAWN("P") {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    KNIGHT("N") {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    BISHOP("B") {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    ROOK("R") {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return true;
        }
    },
    QUEEN("Q") {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    KING("K") {
        @Override
        public boolean isKing() {
            return true;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    };

    private final String pieceName;

    /** Constructor
     * @param pieceName the name of the piece
     */
    PieceType(final String pieceName) {
        this.pieceName = pieceName;
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
