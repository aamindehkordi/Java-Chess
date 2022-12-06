package tests.com.chess.tests;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.*;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.pgn.FenUtilities;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestMiniMax {

    @Test
    public void testOpeningDepth1() {
        final Board board = Board.createStandardBoard();
        final MiniMax miniMax = new MiniMax(1);
        miniMax.execute(board);
        final long numBoardsEvaluated = miniMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 20L);
    }

    @Test
    public void testOpeningDepth2() {
        final Board board = Board.createStandardBoard();
        final MiniMax miniMax = new MiniMax(2);
        miniMax.execute(board);
        final long numBoardsEvaluated = miniMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 400L);
    }

    @Test
    public void testOpeningDepth3() {
        final Board board = Board.createStandardBoard();
        final MiniMax miniMax = new MiniMax(3);
        miniMax.execute(board);
        final long numBoardsEvaluated = miniMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 8902L);
    }

    @Test
    public void testOpeningDepth4() {
        final Board board = Board.createStandardBoard();
        final MiniMax miniMax = new MiniMax(4);
        miniMax.execute(board);
        final long numBoardsEvaluated = miniMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 197281L);
    }

    @Test
    public void testOpeningDepth5() {
        final Board board = Board.createStandardBoard();
        final MiniMax miniMax = new MiniMax(5);
        miniMax.execute(board);
        final long numBoardsEvaluated = miniMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 4865609L);
    }

    @Test
    public void testOpeningDepth6() {
        final Board board = Board.createStandardBoard();
        final MiniMax miniMax = new MiniMax(6);
        miniMax.execute(board);
        final long numBoardsEvaluated = miniMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 119060324L);
    }

    @Test
    public void testKiwiPeteDepth1() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK, false));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Queen(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Bishop(14, Alliance.BLACK));
        builder.setPiece(new Bishop(16, Alliance.BLACK));
        builder.setPiece(new Knight(17, Alliance.BLACK));
        builder.setPiece(new Pawn(20, Alliance.BLACK));
        builder.setPiece(new Knight(21, Alliance.BLACK));
        builder.setPiece(new Pawn(22, Alliance.BLACK));
        builder.setPiece(new Pawn(33, Alliance.BLACK));
        builder.setPiece(new Pawn(47, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(27, Alliance.WHITE));
        builder.setPiece(new Knight(28, Alliance.WHITE));
        builder.setPiece(new Pawn(36, Alliance.WHITE));
        builder.setPiece(new Knight(42, Alliance.WHITE));
        builder.setPiece(new Queen(45, Alliance.WHITE));
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Bishop(51, Alliance.WHITE));
        builder.setPiece(new Bishop(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false));
        builder.setPiece(new Rook(63, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final MiniMax miniMax = new MiniMax(1);
        miniMax.execute(board);
        final long numBoardsEvaluated = miniMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 46);
    }

    @Test
    public void testKiwiPeteDepth2() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK, false));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Queen(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Bishop(14, Alliance.BLACK));
        builder.setPiece(new Bishop(16, Alliance.BLACK));
        builder.setPiece(new Knight(17, Alliance.BLACK));
        builder.setPiece(new Pawn(20, Alliance.BLACK));
        builder.setPiece(new Knight(21, Alliance.BLACK));
        builder.setPiece(new Pawn(22, Alliance.BLACK));
        builder.setPiece(new Pawn(33, Alliance.BLACK));
        builder.setPiece(new Pawn(47, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(27, Alliance.WHITE));
        builder.setPiece(new Knight(28, Alliance.WHITE));
        builder.setPiece(new Pawn(36, Alliance.WHITE));
        builder.setPiece(new Knight(42, Alliance.WHITE));
        builder.setPiece(new Queen(45, Alliance.WHITE));
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Bishop(51, Alliance.WHITE));
        builder.setPiece(new Bishop(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false));
        builder.setPiece(new Rook(63, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        //System.out.println(FenUtilities.createFENFromGame(board));
        final MiniMax miniMax = new MiniMax(2);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 1866L);
    }


    @Test
    public void engineIntegrity1() {
        final Board board = FenUtilities.createGameFromFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -\n");
        final MiniMax miniMax = new MiniMax(3);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 11030083);
    }

    @Test
    public void testKiwiPeteDepth2Bug2() {
        final Board board = FenUtilities.createGameFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e5"),
                        BoardUtils.getCoordinateAtPosition("d7")));
        final MiniMax miniMax = new MiniMax(2);
        miniMax.execute(t1.getTransitionBoard());
        assertEquals(miniMax.getNumBoardsEvaluated(), 45);
    }

    @Test
    public void testChessDotComGame() {
        final Board board = FenUtilities.createGameFromFEN("rnbk1bnr/1pN2ppp/p7/3P2q1/3Pp3/8/PPP1QPPP/RN2KB1R w KQ - 18 10");
        final MiniMax miniMax = new MiniMax(3);
        miniMax.execute(board);
    }


    @Test
    public void testPosition3Depth1() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(19, Alliance.BLACK));
        builder.setPiece(new Rook(31, Alliance.BLACK));
        builder.setPiece(new Pawn(37, Alliance.BLACK));
        builder.setPiece(new King(39, Alliance.BLACK, false));
        // White Layout
        builder.setPiece(new King(24, Alliance.WHITE, false));
        builder.setPiece(new Pawn(25, Alliance.WHITE));
        builder.setPiece(new Rook(33, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final MiniMax miniMax = new MiniMax(1);
        miniMax.execute(board);
        final long numBoardsEvaluated = miniMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 14);
    }

    @Test
    public void testPosition3Depth2() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(19, Alliance.BLACK));
        builder.setPiece(new Rook(31, Alliance.BLACK));
        builder.setPiece(new Pawn(37, Alliance.BLACK));
        builder.setPiece(new King(39, Alliance.BLACK, false));
        // White Layout
        builder.setPiece(new King(24, Alliance.WHITE, false));
        builder.setPiece(new Pawn(25, Alliance.WHITE));
        builder.setPiece(new Rook(33, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final MiniMax miniMax = new MiniMax(2);
        miniMax.execute(board);
        final long numBoardsEvaluated = miniMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 191);
    }
    
    @Test
    public void testPosition3Depth3() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(19, Alliance.BLACK));
        builder.setPiece(new Rook(31, Alliance.BLACK));
        builder.setPiece(new Pawn(37, Alliance.BLACK));
        builder.setPiece(new King(39, Alliance.BLACK, false));
        // White Layout
        builder.setPiece(new King(24, Alliance.WHITE, false));
        builder.setPiece(new Pawn(25, Alliance.WHITE));
        builder.setPiece(new Rook(33, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final MiniMax miniMax = new MiniMax(3);
        miniMax.execute(board);
        final long numBoardsEvaluated = miniMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 2812);
    }
    
    @Test
    public void testPosition3Depth4() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(19, Alliance.BLACK));
        builder.setPiece(new Rook(31, Alliance.BLACK));
        builder.setPiece(new Pawn(37, Alliance.BLACK));
        builder.setPiece(new King(39, Alliance.BLACK, false));
        // White Layout
        builder.setPiece(new King(24, Alliance.WHITE, false));
        builder.setPiece(new Pawn(25, Alliance.WHITE));
        builder.setPiece(new Rook(33, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final MiniMax miniMax = new MiniMax(4);
        miniMax.execute(board);
        final long numBoardsEvaluated = miniMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 43238);
    }
    
}
