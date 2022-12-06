package tests.com.chess.tests;

import com.chess.controller.Alliance;
import com.chess.model.board.Board;
import com.chess.model.board.Board.Builder;
import com.chess.model.pieces.King;
import com.chess.model.pieces.Pawn;
import com.chess.controller.ai.KingSafetyAnalyzer;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestKingSafety {

    @Test
    public void test1() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setMoveMaker(Alliance.WHITE);
        // Set the current player
        final Board board = builder.build();

        assertEquals(KingSafetyAnalyzer.get().calculateKingTropism(board.whitePlayer()), 0);
    }

}