package tests.com.chess.tests;

import org.junit.Test;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Pawn;

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

        //assertEquals(KingSafetyAnalyzer.get().calculateKingTropism(board.whitePlayer()).tropismScore(), 40);
    }

}