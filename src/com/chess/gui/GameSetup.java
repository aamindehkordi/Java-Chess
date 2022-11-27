package com.chess.gui;

import com.chess.engine.Alliance;
import com.chess.engine.player.Player;
import com.chess.gui.Table.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The game setup performs the setup for the game.
 * This setup includes the following:
 * 1. The game type (human vs. human, human vs. computer, computer vs. computer)
 * 2. The player type (white, black, random)
 * 3. The difficulty level (easy, medium, hard)
 * 4. The buttons to control the setup
 */
class GameSetup extends JDialog {

    /**
     * This initializes the white player type.
     */
    private PlayerType whitePlayerType;

    /**
     * This initializes the white player type.
     */
    private PlayerType blackPlayerType;

    /**
     * JSpinner is a component that lets the user select a number from a sequence of numbers.
     */
    private final JSpinner searchDepthSpinner;

/**
     * HUMAN_TEXT is a constant for the human text.
     */
    private static final String HUMAN_TEXT = "Human";

    /**
     * COMPUTER_TEXT is a constant for the computer text.
     */
    private static final String COMPUTER_TEXT = "Computer";

    /**
     * Creates a new {@code GameSetup} instance.
     *
     * @param frame the parent frame
     * @param modal whether the dialog is modal
     */
    GameSetup(final JFrame frame,
              final boolean modal) {
        super(frame, modal);
        final JPanel myPanel = new JPanel(new GridLayout(0, 1)); //
        final JRadioButton whiteHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton whiteComputerButton = new JRadioButton(COMPUTER_TEXT);
        final JRadioButton blackHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton blackComputerButton = new JRadioButton(COMPUTER_TEXT);
        whiteHumanButton.setActionCommand(HUMAN_TEXT);
        final ButtonGroup whiteGroup = new ButtonGroup();
        whiteGroup.add(whiteHumanButton);
        whiteGroup.add(whiteComputerButton);
        whiteHumanButton.setSelected(true);

        final ButtonGroup blackGroup = new ButtonGroup();
        blackGroup.add(blackHumanButton);
        blackGroup.add(blackComputerButton);
        blackHumanButton.setSelected(true);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("White"));
        myPanel.add(whiteHumanButton);
        myPanel.add(whiteComputerButton);
        myPanel.add(new JLabel("Black"));
        myPanel.add(blackHumanButton);
        myPanel.add(blackComputerButton);

        myPanel.add(new JLabel("Search"));
        this.searchDepthSpinner = addLabeledSpinner(myPanel, "Search Depth", new SpinnerNumberModel(6, 0, Integer.MAX_VALUE, 1));

        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("OK");

        //ActionListener is a functional interface that is used to perform an action when an event occurs.
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whitePlayerType = whiteComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                blackPlayerType = blackComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                GameSetup.this.setVisible(false);
            }
        });

        //Cancel button
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel");
                GameSetup.this.setVisible(false);
            }
        });

        myPanel.add(cancelButton);
        myPanel.add(okButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }

    /**
     * This method prompts the user to select the game setup.
     */
    void promptUser() {
        setVisible(true);
        repaint();
    }

    /**
     * This boolean value is used to determine if one of the players is a computer.
     */
    boolean isAIPlayer(final Player player) {
        if(player.getAlliance() == Alliance.WHITE) {
            return getWhitePlayerType() == PlayerType.COMPUTER;
        }
        return getBlackPlayerType() == PlayerType.COMPUTER;
    }

    /**
     * Gets the white player type.
     *
     * @return the white player type
     */
    PlayerType getWhitePlayerType() {
        return this.whitePlayerType;
    }

    /**
     * Returns the black player type.
     *
     * @return the black player type
     */
    PlayerType getBlackPlayerType() {
        return this.blackPlayerType;
    }

    /**
     * This method adds a labeled spinner to the panel.
     *
     * @param c the panel
     * @param label the label
     * @param model the model
     * @return the spinner
     */
    private static JSpinner addLabeledSpinner(final Container c,
                                              final String label,
                                              final SpinnerModel model) {
        final JLabel l = new JLabel(label);
        c.add(l);
        final JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);
        return spinner;
    }

    /**
     * Returns the search depth.
     *
     * @return the search depth
     */
    int getSearchDepth() {
        return (Integer)this.searchDepthSpinner.getValue();
    }

    /**
     * setPlayerMovedPiece is a method that sets the player moved piece.
     */
    public void setPlayerMovedPiece(Alliance alliance) {
        if(alliance == Alliance.WHITE) {
            this.whitePlayerType = PlayerType.HUMAN;
        } else {
            this.blackPlayerType = PlayerType.HUMAN;
        }
    }

    /**
     * setAIMovePiece is a method that sets the AI move piece.
     */
    public void setAIMovePiece(Alliance alliance) {
        if(alliance == Alliance.WHITE) {
            this.whitePlayerType = PlayerType.COMPUTER;
        } else {
            this.blackPlayerType = PlayerType.COMPUTER;
        }
    }

}
