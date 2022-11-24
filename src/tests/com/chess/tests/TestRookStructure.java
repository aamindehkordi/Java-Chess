package tests.com.chess.tests;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.ai.RookStructureAnalyzer;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRookStructure {

    @Test
    public void test1() {
        final Board board = Board.createStandardBoard();
        assertEquals(RookStructureAnalyzer.get().rookStructureScore(board.whitePlayer()), 0);
        assertEquals(RookStructureAnalyzer.get().rookStructureScore(board.whitePlayer()), 0);
    }

    @Test
    public void test2() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        // White Layout
        builder.setPiece(new Rook(63, Alliance.WHITE));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);
        // Set the current player
        final Board board = builder.build();
        assertEquals(RookStructureAnalyzer.get().rookStructureScore(board.whitePlayer()), 25);
        assertEquals(RookStructureAnalyzer.get().rookStructureScore(board.whitePlayer()), 25);
    }


}

