package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {

    /* The board */
    protected final Board board;
    /* The piece being moved */
    protected final Piece movedPiece;
    /* The destination coordinate */
    protected final int destinationCoordinate;
    /* Is this a first move? */
    protected final boolean isFirstMove;


    /** Constructor
     * @param board the board
     * @param movedPiece the piece that is being moved
     * @param destinationCoordinate the destination coordinate
     */
    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = movedPiece;
        this.isFirstMove = false;
    }

    /** Constructor
     * @param board the board
     * @param movedPiece the piece that is being moved
     * @param destinationCoordinate the destination coordinate
     */
    Move(final Board board, final Piece movedPiece, final int destinationCoordinate, final boolean isFirstMove) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = isFirstMove;
        printValues();
    }

    /**
     * Print the values of the parameters for this move.
     */
    void printValues() {
        //System.out.println("-------------------");
        //System.out.println("board: \n" + this.board);
        //System.out.println("movedPiece: " + this.movedPiece);
        //System.out.println("destinationCoordinate: " + this.destinationCoordinate);
        //System.out.println("isFirstMove: " + this.isFirstMove);
        //System.out.println("-------------------");
    }

    /** Hashcode
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        result = prime * result + (this.isFirstMove ? 1 : 0);
        return result;
    }

    /** Equals
     * @param other the other move
     * @return true if the moves are equal, false otherwise
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) { /* if the moves are the same */
            return true;
        }
        if (!(other instanceof final Move otherMove)) { /* if the other object is not a move */
            return false;
        }
        /* cast the other object to a move */
        return  getCurrentCoordinate() == otherMove.getCurrentCoordinate() && /* True if the current coordinates are the same and */
                getDestinationCoordinate() == otherMove.getDestinationCoordinate() && /* True if the destination coordinates are the same */
                getMovedPiece().equals(otherMove.getMovedPiece()); /* and the moved pieces are the same */
    }

    /** Get the destination coordinate
     * @return the destination coordinate
     */
    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    /** Get the moved piece
     * @return the moved piece
     */
    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    /** Get the current Board
     * @return the current board
     */
    public  Board getBoard() {
    	return this.board;
    }

    /** Checks if the move is an attacking move
     *
     * @return true if the move is an attack, false otherwise
     */
    public boolean isAttack() {
        return false;
    }

    /** Checks if the move is a castling move
     *
     * @return true if the move is a castling move, false otherwise
     */
    public boolean isCastlingMove() {
        return false;
    }

    /** Gets the attacked piece
     *
     * @return the attacked piece
     */
    public Piece getAttackedPiece() {
        return null;
    }
    /** Executes the standard move
     * @return the new board
     */
    public Board execute() {

        final Builder builder = new Builder(); /* create a new board builder */

        for (final Piece piece : this.board.currentPlayer().getActivePieces()) { /* for each piece on the board */

            if (!this.movedPiece.equals(piece)) { /* for all the pieces except the moved piece */
                builder.setPiece(piece); /* add them to the new board */
            }
        }
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) { /* for each piece on the board */
            builder.setPiece(piece); /* add the piece to the board */
        }
        builder.setPiece(this.movedPiece.movePiece(this)); /* move the piece */
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance()); /* set the move maker to the opponent */

        return builder.build(); /* build the board */
    }

    /** Gets the current Coordinate of a piece
     *
     * @return the current coordinate of a piece
     */
    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public Move getMove() {
        return this;
    }

    /** Undo the move
     *
     * @return the board after the move has been undone
     */
    public Board undo() {
        /* create a new board builder */
        final Board.Builder builder = new Builder();

        /* for each piece on the board, add it to the new board */
        this.board.getAllPieces().forEach(builder::setPiece);
        builder.setMoveMaker(this.board.currentPlayer().getAlliance());
        return builder.build();
    }

    /** The Major Move class */
    public static final class MajorMove extends Move {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         */
        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
            this.printValues();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorMove && super.equals(other); // True if the moves are the same or if the other move is a major move and the moves are equal
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate); // Return the piece type and the destination coordinate
        }

    }

    /** The Attack Move class */
    public static class MajorAttackMove extends AttackMove {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param attackedPiece the piece that is being attacked
         */
        public MajorAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
            this.printValues();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorAttackMove && super.equals(other); // True if the moves are
                                                                                         // the same or if the other
                                                                                         // move is a major attack
                                                                                         // move and the moves are equal
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate); // Return the piece type and the destination coordinate
        }
    }
    public static class AttackMove extends Move {
        final Piece attackedPiece;

        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param attackedPiece the piece that is being attacked
         */
        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
            this.printValues();

        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) { /* if the moves are the same */
                return true;
            }
            if (!(other instanceof final AttackMove otherAttackMove)) { /* if the other object is not an attack move */
                return false;
            }
            /* cast the other object to an attack move */
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece()); /* True if the destination coordinates are the same */
        }
        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate); // Return the piece type and the destination coordinate
        }
    }

    public static class PawnMove extends Move {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         */
        public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
            this.printValues();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnMove && super.equals(other); // True if the moves are the same
                                                                                      // or if the other move is a pawn
        }                                                                             // move and the moves are equal

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate); // Return the destination coordinate
        }


    }

    public static class PawnAttackMove extends AttackMove {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param attackedPiece the piece that is being attacked
         */
        public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
            this.printValues();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnAttackMove && super.equals(other); // True if the moves are the same or if the other move is a pawn attack move and the moves are equal
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1)
                    + "x" + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate); // Return the piece type and the destination coordinate
        }
    }

    public static class PawnPromotion extends Move {
        /** The decorated move is the move that is being promoted */
        final Move decoratedMove;
        /** The pawn that is being promoted */
        final Pawn promotedPawn;

        /** Constructor
         *
         * @param decoratedMove the move that is being decorated
         */
        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
            this.printValues();
        }

        @Override
        public Board execute() {
            /* Create a new board with the move executed */
            final Board pawnMovedBoard = this.decoratedMove.execute();
            /* Create a new board builder */
            final Board.Builder builder = new Builder();
            /* Loop through the current player's active pieces */
            for (final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()) {
                /* If the piece is not the promoted pawn */
                if (!this.promotedPawn.equals(piece)) {
                    /* Add the piece to the board builder */
                    builder.setPiece(piece);
                }
            }
            /* Loop through the current player's opponent's active pieces */
            for (final Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()) {
                /* Add the piece to the board builder */
                builder.setPiece(piece);
            }
            /* Create the Promoted piece */
            Piece queen = this.promotedPawn.getPromotionPiece();
            /* Add the promoted piece to the board builder */
            builder.setPiece(queen.movePiece(this));
            /* Set the move maker */
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());
            /* Return the new board */
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return this.decoratedMove.getAttackedPiece();
        }

        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + (31 * this.promotedPawn.hashCode());
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnPromotion && (super.equals(other));
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() + disambiguationFile() + "x" +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

        /** Get the disambiguation file
         * <p>
         * Disambiguation is used to distinguish between two pieces of the same type that can move to the same square
         *
         * @return the disambiguation file or an empty string if there is no disambiguation
         */
        private String disambiguationFile() {
            for (final Move move : this.board.currentPlayer().getLegalMoves()) { // Loop through the current player's legal moves
                if (move.getDestinationCoordinate() == this.destinationCoordinate && // If the move's destination coordinate is the same as this move's destination coordinate
                        !this.movedPiece.equals(move.getMovedPiece()) && // and the move's moved piece is not the same as this move's moved piece
                        this.movedPiece.getPieceType().equals(move.getMovedPiece().getPieceType())) { // and the move's moved piece type is the same as this move's moved piece type
                    return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1); // Return the file of the moved piece
                }
            }
            return "";
        }


        /** Returns the decorated move
         *
         * @return the decorated move
         */
        public Move getDecoratedMove() {
            return this.decoratedMove;
        }

        /** Returns the pawn that is being promoted
         *
         * @return the pawn that is being promoted
         */
        public Pawn getPromotedPawn() {
            return this.promotedPawn;
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param attackedPiece the piece that is being attacked
         */
        public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
            this.printValues();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other); // True if the moves are the same or if the other move is a pawn en passant attack move and the moves are equal
        }

        @Override
        public int hashCode() {
            return super.hashCode() + this.attackedPiece.hashCode();
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder(); // Create a new builder
            this.board.currentPlayer().getActivePieces().stream().filter(piece -> !this.movedPiece.equals(piece)).forEach(builder::setPiece); // Add all the pieces to the builder except the moved piece
            this.board.currentPlayer().getOpponent().getActivePieces().stream().filter(piece -> !piece.equals(this.getAttackedPiece())).forEach(builder::setPiece); // Add all the opponent's pieces to the builder except the attacked piece
            builder.setPiece(this.movedPiece.movePiece(this)); // Move the piece
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance()); // Set the move maker to the opponent
            return builder.build(); // Build the board
        }
    }

    public static final class PawnJump extends PawnMove {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         */
        public PawnJump(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
            this.printValues();
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder(); /* create a new board builder */
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) { /* for each piece on the board */
                if (!this.movedPiece.equals(piece)) { /* for all the pieces except the moved piece */
                    builder.setPiece(piece); /* add them to the new board */
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) { /* for each piece on the board */
                builder.setPiece(piece); /* add the piece to the board */
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this); /* move the piece */
            builder.setEnPassantPawn(movedPawn); /* set the en passant pawn */
            builder.setPiece(movedPawn); /* add the moved piece to the board */ /* only difference */
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance()); /* set the move maker to the opponent */
            return builder.build(); /* build the board */
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate); // Return the destination coordinate
        }
    }

    public static abstract class CastleMove extends Move {
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param castleRook the rook that is being castled
         * @param castleRookStart the start coordinate of the rook
         * @param castleRookDestination the destination coordinate of the rook
         */
        public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
            this.printValues();
        }

        /** Returns the rook that is being castled
         *
         * @return the rook that is being castled
         */
        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder(); /* create a new board builder */
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) { /* for each friendly piece on the board */
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) { /* for all the pieces except the moved piece and the castle rook */
                    builder.setPiece(piece); /* add them to the new board */
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) { /* for each ennemy piece on the board */
                builder.setPiece(piece); /* add the piece to the board */
            }
            King king = this.board.currentPlayer().getPlayerKing(); /* get the king */
            builder.setPiece(king.movePiece(this, true)); /* move the piece */
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance())); /* move the rook */
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance()); /* set the move maker to the opponent */
            return builder.build(); /* build the board */
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CastleMove)) {
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param castleRook the rook that is being castled
         * @param castleRookStart the start coordinate of the rook
         * @param castleRookDestination the destination coordinate of the rook
         */
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
            this.printValues();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof KingSideCastleMove && super.equals(other);
        }

        @Override
        public String toString() {
            return "O-O";
        }

    }

    public static final class QueenSideCastleMove extends CastleMove {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param castleRook the rook that is being castled
         * @param castleRookStart the start coordinate of the rook
         * @param castleRookDestination the destination coordinate of the rook
         */
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
            this.printValues();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }
    }

    public static final class NullMove extends Move {
        /** Constructor
         *
         */
        public NullMove() {
            super(null, null, -1);
            this.printValues();
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute the null move!");
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }

        @Override
        public int hashCode() {
            return 1;
        }



    }

    public static class MoveFactory {

        /** A null move */
        public static final Move NULL_MOVE = new NullMove();

        /** Constructor
         *
         */
        private MoveFactory() {
            throw new RuntimeException("Not instantiable!");
        }

        /** Get the Null Move
         *
         * @return null move
         */
        public static Move getNullMove() {
            return NULL_MOVE;
        }

        /** Create a move
         *
         * @param board the board
         * @param currentCoordinate the current coordinate
         * @param destinationCoordinate the destination coordinate
         * @return the move
         */
        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {

            for(final Move move : board.getAllLegalMoves()) {
                if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}
