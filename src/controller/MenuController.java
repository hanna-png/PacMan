package controller;

import view.MenuView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController implements ActionListener {
    private MenuView menuView;
    private GameController gameController;

    public MenuController(){
        menuView = new MenuView();

        for (var entry : menuView.getButtons().entrySet()){
            entry.getValue().setActionCommand(entry.getKey());
            entry.getValue().addActionListener(this);
        }
        menuView.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Exit":
                System.exit(0);
                break;
            case "New Game":
                startNewGame();
                break;
            case "High Scores":
                showHighScores();
                break;

        }
    }

    private void startNewGame() {
        while (true) {
            String widthStr = JOptionPane.showInputDialog(
                    menuView,
                    "Please enter board width (10–100):",
                    "Board Width",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (widthStr == null) return;

            String heightStr = JOptionPane.showInputDialog(
                    menuView,
                    "Please enter board height (10–100):",
                    "Board Height",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (heightStr == null) return;

            try {
                int width = Integer.parseInt(widthStr.trim());
                int height = Integer.parseInt(heightStr.trim());

                if (width < 10 || width > 100 || height < 10 || height > 100) {
                    JOptionPane.showMessageDialog(menuView,
                            "Both width and height must be between 10 and 100.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                }


                 gameController = new GameController(width, height);
                menuView.dispose();
                return;

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(menuView,
                        "Please enter valid numbers for width and height.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void showHighScores() {
      new ScoreController();
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
