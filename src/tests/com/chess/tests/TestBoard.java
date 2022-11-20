package tests.com.chess.tests;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.pieces.*;
import com.chess.engine.player.MoveTransition;
import org.junit.Test;

import static com.chess.engine.Alliance.BLACK;
import static com.chess.engine.Alliance.WHITE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class TestBoard {

    @Test
    public void initialBoard() {

        final Board board = Board.createStandardBoard();
        assertEquals(board.currentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.currentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
        assertFalse(board.currentPlayer().isCastled());
        assertTrue(board.currentPlayer().isKingSideCastleCapable());
        assertTrue(board.currentPlayer().isQueenSideCastleCapable());
        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.currentPlayer().getOpponent().isCastled());
        assertTrue(board.currentPlayer().getOpponent().isKingSideCastleCapable());
        assertTrue(board.currentPlayer().getOpponent().isQueenSideCastleCapable());
        assertEquals("White", board.whitePlayer().toString());
        assertEquals("Black", board.blackPlayer().toString());
        /*
        final Iterable<Piece> allPieces = board.getAllPieces();
        final Iterable<Move> allMoves = Iterables.concat(board.whitePlayer().getLegalMoves(), board.blackPlayer().getLegalMoves());
        for(final Move move : allMoves) {
            assertFalse(move.isAttack());
            assertFalse(move.isCastlingMove());
            assertEquals(MoveUtils.exchangeScore(move), 1);
        }
        assertEquals(Iterables.size(allMoves), 40);
        assertEquals(Iterables.size(allPieces), 32);
        assertFalse(BoardUtils.isEndGame(board));
        assertFalse(BoardUtils.isThreatenedBoardImmediate(board));
        assertEquals(StandardBoardEvaluator.get().evaluate(board, 0), 0);
        */
        assertNull(board.getTile(35));
    }

    @Test
    public void testPlainKingMove() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(4, BLACK));
        builder.setPiece(new Pawn(12, WHITE));
        // White Layout
        builder.setPiece(new Pawn(52, BLACK));
        builder.setPiece(new King(60, WHITE));
        builder.setMoveMaker(WHITE);
        // Set the current player
        final Board board = builder.build();
        System.out.println(board);

        assertEquals(board.whitePlayer().getLegalMoves().size(), 6);
        assertEquals(board.blackPlayer().getLegalMoves().size(), 6);
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());
        /*
        BoardEvaluator evaluator = StandardBoardEvaluator.get();
        System.out.println(evaluator.evaluate(board, 0));
        assertEquals(StandardBoardEvaluator.get().evaluate(board, 0), 0);

        final Move move = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e1"),
                BoardUtils.getCoordinateAtPosition("f1"));

        final MoveTransition moveTransition = board.currentPlayer()
                .makeMove(move);

        assertEquals(moveTransition.getTransitionMove(), move);
        assertEquals(moveTransition.getFromBoard(), board);
        assertEquals(moveTransition.getTransitionBoard().currentPlayer(), moveTransition.getTransitionBoard().blackPlayer());

        assertTrue(moveTransition.getMoveStatus().isDone());
        assertEquals(moveTransition.getTransitionBoard().whitePlayer().getPlayerKing().getPiecePosition(), 61);
        System.out.println(moveTransition.getTransitionBoard());
                    
         */

    }

    @Test
    public void testBoardConsistency() {
        final Board board = Board.createStandardBoard();
        assertEquals(board.currentPlayer(), board.whitePlayer());

        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));
        final MoveTransition t2 = t1.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        final MoveTransition t3 = t2.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));
        final MoveTransition t4 = t3.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d5")));

        final MoveTransition t5 = t4.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t4.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e4"),
                        BoardUtils.getCoordinateAtPosition("d5")));
        final MoveTransition t6 = t5.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t5.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("d5")));

        final MoveTransition t7 = t6.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t6.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("f3"),
                        BoardUtils.getCoordinateAtPosition("g5")));
        final MoveTransition t8 = t7.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t7.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("f7"),
                        BoardUtils.getCoordinateAtPosition("f6")));

        final MoveTransition t9 = t8.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t8.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d1"),
                        BoardUtils.getCoordinateAtPosition("h5")));
        final MoveTransition t10 = t9.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t9.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("g7"),
                        BoardUtils.getCoordinateAtPosition("g6")));

        final MoveTransition t11 = t10.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t10.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("h5"),
                        BoardUtils.getCoordinateAtPosition("h4")));
        final MoveTransition t12 = t11.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t11.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("f6"),
                        BoardUtils.getCoordinateAtPosition("g5")));

        final MoveTransition t13 = t12.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t12.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("h4"),
                        BoardUtils.getCoordinateAtPosition("g5")));
        final MoveTransition t14 = t13.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t13.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d5"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t14.getTransitionBoard().whitePlayer().getActivePieces().size() == calculatedActivesFor(t14.getTransitionBoard(), WHITE));
        assertTrue(t14.getTransitionBoard().blackPlayer().getActivePieces().size() == calculatedActivesFor(t14.getTransitionBoard(), BLACK));

    }

    @Test(expected=RuntimeException.class)
    public void testInvalidBoard() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Pawn(0, BLACK));
        builder.setPiece(new Pawn(1, BLACK));
        builder.setPiece(new Pawn(2, BLACK));
        builder.setPiece(new Pawn(3, BLACK));
        builder.setPiece(new Pawn(4, BLACK));
        builder.setPiece(new Pawn(5, BLACK));
        builder.setPiece(new Pawn(6, BLACK));
        builder.setPiece(new Pawn(7, BLACK));
        builder.setPiece(new Pawn(8, BLACK));
        builder.setPiece(new Pawn(9, BLACK));
        builder.setPiece(new Pawn(10, BLACK));
        builder.setPiece(new Pawn(11, BLACK));
        builder.setPiece(new Pawn(12, BLACK));
        builder.setPiece(new Pawn(13, BLACK));
        builder.setPiece(new Pawn(14, BLACK));
        builder.setPiece(new Pawn(15, BLACK));
        builder.setPiece(new Pawn(48, BLACK));
        builder.setPiece(new Pawn(49, BLACK));
        builder.setPiece(new Pawn(50, BLACK));
        builder.setPiece(new Pawn(51, BLACK));
        builder.setPiece(new Pawn(52, BLACK));
        builder.setPiece(new Pawn(53, BLACK));
        builder.setPiece(new Pawn(54, BLACK));
            
        builder.setPiece(new Rook(56, BLACK));
        builder.setPiece(new Knight(57, BLACK));
        builder.setPiece(new Bishop(58, BLACK));
        builder.setPiece(new Queen(59, BLACK));
        builder.setPiece(new King(60, BLACK));
        builder.setPiece(new Bishop(61, BLACK));
        builder.setPiece(new Knight(62, BLACK));
        builder.setPiece(new Rook(63, BLACK));
        // White Layout
        builder.setPiece(new Pawn(16, WHITE));
        builder.setPiece(new Pawn(17, WHITE));
        builder.setPiece(new Pawn(18, WHITE));
        builder.setPiece(new Pawn(19, WHITE));
        builder.setPiece(new Pawn(20, WHITE));
        builder.setPiece(new Pawn(21, WHITE));
        builder.setPiece(new Pawn(22, WHITE));
        builder.setPiece(new Pawn(23, WHITE));
        builder.setPiece(new Pawn(24, WHITE));
        builder.setPiece(new Pawn(25, WHITE));
        builder.setPiece(new Pawn(26, WHITE));
        builder.setPiece(new Pawn(27, WHITE));
        builder.setPiece(new Pawn(28, WHITE));
        builder.setPiece(new Pawn(29, WHITE));
        builder.setPiece(new Pawn(30, WHITE));
        builder.setPiece(new Pawn(31, WHITE));
        builder.setPiece(new Pawn(55, WHITE));
        builder.setPiece(new Pawn(40, WHITE));
        builder.setPiece(new Pawn(41, WHITE));
        builder.setPiece(new Pawn(42, WHITE));
        builder.setPiece(new Pawn(43, WHITE));
        builder.setPiece(new Pawn(44, WHITE));
        builder.setPiece(new Pawn(45, WHITE));
        builder.setPiece(new Pawn(46, WHITE));
        builder.setPiece(new Pawn(47, WHITE));
            
        builder.setPiece(new Rook(56, WHITE));
        builder.setPiece(new Knight(57, WHITE));
        builder.setPiece(new Bishop(58, WHITE));
        builder.setPiece(new Queen(59, WHITE));
        builder.setPiece(new King(60, WHITE));
        builder.setPiece(new Bishop(61, WHITE));
        builder.setPiece(new Knight(62, WHITE));
        builder.setPiece(new Rook(63, WHITE));
        // Set the current player
        builder.setMoveMaker(WHITE);
        // build the board
        builder.build();
    }

    @Test
    public void testAlgebreicNotation() {
        assertEquals(BoardUtils.getPositionAtCoordinate(0), "a8");
        assertEquals(BoardUtils.getPositionAtCoordinate(1), "b8");
        assertEquals(BoardUtils.getPositionAtCoordinate(2), "c8");
        assertEquals(BoardUtils.getPositionAtCoordinate(3), "d8");
        assertEquals(BoardUtils.getPositionAtCoordinate(4), "e8");
        assertEquals(BoardUtils.getPositionAtCoordinate(5), "f8");
        assertEquals(BoardUtils.getPositionAtCoordinate(6), "g8");
        assertEquals(BoardUtils.getPositionAtCoordinate(7), "h8");
    }

    @Test
    public void mem() {
        final Runtime runtime = Runtime.getRuntime();
        long start, end;
        runtime.gc();
        start = runtime.freeMemory();
        Board board = Board.createStandardBoard();
        end =  runtime.freeMemory();
        System.out.println("That took " + (start-end) + " bytes.");

    }
    private static int calculatedActivesFor(final Board board,
                                            final Alliance alliance) {
        int count = 0;
        for (final Piece p : board.getAllPieces()) {
            if (p.getPieceAlliance().equals(alliance)) {
                count++;
            }
        }
        return count;
    }

}

