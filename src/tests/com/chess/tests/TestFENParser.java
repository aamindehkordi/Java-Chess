package tests.com.chess.tests;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.player.MoveTransition;
import com.chess.engine.pgn.FenUtilities;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFENParser {


    @Test
    public void testWriteFEN1() throws IOException {
        final Board board = Board.createStandardBoard();
        final String fenString = FenUtilities.createFENFromGame(board);
        // FEN string for the initial position where neither king can castle
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", fenString);
    }

    @Test
    public void testWriteFEN2() throws IOException {
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final String fenString = FenUtilities.createFENFromGame(t1.getTransitionBoard());
        String fenString2 = null;
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fenString);
        final MoveTransition t2 = t1.getTransitionBoard().currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("c7"),
                        BoardUtils.getCoordinateAtPosition("c5")));
        assertTrue(t2.getMoveStatus().isDone());
        fenString2 = FenUtilities.createFENFromGame(t2.getTransitionBoard());
        assertEquals("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 1", fenString2);

    }

}
