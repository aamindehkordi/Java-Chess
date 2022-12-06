package com.chess.model.pgn;


import com.chess.controller.Alliance;
import com.chess.model.board.Board;
import com.chess.model.board.BoardUtils;
import com.chess.model.pieces.*;

import static com.chess.model.board.Board.Builder;

/**
 * This class is a utility class that is used to create a board from a FEN string.
 */
public class FenUtilities {
//TODO ADD COMMENTS

    /**
     * This is throwing a runtime exception because the FEN string is not valid.
     */
    private FenUtilities() {
        throw new RuntimeException("Not Instantiable!");
    }

    /**
     * This method is used to create a board from a FEN string.
     * @param fenString
     * @return
     */
    public static Board createGameFromFEN(final String fenString) {
        return parseFEN(fenString);
    }

    /**
     * This is the method that parses the FEN string and creates a board.
     * @param board
     * @return
     */
    public static String createFENFromGame(final Board board) {
        return generateFEN(board);
    }

    /** Generates a correct FEN string from a board following FEN rules
     * Example of starting board FEN would be:
     * rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
     *
     * @param board the board
     * @return the FEN string
     */
    private static String generateFEN(final Board board) {
        // Create a string builder to build the FEN string
        final StringBuilder fenBuilder = new StringBuilder();
        int emptyTileCount = 0;
        // Loop through the board tiles
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            // Get the piece at the current tile
            final Piece piece = board.getTile(i).getPiece();
            // If the tile is empty, add a dash
            if (piece == null) {
                fenBuilder.append("-");
                emptyTileCount++;
            } else { // If the tile is not empty
                // If there are empty tiles
                if (emptyTileCount > 0) {
                    // Remove the empty tiles from the FEN string
                    for (int j = 0; j < emptyTileCount; j++) {
                        fenBuilder.deleteCharAt(fenBuilder.length() - 1);
                    }
                    // Add the empty tile count to the FEN string
                    fenBuilder.append(emptyTileCount);
                    // Reset the empty tile count
                    emptyTileCount = 0;
                }
                // If the piece is white, add the piece type as a capital letter
                if (piece.getPieceAlliance() == Alliance.WHITE) {
                    fenBuilder.append(piece.getPieceType().toString());
                } else { // If the piece is black, add the piece type as a lowercase letter
                    fenBuilder.append(piece.getPieceType().toString().toLowerCase());
                }
            }
            // If the current tile is the end of a row
            if ((i + 1) % 8 == 0 && i != 63) {
                // If there are empty tiles
                if (emptyTileCount > 0) {
                    // Remove all preceding '-'s
                    for (int j = 0; j < emptyTileCount; j++) {
                        fenBuilder.deleteCharAt(fenBuilder.length() - 1);
                    }
                    // Add the empty tile count to the FEN string
                    fenBuilder.append(emptyTileCount);
                    // Reset the empty tile count
                    emptyTileCount = 0;
                }

                // Add a slash to the FEN string
                fenBuilder.append("/");
            }
        }

        // Add the current player
        fenBuilder.append(" " + calculateCurrentPlayerText(board));

        // Add the castling rights
        fenBuilder.append(" " + calculateCastleText(board));

        // Add the en passant square
        fenBuilder.append(" " + calculateEnPassantSquare(board));

        // Add the half move clock
        fenBuilder.append(" " + board.HalfMoveClock());

        // Add the full move number which is the current move number
        fenBuilder.append(" " + board.getMoveCounter());

