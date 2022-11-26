package com.chess.pgn;


import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.*;

import static com.chess.engine.board.Board.Builder;

public class FenUtilities {
//TODO ADD COMMENTS

    private FenUtilities() {
        throw new RuntimeException("Not Instantiable!");
    }

    public static Board createGameFromFEN(final String fenString) {
        return parseFEN(fenString);
    }

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

    private static Board parseFEN(final String fenString) {
        final String[] fenPartitions = fenString.trim().split(" ");
        final Builder builder = new Builder();
        final boolean whiteKingSideCastle = whiteKingSideCastle(fenPartitions[2]);
        final boolean whiteQueenSideCastle = whiteQueenSideCastle(fenPartitions[2]);
        final boolean blackKingSideCastle = blackKingSideCastle(fenPartitions[2]);
        final boolean blackQueenSideCastle = blackQueenSideCastle(fenPartitions[2]);
        final String gameConfiguration = fenPartitions[0];
        final char[] boardTiles = gameConfiguration.replaceAll("/", "")
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
            switch (boardTiles[i]) {
                case 'r':
                    builder.setPiece(new Rook(i, Alliance.BLACK));
                    i++;
                    break;
                case 'n':
                    builder.setPiece(new Knight(i, Alliance.BLACK));
                    i++;
                    break;
                case 'b':
                    builder.setPiece(new Bishop(i, Alliance.BLACK));
                    i++;
                    break;
                case 'q':
                    builder.setPiece(new Queen(i, Alliance.BLACK));
                    i++;
                    break;
                case 'k':
                    final boolean isCastled = !blackKingSideCastle && !blackQueenSideCastle;
                    builder.setPiece(new King(Alliance.BLACK, i, blackKingSideCastle, blackQueenSideCastle));
                    i++;
                    break;
                case 'p':
                    builder.setPiece(new Pawn(i, Alliance.BLACK));
                    i++;
                    break;
                case 'R':
                    builder.setPiece(new Rook(i, Alliance.WHITE));
                    i++;
                    break;
                case 'N':
                    builder.setPiece(new Knight(i, Alliance.WHITE));
                    i++;
                    break;
                case 'B':
                    builder.setPiece(new Bishop(i, Alliance.WHITE));
                    i++;
                    break;
                case 'Q':
                    builder.setPiece(new Queen(i, Alliance.WHITE));
                    i++;
                    break;
                case 'K':
                    builder.setPiece(new King(Alliance.WHITE, i, whiteKingSideCastle, whiteQueenSideCastle));
                    i++;
                    break;
                case 'P':
                    builder.setPiece(new Pawn(i, Alliance.WHITE));
                    i++;
                    break;
                case '-':
                    i++;
                    break;
                default:
                    throw new RuntimeException("Invalid FEN String " +gameConfiguration);
            }
        }
        builder.setMoveMaker(moveMaker(fenPartitions[1]));
        return builder.build();
    }

    private static Alliance moveMaker(final String moveMakerString) {
        if(moveMakerString.equals("w")) {
            return Alliance.WHITE;
        } else if(moveMakerString.equals("b")) {
            return Alliance.BLACK;
        }
        throw new RuntimeException("Invalid FEN String " +moveMakerString);
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

    private static String calculateEnPassantSquare(final Board board) {
        final Pawn enPassantPawn = board.getEnPassantPawn();
        if(enPassantPawn != null) {
            return BoardUtils.getPositionAtCoordinate(enPassantPawn.getPiecePosition() +
                    (8) * enPassantPawn.getPieceAlliance().getOppositeDirection());
        }
        return "-";
    }
    private static String calculateCurrentPlayerText(final Board board) {
        return board.currentPlayer().toString().substring(0, 1).toLowerCase();
    }

}
