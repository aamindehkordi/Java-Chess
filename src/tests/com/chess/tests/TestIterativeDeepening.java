package tests.com.chess.tests;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.*;
import org.junit.Test;

public class TestIterativeDeepening {

    @Test
    public void testOpeningDepth4BlackMovesFirst() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Rook(63, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.BLACK);

        final Board board = builder.build();

        /*
        final MoveStrategy alphaBeta = new IterativeDeepening(4);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("b8"), BoardUtils.getCoordinateAtPosition("c6")));

         */
    }

    @Test
    public void advancedLevelProblem2NakamuraShirov() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(5, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Rook(25, Alliance.BLACK));
        builder.setPiece(new Bishop(29, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Knight(27, Alliance.WHITE));
        builder.setPiece(new Rook(36, Alliance.WHITE));
        builder.setPiece(new Pawn(39, Alliance.WHITE));
        builder.setPiece(new King(42, Alliance.WHITE));
        builder.setPiece(new Pawn(46, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        /*
        final MoveStrategy iterativeDeepening = new IterativeDeepening(6);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d5"), BoardUtils.getCoordinateAtPosition("c7")));

         */
    }

    @Test
    public void eloTest1() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new King(6, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Knight(18, Alliance.BLACK));
        builder.setPiece(new Pawn(20, Alliance.BLACK));
        builder.setPiece(new Rook(21, Alliance.BLACK));
        builder.setPiece(new Pawn(23, Alliance.BLACK));
        builder.setPiece(new Queen(24, Alliance.BLACK));
        builder.setPiece(new Pawn(26, Alliance.BLACK));
        builder.setPiece(new Bishop(33, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(16, Alliance.WHITE));
        builder.setPiece(new Pawn(35, Alliance.WHITE));
        builder.setPiece(new Knight(42, Alliance.WHITE));
        builder.setPiece(new Knight(45, Alliance.WHITE));
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Queen(51, Alliance.WHITE));
        builder.setPiece(new Bishop(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setPiece(new Rook(63, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.BLACK);
        /*
        final Board board = builder.build();

        final String fen = createFENFromGame(board);
        System.out.println(fen);
        final MoveStrategy iterativeDeepening = new IterativeDeepening(7);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("c8"), BoardUtils.getCoordinateAtPosition("a6")));
        */

    }

    /*
    @Test
    public void testQualityDepth7() {
        final Board board = FenUtilities.createGameFromFEN("4k2r/1R3R2/p3p1pp/4b3/1BnNr3/8/P1P5/5K2 w - - 1 0");
        final MoveStrategy iterativeDeepening = new IterativeDeepening(7);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("f7"), BoardUtils.getCoordinateAtPosition("e7")));
    }

    @Test
    public void testQualityTwoDepth6() {
        final Board board = FenUtilities.createGameFromFEN("6k1/3b3r/1p1p4/p1n2p2/1PPNpP1q/P3Q1p1/1R1RB1P1/5K2 b - - 0-1");
        final MoveStrategy iterativeDeepening = new IterativeDeepening(6);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h4"), BoardUtils.getCoordinateAtPosition("f4")));
    }

    @Test
    public void testQualityThreeDepth6() {
        final Board board = FenUtilities.createGameFromFEN("r2r1n2/pp2bk2/2p1p2p/3q4/3PN1QP/2P3R1/P4PP1/5RK1 w - - 0 1");
        final MoveStrategy iterativeDeepening = new IterativeDeepening(7);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("g4"), BoardUtils.getCoordinateAtPosition("g7")));
    }

    @Test
    public void testQualityFourDepth6() {
        final Board board = FenUtilities.createGameFromFEN("r1b1k2r/pp3pbp/1qn1p1p1/2pnP3/3p1PP1/1P1P1NBP/P1P5/RN1QKB1R b KQkq - 2 11");
        final MoveStrategy iterativeDeepening = new IterativeDeepening(6);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d5"), BoardUtils.getCoordinateAtPosition("e3")));
    }

    */
    @Test
    public void eloTest2() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Knight(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new Knight(5, Alliance.BLACK));
        builder.setPiece(new King(6, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));
        builder.setPiece(new Pawn(20, Alliance.BLACK));
        builder.setPiece(new Pawn(22, Alliance.BLACK));
        builder.setPiece(new Pawn(24, Alliance.BLACK));
        builder.setPiece(new Bishop(25, Alliance.BLACK));
        builder.setPiece(new Pawn(27, Alliance.BLACK));
        builder.setPiece(new Pawn(33, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Queen(23, Alliance.WHITE));
        builder.setPiece(new Pawn(28, Alliance.WHITE));
        builder.setPiece(new Knight(30, Alliance.WHITE));
        builder.setPiece(new Pawn(31, Alliance.WHITE));
        builder.setPiece(new Pawn(35, Alliance.WHITE));
        builder.setPiece(new Pawn(38, Alliance.WHITE));
        builder.setPiece(new Pawn(41, Alliance.WHITE));
        builder.setPiece(new Knight(46, Alliance.WHITE));
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Bishop(54, Alliance.WHITE));
        builder.setPiece(new King(62, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        /*
        System.out.println(FenUtilities.createFENFromGame(board));
        final MoveStrategy iterativeDeepening = new IterativeDeepening(8);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("g5"), BoardUtils.getCoordinateAtPosition("h7")));

         */
    }

    @Test
    public void eloTest3() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(11, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(16, Alliance.BLACK));
        builder.setPiece(new Pawn(17, Alliance.BLACK));
        builder.setPiece(new Pawn(20, Alliance.BLACK));
        builder.setPiece(new Pawn(22, Alliance.BLACK));
        builder.setPiece(new King(25, Alliance.BLACK));
        builder.setPiece(new Knight(33, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Bishop(19, Alliance.WHITE));
        builder.setPiece(new Pawn(26, Alliance.WHITE));
        builder.setPiece(new King(36, Alliance.WHITE));
        builder.setPiece(new Rook(46, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        /*
        final MoveStrategy iterativeDeepening = new IterativeDeepening(6);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("g3"), BoardUtils.getCoordinateAtPosition("g6")));

         */
    }

    /*
    @Test
    public void blackWidowLoss1() {
        final Board board = FenUtilities.createGameFromFEN("r2qkb1r/3p1pp1/p1n1p2p/1p1bP3/P2p4/1PP5/5PPP/RNBQNRK1 w kq - 0 13");
        final MoveStrategy iterativeDeepening = new IterativeDeepening(7);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("c3"), BoardUtils.getCoordinateAtPosition("d4")));
    }

    @Test
    public void blackWidowLossWithID() {
        final Board board = FenUtilities.createGameFromFEN("r2qkb1r/3p1pp1/p1n1p2p/1p1bP3/P2p4/1PP5/5PPP/RNBQNRK1 w kq - 0 13");
        final MoveStrategy iterativeDeepening = new IterativeDeepening(7);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("c3"), BoardUtils.getCoordinateAtPosition("d4")));
    }


     */
    @Test
    public void testCheckmateHorizon() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(11, Alliance.BLACK));
        builder.setPiece(new Pawn(16, Alliance.BLACK));
        builder.setPiece(new Bishop(27, Alliance.BLACK));
        builder.setPiece(new King(29, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Rook(17, Alliance.WHITE));
        builder.setPiece(new Rook(26, Alliance.WHITE));
        builder.setPiece(new Pawn(35, Alliance.WHITE));
        builder.setPiece(new Pawn(45, Alliance.WHITE));
        builder.setPiece(new Bishop(51, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new King(63, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        /*
        final MoveStrategy iterativeDeepening = new IterativeDeepening(4);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("g2"), BoardUtils.getCoordinateAtPosition("g4")));
        */
    }

    @Test
    public void testBlackInTrouble() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Queen(11, Alliance.BLACK));
        builder.setPiece(new Rook(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));
        builder.setPiece(new Bishop(17, Alliance.BLACK));
        builder.setPiece(new Knight(18, Alliance.BLACK));
        builder.setPiece(new Pawn(19, Alliance.BLACK));
        builder.setPiece(new Pawn(21, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Knight(31, Alliance.WHITE));
        builder.setPiece(new Pawn(35, Alliance.WHITE));
        builder.setPiece(new Rook(36, Alliance.WHITE));
        builder.setPiece(new Queen(46, Alliance.WHITE));
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new King(62, Alliance.WHITE));
        // Set the current player
        /*
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final MoveStrategy iterativeDeepening = new IterativeDeepening(4);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e8")));

         */
    }

    /*
    @Test
    public void findMate3() {
        final Board board = FenUtilities.createGameFromFEN("5rk1/5Npp/8/3Q4/8/8/8/7K w - - 0");
        final MoveStrategy iterativeDeepening = new IterativeDeepening(5);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("f7"), BoardUtils.getCoordinateAtPosition("h6")));
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(bestMove);
        assertTrue(t1.getMoveStatus().isDone());
    }

    @Test
    public void runawayPawn() {
        final Board board = FenUtilities.createGameFromFEN("2k5/8/8/8/p7/8/8/4K3 b - - 0 1");
        final MoveStrategy iterativeDeepening = new IterativeDeepening(5);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a4"), BoardUtils.getCoordinateAtPosition("a3")));
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(bestMove);
        assertTrue(t1.getMoveStatus().isDone());
    }

    @Test
    public void testMackHackScenario() {
        final Board board = FenUtilities.createGameFromFEN("1r1k1r2/p5Q1/2p3p1/8/1q1p2n1/3P2P1/P3RPP1/4RK2 b - - 0 1");
        final MoveStrategy iterativeDeepening = new IterativeDeepening(8);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("f8"), BoardUtils.getCoordinateAtPosition("f2")));
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(bestMove);
        assertTrue(t1.getMoveStatus().isDone());
    }

    @Test
    public void testAutoResponseVsPrinChess() {
        final Board board = FenUtilities.createGameFromFEN("r2q1rk1/p1p2pp1/3p1b2/2p2QNb/4PB1P/6R1/PPPR4/2K5 b - - 0 1");
        final MoveStrategy iterativeDeepening = new IterativeDeepening(6);
        final Move bestMove = iterativeDeepening.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h5"), BoardUtils.getCoordinateAtPosition("g6")));
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(bestMove);
        assertTrue(t1.getMoveStatus().isDone());
    }

     */
}
