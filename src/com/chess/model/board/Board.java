package com.chess.model.board;

import com.chess.controller.Alliance;
import com.chess.model.board.Move.MoveFactory;
import com.chess.model.pieces.*;
import com.chess.controller.*;

import java.util.*;

/**
 * The board
 */
public class Board {

    private final List<Tile> gameBoard; /* the game board */
    private final WhitePlayer whitePlayer; /* the white player */
    private final BlackPlayer blackPlayer; /* the black player */
    private final Collection<Piece> whitePieces; /* the white pieces */
    private final Collection<Piece> blackPieces; /* the black pieces */
    private final Player currentPlayer; /* the current player */
    private final Pawn enPassantPawn; /* the pawn that can be captured en passant */
    private Move transitionMove; /* the move transition */
    private int moveCount;
    private int halfMoveClock;
    private MoveTransitionTracker moveTransitionTracker;


    /** Constructor
    *
    * @param builder the builder
    */
    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder); /* create the game board */
        this.enPassantPawn = builder.enPassantPawn; /* the pawn that can be captured en passant */
        /* the white pieces */
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE); /* calculate the white pieces */
        /* the black pieces */
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK); /* calculate the black pieces */
        this.moveCount = builder.moveCount;
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces); /* calculate the white legal moves */
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces); /* calculate the black legal moves */
        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves); /* create the white player */
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves); /* create the black player */
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer); /* set the current player */
        this.transitionMove = builder.transitionMove != null ? builder.transitionMove : MoveFactory.getNullMove();
        this.moveTransitionTracker = new MoveTransitionTracker(this);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(); /* the string builder */
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) { /* for each tile */
            final String tileText = this.gameBoard.get(i).toString(); /* get the string representation of the tile, example: "  "  or "wp" */
            builder.append(String.format("%3s", tileText)); /* append the string representation of the tile to the string builder, %3s = 3 spaces */
            if ((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) { /* if the tile is at the end of the row */
                builder.append("\n"); /* append a new line to the string builder */
            }
        }
        return builder.toString();
    }

    /** Returns the black pieces
    *
    * @return the black pieces
     */
    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    /** Returns the white pieces
    *
    * @return the white pieces
     */
    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    /** Calculate the legal moves for the pieces of a certain alliance
    *
    * @param pieces the pieces
    * @return a collection of legal moves
     */
    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>(); /* the legal moves */
        for (final Piece piece : pieces) { /* for each piece */
            legalMoves.addAll(piece.calculateLegalMoves(this)); /* add the legal moves of the piece to the list of legal moves */
        }
        return Collections.unmodifiableList(new LinkedList<Move>(legalMoves)); /* return an unmodifiable list of legal moves */
    }

    /** Calculate the active pieces of a given alliance
    *
    * @param gameBoard the game board
    * @param alliance the alliance
    * @return a collection of active pieces
     */
    private static Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>(); /* the active pieces */
        for (final Tile tile : gameBoard) { /* for each tile on the board */
            if (tile.isTileOccupied()) { /* if the tile is occupied */
                final Piece piece = tile.getPiece(); /* get the piece */
                if (piece.getPieceAlliance() == alliance) { /* if the piece is the same alliance */
                    activePieces.add(piece); /* add the piece to the list of active pieces */
                }
            }
        }
        return Collections.unmodifiableList(activePieces); /* return the list of active pieces */
    }

    /** Create the game board
    *
    * @param builder the builder
     */
    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES]; /* the tiles */
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) { /* for each tile */
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i)); /* create the tile */
        }
        return Collections.unmodifiableList(new LinkedList<>(List.of(tiles))); /* return the game board */
    }

    /** Get the tile at the given position
    *
    * @param tileCoordinate the position of the tile
    * @return the tile at the given position
    */
    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate); /* return the tile at the given position */
    }

    /**
    * This class is used to build the board
    *
    * @return the builder
    */
    public static Board createStandardBoard() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK)); /* set the black rook at position 0 or A8 */
        builder.setPiece(new Knight(1, Alliance.BLACK)); /* set the black knight at position 1 or B8 */
        builder.setPiece(new Bishop(2, Alliance.BLACK)); /* set the black bishop at position 2 or C8 */
        builder.setPiece(new Queen(3, Alliance.BLACK)); /* set the black queen at position 3 or D8 */
        builder.setPiece(new King(4, Alliance.BLACK)); /* set the black king at position 4 or E8 */
        builder.setPiece(new Bishop(5, Alliance.BLACK)); /* set the black bishop at position 5 or F8 */
        builder.setPiece(new Knight(6, Alliance.BLACK)); /* set the black knight at position 6 or G8 */
        builder.setPiece(new Rook(7, Alliance.BLACK)); /* set the black rook at position 7 or H8 */
        builder.setPiece(new Pawn(8, Alliance.BLACK)); /* set the black pawn at position 8 or A7 */
        builder.setPiece(new Pawn(9, Alliance.BLACK)); /* set the black pawn at position 9 or B7 */
        builder.setPiece(new Pawn(10, Alliance.BLACK)); /* set the black pawn at position 10 or C7 */
        builder.setPiece(new Pawn(11, Alliance.BLACK)); /* set the black pawn at position 11 or D7 */
        builder.setPiece(new Pawn(12, Alliance.BLACK)); /* set the black pawn at position 12 or E7 */
        builder.setPiece(new Pawn(13, Alliance.BLACK)); /* set the black pawn at position 13 or F7 */
        builder.setPiece(new Pawn(14, Alliance.BLACK)); /* set the black pawn at position 14 or G7 */
        builder.setPiece(new Pawn(15, Alliance.BLACK)); /* set the black pawn at position 15 or H7 */
        // White Layout
        builder.setPiece(new Pawn(48, Alliance.WHITE)); /* set the white pawn at position 48 or A2 */
        builder.setPiece(new Pawn(49, Alliance.WHITE)); /* set the white pawn at position 49 or B2 */
        builder.setPiece(new Pawn(50, Alliance.WHITE)); /* set the white pawn at position 50 or C2 */
        builder.setPiece(new Pawn(51, Alliance.WHITE)); /* set the white pawn at position 51 or D2 */
        builder.setPiece(new Pawn(52, Alliance.WHITE)); /* set the white pawn at position 52 or E2 */
        builder.setPiece(new Pawn(53, Alliance.WHITE)); /* set the white pawn at position 53 or F2 */
        builder.setPiece(new Pawn(54, Alliance.WHITE)); /* set the white pawn at position 54 or G2 */
        builder.setPiece(new Pawn(55, Alliance.WHITE)); /* set the white pawn at position 55 or H2 */
        builder.setPiece(new Rook(56, Alliance.WHITE)); /* set the white rook at position 56 or A1 */
        builder.setPiece(new Knight(57, Alliance.WHITE));   /* set the white knight at position 57 or B1 */
        builder.setPiece(new Bishop(58, Alliance.WHITE));   /* set the white bishop at position 58 or C1 */
        builder.setPiece(new Queen(59, Alliance.WHITE));    /* set the white queen at position 59 or D1 */
        builder.setPiece(new King(60, Alliance.WHITE));    /* set the white king at position 60 or E1 */
        builder.setPiece(new Bishop(61, Alliance.WHITE));   /* set the white bishop at position 61 or F1 */
        builder.setPiece(new Knight(62, Alliance.WHITE));   /* set the white knight at position 62 or G1 */
        builder.setPiece(new Rook(63, Alliance.WHITE));   /* set the white rook at position 63 or H1 */
        // White to move
        builder.setMoveMaker(Alliance.WHITE); /* set the move maker to white */
        // Build the board
        return builder.build(); /* build the board */
    }

    /** Gets the Black player
     *
     * @return the Black player
     */
    public Player blackPlayer() {
        return this.blackPlayer; /* return the black player */
    }
    /** Gets the White player
     *
     * @return the White player
     */
    public Player whitePlayer() {
        return this.whitePlayer; /* return the white player */
    }

    /** Gets the current player
     *
     * @return the current player
     */
    public Player currentPlayer() {
        return this.currentPlayer; /* return the current player */
    }

    public Iterable<Move> getAllLegalMoves() {
        List<Move> allLegalMoves = new ArrayList<>(); /* the list of all legal moves */
        allLegalMoves.addAll(this.whitePlayer.getLegalMoves()); /* add all the white player's legal moves */
        allLegalMoves.addAll(this.blackPlayer.getLegalMoves()); /* add all the black player's legal moves */
        return Collections.unmodifiableList(allLegalMoves); /* return the list of all legal moves */
    }

    public Pawn getEnPassantPawn() {
        return this.enPassantPawn; /* return the en passant pawn */
    }

    /** Gets all the pieces on the board
     *
     * @return all the pieces on the board
     */
    public Collection<Piece> getAllPieces() {
        Collection<Piece> allPieces = new ArrayList<>(); /* the list of all pieces */
        allPieces.addAll(this.whitePlayer.getActivePieces()); /* add all the white player's pieces */
        allPieces.addAll(this.blackPlayer.getActivePieces()); /* add all the black player's pieces */
        return Collections.unmodifiableCollection(allPieces); /* return the list of all pieces */
    }

    public Alliance getAlliance() {
        return this.currentPlayer.getAlliance(); /* return the alliance of the current player */
    }

    public Collection<Move> getAllianceToMove() {
        return this.currentPlayer.getLegalMoves(); /* return the legal moves of the current player */
    }

    public Move getTransitionMove() {
        return this.transitionMove; /* return the transition move */
    }

    public Player getMoveMaker() {
        return this.currentPlayer; /* return the current player */
    }

    public int getMoveCounter() {
        return this.moveCount; /* return the move counter */
    }

    public MoveTransition getMoveTransition() {
        return this.transitionMove.getBoard().getMoveTransition();
    }

    public MoveTransitionTracker getMoveTransitionTracker() {
        return this.moveTransitionTracker;
    }

    public Player getPlayer(Alliance pieceAlliance) {
        if (pieceAlliance == Alliance.WHITE) {
            return this.whitePlayer; /* return the white player */
        } else if (pieceAlliance == Alliance.BLACK) {
            return this.blackPlayer; /* return the black player */
        }
        return null; /* return null */
    }


    public class MoveTransitionTracker {
        private final Board transitionBoard;

        public MoveTransitionTracker(final Board transitionBoard){
            this.transitionBoard = transitionBoard;
        }


        public String getHalfMoveClock() {
            return String.valueOf(HalfMoveClock());
        }

        public String getFullMoveNumber() {
            return String.valueOf(transitionBoard.getMoveCounter());
        }
    }

    /** The HalfMoveClock is a counter that is incremented after each move. The counter is reset to zero when a pawn is
     * moved, a piece is captured, or a move results in a pawn promotion. The counter is incremented when a player makes
     * a move that does not meet any of the above criteria. The HalfMoveClock is used to determine if a draw can be
     * claimed under the fifty-move rule.
     *
     * @return the HalfMoveClock
     */
    public int HalfMoveClock() {
        int halfMoveClock = 0; /* the half move clock */
        // check if transition move is null
        if (this.transitionMove.getMovedPiece() != null) {
            // check if the transition move is a pawn move or a capture move
            if (this.transitionMove.getMovedPiece().getPieceType().isPawn() || this.transitionMove.isAttack()) {
                halfMoveClock = 0; /* reset the half move clock */
            } else {
                halfMoveClock = this.halfMoveClock + 1; /* increment the half move clock */
            }
        }
        return halfMoveClock; /* return the half move clock */
    }


    /** ‘Builder’ class */
    public static class Builder {

        final Map<Integer, Piece> boardConfig; /* the board configuration */
        public Move transitionMove;
        public int moveCount;
        Alliance nextMoveMaker; /* the next move maker */
        Pawn enPassantPawn; /* the en passant pawn */

        /** Constructor */
        public Builder() {
            this.moveCount = 0;
            this.boardConfig = new HashMap<>(); /* initialize the board configuration */
        }

        /** Set the piece at the given position
        *
        * @param piece the piece
        * @return the builder
         */
        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece); /* set the piece at the given position */
            return this;
        }

        /** Set the next move maker
        *
        * @param nextMoveMaker the next move maker
        * @return the builder
         */
        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker; /* set the next move maker */
            this.moveCount++;
            return this;
        }
        public Builder setMoveTransition(final Move transitionMove) {
            this.transitionMove = transitionMove;
            return this;
        }

        /** Build the board
        *
        * @return the board
         */
        public Board build() {
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }
}
