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

        @Override
        public boolean isPawn() {
            return true;
        }

        @Override
        public boolean isBishop() {
            return false;
        }

        @Override
        public boolean isKnight() {
            return false;
        }

        @Override
        public boolean isQueen() {
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

        @Override
        public boolean isPawn() {return false;}

        @Override
        public boolean isBishop() {
            return false;
        }

        @Override
        public boolean isKnight() {
            return true;
        }

        @Override
        public boolean isQueen() {
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

        @Override
        public boolean isPawn() {return false;}

        @Override
        public boolean isBishop() {
            return true;
        }

        @Override
        public boolean isKnight() {
            return false;
        }

        @Override
        public boolean isQueen() {
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

        @Override
        public boolean isPawn() {return false;}

        @Override
        public boolean isBishop() {
            return false;
        }

        @Override
        public boolean isKnight() {
            return false;
        }

        @Override
        public boolean isQueen() {
            return false;
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

        @Override
        public boolean isPawn() {return false;}

        @Override
        public boolean isBishop() {
            return false;
        }

        @Override
        public boolean isKnight() {
            return false;
        }

        @Override
        public boolean isQueen() {
            return true;
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

        @Override
        public boolean isPawn() {return false;}

        @Override
        public boolean isBishop() {
            return false;
        }

        @Override
        public boolean isKnight() {
            return false;
        }

        @Override
        public boolean isQueen() {
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

    /** Returns the piece's value
     * @return the piece's value
     */
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

    /** Is this piece a Bishop?
     * @return true if the piece is a bishop, false otherwise
     */
    public abstract boolean isBishop();

    /** Is this piece a Knight?
     * @return true if the piece is a knight, false otherwise
     */
    public abstract boolean isKnight();

    /** Is this piece a Queen?
     * @return true if the piece is a queen, false otherwise
     */
    public abstract boolean isQueen();

    /** Is this piece a Pawn?
     * @return true if the piece is a pawn, false otherwise
     */
    public abstract boolean isPawn();
}
