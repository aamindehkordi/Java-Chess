package tests.com.chess.tests;

import com.chess.model.board.Board;
import com.chess.controller.ai.MiniMax;
import com.chess.model.pgn.FenUtilities;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by amir.afghani on 6/4/17.
 * Tests from: <a href="https://chessprogramming.wikispaces.com/Perft+Results">...</a>
 */
public class TestEngine {

    @Test
    public void kiwiPeteDepth1() {
        final Board board = FenUtilities.createGameFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        final MiniMax miniMax = new MiniMax(1);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 48L);
    }

    @Test
    public void kiwiPeteDepth2() {
        final Board board = FenUtilities.createGameFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        final MiniMax miniMax = new MiniMax(2);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 2039L);
    }

    @Test
    public void kiwiPeteDepth3() {
        final Board board = FenUtilities.createGameFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        final MiniMax miniMax = new MiniMax(3);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 97862L);
    }

    @Test
    public void kiwiPeteDepth4() {
        final Board board = FenUtilities.createGameFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        final MiniMax miniMax = new MiniMax(4);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 4085603L);
    }

    @Test
    public void kiwiPeteDepth5() {
        final Board board = FenUtilities.createGameFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        final MiniMax miniMax = new MiniMax(5);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 193690690L);
    }


    @Test
    public void testPosition3Depth1() {
        final Board board = FenUtilities.createGameFromFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
        final MiniMax miniMax = new MiniMax(1);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 14L);
    }

    @Test
    public void testPosition3Depth2() {
        final Board board = FenUtilities.createGameFromFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
        final MiniMax miniMax = new MiniMax(2);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 191L);
    }

    @Test
    public void testPosition3Depth3() {
        final Board board = FenUtilities.createGameFromFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
        final MiniMax miniMax = new MiniMax(3);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 2812L);
    }

    @Test
    public void testPosition3Depth4() {
        final Board board = FenUtilities.createGameFromFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
        final MiniMax miniMax = new MiniMax(4);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 43238L);
    }

    @Test
    public void testPosition3Depth5() {
        final Board board = FenUtilities.createGameFromFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
        final MiniMax miniMax = new MiniMax(5);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 674624L);
    }

    @Test
    public void testPosition4Depth1() {
        final Board board = FenUtilities.createGameFromFEN("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        final MiniMax miniMax = new MiniMax(1);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 6L);
    }

    @Test
    public void testPosition4Depth2() {
        final Board board = FenUtilities.createGameFromFEN("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        final MiniMax miniMax = new MiniMax(2);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 264L);
    }

    @Test
    public void testPosition4Depth3() {
        final Board board = FenUtilities.createGameFromFEN("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        final MiniMax miniMax = new MiniMax(3);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 9467L);
    }

    @Test
    public void testPosition4Depth4() {
        final Board board = FenUtilities.createGameFromFEN("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        final MiniMax miniMax = new MiniMax(4);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 422333L);
    }

    @Test
    public void testPosition4Depth5() {
        final Board board = FenUtilities.createGameFromFEN("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        final MiniMax miniMax = new MiniMax(5);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 15833292L);
    }

    @Test
    public void testPosition5Depth1() {
        final Board board = FenUtilities.createGameFromFEN("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        final MiniMax miniMax = new MiniMax(1);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 44L);
    }

    @Test
    public void testPosition5Depth2() {
        final Board board = FenUtilities.createGameFromFEN("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        final MiniMax miniMax = new MiniMax(2);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 1486L);
    }

    @Test
    public void testPosition5Depth3() {
        final Board board = FenUtilities.createGameFromFEN("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        final MiniMax miniMax = new MiniMax(3);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 62379L);
    }

    @Test
    public void testPosition5Depth4() {
        final Board board = FenUtilities.createGameFromFEN("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        final MiniMax miniMax = new MiniMax(4);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 2103487L);
    }

    @Test
    public void testPosition5Depth5() {
        final Board board = FenUtilities.createGameFromFEN("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        final MiniMax miniMax = new MiniMax(5);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 89941194L);
    }

    @Test
    public void testPosition6Depth4() {
        final Board board = FenUtilities.createGameFromFEN("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10\n");
        final MiniMax miniMax = new MiniMax(4);
        miniMax.execute(board);
        assertEquals(miniMax.getNumBoardsEvaluated(), 3894594L);
    }

}
