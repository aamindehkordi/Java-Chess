package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameHistoryPanel extends JPanel {

    private final DataModel model;  /* the data model */
    private final JScrollPane scrollPane;   /* the scroll pane */
    private static final Dimension HISTORY_PANEL_DIMENSION =
            new Dimension(100, 400);   /* the dimension for the panel */
    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");  /* the bg color for the panel */
    private static final Dimension MOVE_LOG_DIMENSION =
            new Dimension(100, 400);    /* the dimension for the table */

    /**
     * Constructor
     */
    GameHistoryPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); /* set the layout */
        this.model = new DataModel(); /* create the data model */
        final JTable table = new JTable(model); /* create the table */
        table.setRowHeight(15); /* set the row height */
        this.scrollPane = new JScrollPane(table); /* create the scroll pane */
        scrollPane.setColumnHeaderView(table.getTableHeader()); /* set the column header */
        scrollPane.setPreferredSize(MOVE_LOG_DIMENSION); /* set the dimension */
        this.add(scrollPane, BorderLayout.CENTER); /* add the scroll pane to the center */
        this.setBackground(PANEL_COLOR); /* set the bg color */
        this.setPreferredSize(HISTORY_PANEL_DIMENSION); /* set the dimension */
        this.setVisible(true); /* set the panel visible */
    }

    /**
     * Redraw the game history panel
     *
     * @param moveLog the move log
     */
    void redo(final Board board, final Table.MoveLog moveLog) {
        int currentRow = 0; /* the current row */
        this.model.clear(); /* clear the data model */
        for (final Move move : moveLog.getMoves()) { /* for each move in the move log */
            final String moveText = move.toString(); /* get the move text */
            if (move.getMovedPiece().getPieceAlliance().isWhite()) { /* if the piece is white */
                this.model.setValueAt(moveText,
                        currentRow, 0); /* set the move text in the current row and column 0 */
            } else if (move.getMovedPiece().getPieceAlliance().isBlack()) { /* if the piece is black */
                this.model.setValueAt(moveText,
                        currentRow, 1); /* set the move text in the current row and column 1 */
                currentRow++; /* increment the current row */
            }
        }
        if (moveLog.getMoves().size() > 0) { /* if there are moves in the move log */
            final Move lastMove = moveLog.getMoves().get(moveLog.size() - 1); /* get the last move */
            final String moveText = lastMove.toString(); /* get the move text */
            if (lastMove.getMovedPiece().getPieceAlliance().isWhite()) { /* if the piece is white */
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board),
                        currentRow, 0); /* set the move text in the current row and column 0 */
            } else if (lastMove.getMovedPiece().getPieceAlliance().isBlack()) { /* if the piece is black */
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board),
                        --currentRow, 1); /* set the move text in the current row and column 1 */
            }
        }
        final JScrollBar vertical = scrollPane.getVerticalScrollBar(); /* get the vertical scroll bar */
        vertical.setValue(vertical.getMaximum()); /* set the value to the maximum */
    }

    /**
     * Calculate the check and checkmate hash
     *
     * @param board the board
     * @return the check and checkmate hash
     */
    private String calculateCheckAndCheckMateHash(final Board board) {
        if (board.currentPlayer().isInCheckMate()) { /* if the player is in checkmate */
            return "#"; /* return the checkmate hash */
        } else if (board.currentPlayer().isInCheck()) { /* if the player is in check */
            return "+"; /* return the check hash */
        }
        return ""; /* return an empty string */
    }

    /**
     * The row class is used to store the data in the table
     *
     */
    private static class Row {
        private String whiteMove;
        private String blackMove;

        Row() {
        }

        public String getWhiteMove() {
            return whiteMove;
        }

        public String getBlackMove() {
            return blackMove;
        }

        public void setWhiteMove(final String move) {
            this.whiteMove = move;
        }

        public void setBlackMove(final String move) {
            this.blackMove = move;
        }
    }

    /**
     * The data model class is responsible for
     * 1. Creating the table
     * 2. Adding the data to the table
     * 3. Clearing the table
     * 4. Setting the column names
     * 5. Setting the column width
     * 6. Setting the column alignment
     */
    private static class DataModel extends DefaultTableModel {
//TODO ADD COMMENTS

        private final List<Row> values;
        private static final String[] NAMES = {"White", "Black"};

        DataModel() {
            this.values = new ArrayList<>();
        }

        public void clear() {
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount() {
            if (this.values == null) {
                return 0;
            }
            return this.values.size();
        }

        @Override
        public int getColumnCount() {
            return NAMES.length;
        }

        @Override
        public Object getValueAt(final int row, final int column) {
            final Row currentRow = this.values.get(row);
            if (column == 0) {
                return currentRow.getWhiteMove();
            } else if (column == 1) {
                return currentRow.getBlackMove();
            }
            return null;
        }

        @Override
        public void setValueAt(final Object aValue, final int row, final int column) {
            final Row currentRow;
            if (this.values.size() <= row) {
                currentRow = new Row();
                this.values.add(currentRow);
            } else {
                currentRow = this.values.get(row);
            }
            if (column == 0) {
                currentRow.setWhiteMove((String) aValue);
                fireTableRowsInserted(row, row);
            } else if (column == 1) {
                currentRow.setBlackMove((String) aValue);
                fireTableCellUpdated(row, column);
            }
        }

        @Override
        public Class<?> getColumnClass(final int column) {
            return Move.class;
        }

        @Override
        public String getColumnName(final int column) {
            return NAMES[column];
        }
    }



}
