package tests.com.chess.tests;

import com.chess.controller.Alliance;
import com.chess.model.board.Board;
import com.chess.model.board.Board.Builder;
import com.chess.model.board.BoardUtils;
import com.chess.model.board.Move.MoveFactory;
import com.chess.model.pieces.Bishop;
import com.chess.model.pieces.King;
import com.chess.model.pieces.Pawn;
import com.chess.controller.MoveTransition;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestStaleMate {
    @Test
    public void testAnandKramnikStaleMate() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(21, Alliance.BLACK));
        builder.setPiece(new King(36, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(29, Alliance.WHITE));
        builder.setPiece(new King(31, Alliance.WHITE));
        builder.setPiece(new Pawn(39, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.BLACK);
        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStaleMate());
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e4"),
                        BoardUtils.getCoordinateAtPosition("f5")));
        assertTrue(t1.getMoveStatus().isDone());
        //TODO: Stalemate is not being detected
        assertTrue(t1.getTransitionBoard().currentPlayer().isInStaleMate());
        assertFalse(t1.getTransitionBoard().currentPlayer().isInCheck());
        assertFalse(t1.getTransitionBoard().currentPlayer().isInCheckMate());
    }

    @Test
    public void testAnonymousStaleMate() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(2, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(10, Alliance.WHITE));
        builder.setPiece(new King(26, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStaleMate());
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("c5"),
                        BoardUtils.getCoordinateAtPosition("c6")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getTransitionBoard().currentPlayer().isInStaleMate());
        assertFalse(t1.getTransitionBoard().currentPlayer().isInCheck());
        assertFalse(t1.getTransitionBoard().currentPlayer().isInCheckMate());
    }

    @Test
    public void testAnonymousStaleMate2() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(0, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(16, Alliance.WHITE));
        builder.setPiece(new King(17, Alliance.WHITE));
        builder.setPiece(new Bishop(19, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStaleMate());
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("a6"),
                        BoardUtils.getCoordinateAtPosition("a7")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getTransitionBoard().currentPlayer().isInStaleMate());
        assertFalse(t1.getTransitionBoard().currentPlayer().isInCheck());
        assertFalse(t1.getTransitionBoard().currentPlayer().isInCheckMate());
    }
}