        // Return the FEN string
        return fenBuilder.toString();

    }

    /**
     * This method is used to parse the FEN string
     * @param fenString
     * @return
     */
    private static Board parseFEN(final String fenString) {
        final String[] fenPartitions = fenString.trim().split(" "); // Split the FEN string into partitions
        final Builder builder = new Builder();                            // Create a new board builder
        final boolean whiteKingSideCastle = whiteKingSideCastle(fenPartitions[2]);       // Check if white can castle kingside
        final boolean whiteQueenSideCastle = whiteQueenSideCastle(fenPartitions[2]);     // Check if white can castle queenside
        final boolean blackKingSideCastle = blackKingSideCastle(fenPartitions[2]);       // Check if black can castle kingside
        final boolean blackQueenSideCastle = blackQueenSideCastle(fenPartitions[2]);     // Check if black can castle queenside
        final String gameConfiguration = fenPartitions[0];                               // Get the game configuration
        final char[] boardTiles = gameConfiguration.replaceAll("/", "") // Remove all slashes from the game configuration
                .replaceAll("8", "--------")
                .replaceAll("7", "-------")
                .replaceAll("6", "------")
                .replaceAll("5", "-----")
                .replaceAll("4", "----")
                .replaceAll("3", "---")
                .replaceAll("2", "--")
                .replaceAll("1", "-")
                .toCharArray();
        int i = 0;
        while (i < boardTiles.length) {
            switch (boardTiles[i]) {                                // Switch on the current tile
                case 'r':
                    builder.setPiece(new Rook(i, Alliance.BLACK));  // If the tile is a black rook, add a black rook to the board
                    i++;
                    break;
                case 'n':
                    builder.setPiece(new Knight(i, Alliance.BLACK)); // If the tile is a black knight, add a black knight to the board
                    i++;
                    break;
                case 'b':
                    builder.setPiece(new Bishop(i, Alliance.BLACK)); // If the tile is a black bishop, add a black bishop to the board
                    i++;
                    break;
                case 'q':
                    builder.setPiece(new Queen(i, Alliance.BLACK)); // If the tile is a black queen, add a black queen to the board
                    i++;
                    break;
                case 'k':
                    final boolean isCastled = !blackKingSideCastle && !blackQueenSideCastle; // Check if the black king is castled
                    builder.setPiece(new King(Alliance.BLACK, i, blackKingSideCastle, blackQueenSideCastle)); // Add a black king to the board
                    i++;
                    break;
                case 'p':
                    builder.setPiece(new Pawn(i, Alliance.BLACK)); // If the tile is a black pawn, add a black pawn to the board
                    i++;
                    break;
                case 'R':
                    builder.setPiece(new Rook(i, Alliance.WHITE)); // If the tile is a white rook, add a white rook to the board
                    i++;
                    break;
                case 'N':
                    builder.setPiece(new Knight(i, Alliance.WHITE)); // If the tile is a white knight, add a white knight to the board
                    i++;
                    break;
                case 'B':
                    builder.setPiece(new Bishop(i, Alliance.WHITE)); // If the tile is a white bishop, add a white bishop to the board
                    i++;
                    break;
                case 'Q':
                    builder.setPiece(new Queen(i, Alliance.WHITE)); // If the tile is a white queen, add a white queen to the board
                    i++;
                    break;
                case 'K':
                    builder.setPiece(new King(Alliance.WHITE, i, whiteKingSideCastle, whiteQueenSideCastle)); // Add a white king to the board
                    i++;
                    break;
                case 'P':
                    builder.setPiece(new Pawn(i, Alliance.WHITE)); // If the tile is a white pawn, add a white pawn to the board
                    i++;
                    break;
                case '-':
                    i++;
                    break;
                default:
                    throw new RuntimeException("Invalid FEN String " +gameConfiguration); // Throw an exception if the FEN string is invalid
            }
        }
        builder.setMoveMaker(moveMaker(fenPartitions[1]));  // Set the current player
        return builder.build();                             // Build the board
    }

    private static Alliance moveMaker(final String moveMakerString) {
        if(moveMakerString.equals("w")) {           // If the current player is white
            return Alliance.WHITE;
        } else if(moveMakerString.equals("b")) {    // If the current player is black
            return Alliance.BLACK;
        }
        throw new RuntimeException("Invalid FEN String " +moveMakerString);     // Throw an exception if the FEN string is invalid
    }

    private static boolean whiteKingSideCastle(final String fenCastleString) {
        return fenCastleString.contains("K");
    }

    private static boolean whiteQueenSideCastle(final String fenCastleString) {
        return fenCastleString.contains("Q");
    }

    private static boolean blackKingSideCastle(final String fenCastleString) {
        return fenCastleString.contains("k");
    }

    private static boolean blackQueenSideCastle(final String fenCastleString) {
        return fenCastleString.contains("q");
    }

    /**
     * This method is used to calculate the current player text
     * @param board
     * @return
     */

    private static String calculateCastleText(final Board board) {
        final StringBuilder builder = new StringBuilder();
        if(!board.whitePlayer().isKingSideCastleCapable()) {
            builder.append("K");
        }
        if(!board.whitePlayer().isQueenSideCastleCapable()) {
            builder.append("Q");
        }
        if(!board.blackPlayer().isKingSideCastleCapable()) {
            builder.append("k");
        }
        if(!board.blackPlayer().isQueenSideCastleCapable()) {
            builder.append("q");
        }
        final String result = builder.toString();

        return result.isEmpty() ? "-" : result;
    }

    /**
     * This method is used to calculate en passant text
     * @param board
     * @return
     */
    private static String calculateEnPassantSquare(final Board board) {
        final Pawn enPassantPawn = board.getEnPassantPawn();
        if(enPassantPawn != null) {
            return BoardUtils.getPositionAtCoordinate(enPassantPawn.getPiecePosition() +
                    (8) * enPassantPawn.getPieceAlliance().getOppositeDirection());
        }
        return "-";
    }

    /**
     * This method is used to calculate the current player text
     * @param board
     * @return
     */
    private static String calculateCurrentPlayerText(final Board board) {
        return board.currentPlayer().toString().substring(0, 1).toLowerCase();
    }

}
